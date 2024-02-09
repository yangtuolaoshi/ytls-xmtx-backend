package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.QuestConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.AdminHolder;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 只能接收从学校服务传来的请求，否则逻辑出错(需保证到达此处的参数仅需填充左右值和自动填充的字段) TODO 后续添加对应的逻辑校验
     *
     * @param quest 需要新增的任务对象
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
}
