package love.ytlsnb.school.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.school.mapper.QuestMapper;
import love.ytlsnb.school.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/2/7 16:32
 */
@Slf4j
@Service
public class QuestServiceImpl extends ServiceImpl<QuestMapper, Quest> implements QuestService {
    @Autowired
    private QuestMapper questMapper;
}
