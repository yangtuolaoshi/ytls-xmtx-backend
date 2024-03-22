package love.ytlsnb.reward.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.properties.AliOssProperties;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.ExchangeLogQueryDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.ExchangeLog;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import love.ytlsnb.reward.service.ExchangeLogService;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/12 14:39
 */
@RestController
@Slf4j
@RequestMapping("/exchange")
public class ExchangeLogController {

    @Autowired
    private ExchangeLogService exchangeLogService;

    @PostMapping("/add")
    public Result addExchangeLog(ExchangeLogDTO exchangeLogDTO){
        log.info("根据兑换情况添加的奖品情况{}",exchangeLogDTO);
        exchangeLogService.addExchangeLog(exchangeLogDTO);
        return Result.ok();
    }

    @GetMapping("/page")
    public PageResult<List<ExchangeLogVO>> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size){
        log.info("分页查询奖品兑换情况：{}",exchangeLogQueryDTO);
        return exchangeLogService.getPageByCondition(exchangeLogQueryDTO,page,size);
    }


}

