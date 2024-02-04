package love.ytlsnb.school.task;

import love.ytlsnb.common.utils.SchoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/4 15:19
 */
@Component
public class SchoolTask {
    @Autowired
    SchoolUtil schoolUtil;

    /**
     * 每月一号凌晨执行
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateRankingListTotal(){
        schoolUtil.updateRankingListTotal();
    }
    /**
     * 每周一凌晨执行
     */
    @Scheduled(cron = "0 0 0 ? * 1")
    public void updateRankingListWeekly(){
        schoolUtil.updateRankingListWeekly();
    }
    /**
     * 每天凌晨执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateRankingListDayly(){
        schoolUtil.updateRankingListDayly();
    }
}
