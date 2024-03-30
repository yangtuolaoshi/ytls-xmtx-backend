package love.ytlsnb.quest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;
import love.ytlsnb.quest.mapper.QuestLocationPhotoMapper;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务地点图片业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/5
 */
@Service
public class QuestLocationPhotoServiceImpl implements QuestLocationPhotoService {
    @Autowired
    private QuestLocationPhotoMapper questLocationPhotoMapper;

    @Transactional
    @Override
    public Boolean addBatchByUrls(List<String> urls, Long locationId) {
        // TODO 消除for循环，实现真正的批量插入
        LinkedList<QuestLocationPhoto> photos = new LinkedList<>();
        if (urls == null || urls.size() == 0) {
            return false;
        }
        for (String url : urls) {
            QuestLocationPhoto questLocationPhoto = new QuestLocationPhoto();
            questLocationPhoto.setLocationId(locationId);
            questLocationPhoto.setUrl(url);
            questLocationPhoto.setCreateTime(LocalDateTime.now());
            questLocationPhoto.setIsDeleted(0);
            photos.add(questLocationPhoto);
        }
        for (QuestLocationPhoto photo : photos) {
            questLocationPhotoMapper.insert(photo);
        }
        return true;
    }

    @Transactional
    @Override
    public Boolean deleteByLocationId(Long locationId) {
        LambdaQueryWrapper<QuestLocationPhoto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestLocationPhoto::getLocationId, locationId);
        return questLocationPhotoMapper.delete(queryWrapper) > 0;
    }

    @Override
    public Boolean deleteByLocationIds(List<Long> locationIds) {
        LambdaQueryWrapper<QuestLocationPhoto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuestLocationPhoto::getLocationId, locationIds);
        return questLocationPhotoMapper.delete(queryWrapper) > 0;
    }
}
