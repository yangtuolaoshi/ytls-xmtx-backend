package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.common.constants.PojoConstant;
import love.ytlsnb.common.constants.QuestConstant;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;
import love.ytlsnb.model.quest.vo.QuestVO;
import love.ytlsnb.model.school.vo.LocationVO;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestLocationPhotoMapper;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.service.QuestService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户基本信息业务层实现类
 *
 * @author ula
 * @date 2024/01/30
 */
@Service
@Slf4j
public class QuestServiceImpl extends ServiceImpl<QuestMapper, Quest> implements QuestService {
    @Autowired
    private QuestMapper questMapper;
    @Autowired
    private QuestLocationMapper questLocationMapper;
    @Autowired
    private QuestLocationPhotoMapper questLocationPhotoMapper;
    @Autowired
    private SchoolClient schoolClient;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 只能接收从学校服务传来的请求，否则逻辑出错(需保证到达此处的参数仅需填充左右值和自动填充的字段) TODO 后续添加对应的逻辑校验
     *
     * @param questInsertDTO 需要新增的任务对象
     */
    @Override
    @Transactional
    public void insert(QuestInsertDTO questInsertDTO) {
        Quest quest = new Quest();
        BeanUtil.copyProperties(questInsertDTO, quest);
        quest.setStatus(QuestConstant.DISABLED);

        Long parentId = quest.getParentId();
        if (parentId == null) {
            // 需要添加的任务没有父结点
            // 检查当前学校是否已经含有无父节点的任务
            Quest selectOne = questMapper.selectOne(new QueryWrapper<Quest>()
                    .eq(QuestConstant.SCHOOL_ID, quest.getSchoolId())
                    .eq(QuestConstant.PARENT_ID, QuestConstant.NULL_PARENT_ID));
            // 已经含有，报错
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "当前学校已存在根任务");
            }
            // 没有，添加任务
            quest.setParentId(QuestConstant.NULL_PARENT_ID);
            quest.setLeftValue(QuestConstant.ROOT_QUEST_LEFT);
            quest.setRightValue(QuestConstant.ROOT_QUEST_RIGHT);

            questMapper.insert(quest);
            log.info("新增任务:{}", quest);
        } else {
            // 需要添加的任务有父节点
            // 查询父结点
            Quest parent = questMapper.selectById(parentId);
            if (parent == null) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "传入父结点参数错误");
            }
            Long parentLeft = parent.getLeftValue();
            Long parentRight = parent.getRightValue();
            // 生成当前任务结点的左右值
            Long newLeft = parentLeft + 1;
            Long newRight = parentLeft + 2;
            quest.setLeftValue(newLeft);
            quest.setRightValue(newRight);

            // 修改关联任务的左值(左值大于父结点)
            questMapper.update(null, new UpdateWrapper<Quest>()
                    .eq(QuestConstant.SCHOOL_ID, quest.getSchoolId())
                    .gt(QuestConstant.LEFT_VALUE, parentLeft)
                    .setSql("left_value = left_value + 2"));
            // 修改关联任务的右值(右值大于等于父结点)
            questMapper.update(null, new UpdateWrapper<Quest>()
                    .eq(QuestConstant.SCHOOL_ID, quest.getSchoolId())
                    .ge(QuestConstant.LEFT_VALUE, parentLeft)
                    .setSql("right_value = right_value + 2"));
            // 新增任务
            questMapper.insert(quest);
        }
    }

    /**
     * 获取当前学校的根任务
     *
     * @param schoolId 学校ID
     * @return 根据学校ID获得的Quest对象，包含了相关的所有信息
     */
    @Override
    public QuestVO getRootQuest(Long schoolId) {
        // 获取学校的根任务
        Quest rootQuest = questMapper.selectOne(new QueryWrapper<Quest>()
                .eq(QuestConstant.SCHOOL_ID, schoolId)
                .eq(QuestConstant.PARENT_ID, QuestConstant.NULL_PARENT_ID));
        if (rootQuest == null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前学校没有根任务");
        }
        QuestVO questVO = BeanUtil.copyProperties(rootQuest, QuestVO.class);

        // 封装根任务的地点信息
        Long locationId = rootQuest.getLocationId();
        QuestLocation questLocation = questLocationMapper.selectOne(new QueryWrapper<QuestLocation>()
                .eq(QuestConstant.LOCATION_ID, locationId));
        if (questLocation == null) {
            // 查询到的任务地点信息为空，当前地址信息未更新至任务数据库
            // 进行信息更新
            String updateQuestLocationKey = RedisConstant.QUEST_LOCATION_PREFIX + locationId;
            RLock lock = redissonClient.getLock(updateQuestLocationKey);
            try {
                boolean success = lock.tryLock();
                if (!success) {
                    throw new BusinessException(ResultCodes.BAD_REQUEST, "根任务数据重复更新");
                }
                // 更新地址信息
                LocationVO locationVO = updateQuestLocation(locationId);
                BeanUtil.copyProperties(locationVO, questVO, PojoConstant.ID, QuestConstant.SCHOOL_ID_JAVA);
                return questVO;
            } finally {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("释放锁失败:{},锁已经释放", updateQuestLocationKey);
                }
            }
        } else {
            // 查询到的任务地点信息不为空
            BeanUtil.copyProperties(questLocation, questVO, PojoConstant.ID);
            List<QuestLocationPhoto> questLocationPhotos = questLocationPhotoMapper.selectList(new QueryWrapper<QuestLocationPhoto>()
                    .eq(QuestConstant.LOCATION_ID, locationId));
            if (questLocationPhotos != null) {
                List<String> photos = questLocationPhotos.stream().map(QuestLocationPhoto::getPhoto).collect(Collectors.toList());
                questVO.setPhotos(photos);
            }
            return questVO;
        }
    }

    private LocationVO updateQuestLocation(Long locationId) {
        // 删除已经存在的任务地点信息
        questLocationMapper.delete(new QueryWrapper<QuestLocation>()
                .eq(QuestConstant.LOCATION_ID, locationId));
        // 调用其他服务获取数据
        Result<LocationVO> locationById = schoolClient.getWholeLocationById(locationId);
        if (locationById.getCode() != ResultCodes.OK) {
            // 远程调用异常
            throw new BusinessException(ResultCodes.BAD_REQUEST, locationById.getMsg());
        }
        LocationVO locationVO = locationById.getData();
        if (locationVO == null) {
            // 查询结果为空
            throw new BusinessException(ResultCodes.BAD_REQUEST, "地址 " + locationId + " 信息异常");
        }
        // 根据查询到的信息新增数据
        QuestLocation newQuestLocation = BeanUtil.copyProperties(locationVO, QuestLocation.class);
        newQuestLocation.setLocationId(locationId);
        newQuestLocation.setId(null);
        questLocationMapper.insert(newQuestLocation);
        // 新增任务相关照片信息
        List<String> photos = locationVO.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            for (int i = 0; i < photos.size(); i++) {
                QuestLocationPhoto questLocationPhoto = new QuestLocationPhoto();
                if (i == 0) {
                    questLocationPhoto.setCover(QuestConstant.COVER);
                } else {
                    questLocationPhoto.setCover(QuestConstant.COMMON);
                }
                questLocationPhoto.setLocationId(newQuestLocation.getLocationId());
                questLocationPhoto.setPhoto(photos.get(i));
                questLocationPhotoMapper.insert(questLocationPhoto);
            }
        }
        return locationVO;
    }
}
