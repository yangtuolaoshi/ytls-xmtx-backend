package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestLocationInfoVO;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestLocationPhotoMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import love.ytlsnb.quest.service.QuestLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 任务地点业务层实现类
 */
@Service
public class QuestLocationServiceImpl implements QuestLocationService {
    @Autowired
    private QuestLocationMapper questLocationMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestLocationPhotoMapper questLocationPhotoMapper;

    @Override
    public QuestLocationInfoVO getQuestSchedulePageLocation(Long scheduleId) {
        QuestLocationInfoVO questLocationInfoVO = new QuestLocationInfoVO();
        // 找进度对应的地点
        QuestSchedule questSchedule = questScheduleMapper.selectById(scheduleId);
        Long locationId = questSchedule.getLocationId();
        questLocationInfoVO.setLocationId(locationId);
        QuestLocation questLocation = questLocationMapper.selectById(locationId);
        BeanUtil.copyProperties(questLocation, questLocationInfoVO);
        // 找地点的照片集合
        LambdaQueryWrapper<QuestLocationPhoto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestLocationPhoto::getLocationId, locationId);
        List<QuestLocationPhoto> questLocationPhotos = questLocationPhotoMapper.selectList(queryWrapper);
        LinkedList<String> photoUrls = new LinkedList<>();
        questLocationPhotos.forEach((questLocationPhoto) -> {
            String photoUrl = questLocationPhoto.getUrl();
            photoUrls.add(photoUrl);
        });
        questLocationInfoVO.setPhotoUrls(photoUrls);
        return questLocationInfoVO;
    }
}
