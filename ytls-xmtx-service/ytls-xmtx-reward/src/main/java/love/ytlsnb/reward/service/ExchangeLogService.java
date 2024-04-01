package love.ytlsnb.reward.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.ExchangeLogQueryDTO;
import love.ytlsnb.model.reward.po.ExchangeLog;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/12 14:19
 */
public interface ExchangeLogService extends IService<ExchangeLog> {
    void addExchangeLog(ExchangeLogDTO exchangeLogDTO);

    PageResult<List<ExchangeLogVO>> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size);


    ExchangeLogVO selectById(Long id);
}
