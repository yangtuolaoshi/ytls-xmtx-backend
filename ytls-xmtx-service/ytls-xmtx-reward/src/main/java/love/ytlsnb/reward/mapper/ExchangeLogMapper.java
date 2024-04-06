package love.ytlsnb.reward.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.ExchangeLogQueryDTO;
import love.ytlsnb.model.reward.po.ExchangeLog;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/12 14:10
 */
@Mapper
public interface ExchangeLogMapper extends BaseMapper<ExchangeLog> {


    List<ExchangeLogVO> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size);
}
