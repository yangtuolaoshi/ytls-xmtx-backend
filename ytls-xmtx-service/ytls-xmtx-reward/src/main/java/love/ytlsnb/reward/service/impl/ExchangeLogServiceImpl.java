package love.ytlsnb.reward.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytls.api.school.SchoolClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.RewardConstant;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.ExchangeLogQueryDTO;
import love.ytlsnb.model.reward.po.ExchangeLog;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.reward.mapper.ExchangeLogMapper;
import love.ytlsnb.reward.mapper.RewardMapper;
import love.ytlsnb.reward.service.ExchangeLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/12 14:19
 */
@Service
public class ExchangeLogServiceImpl extends ServiceImpl<ExchangeLogMapper, ExchangeLog> implements ExchangeLogService {

    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private ExchangeLogMapper exchangeLogMapper;
    @Autowired
    private UserClient userClient;
    @Lazy
    @Autowired
    private SchoolClient schoolClient;



    @Override
    public void addExchangeLog(ExchangeLogDTO exchangeLogDTO) {
        Long rewardId = exchangeLogDTO.getRewardId();
        Reward reward = rewardMapper.selectById(rewardId);
        if (reward == null) {
            throw new RuntimeException("未查询到奖品");
        }
        if (reward.getStock() <= 0) {
            throw new RuntimeException("奖品库存不足！");
        }
        //更新奖品的库存量
        rewardMapper.updateStock(rewardId, reward.getStock() - 1);

        ExchangeLog exchangeLog = new ExchangeLog();
        exchangeLog.setCreateTime(LocalDateTime.now());
        BeanUtil.copyProperties(exchangeLogDTO, exchangeLog);

        exchangeLogMapper.insert(exchangeLog);

    }

    @Override
    public PageResult<List<ExchangeLogVO>> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size) {
        LambdaQueryWrapper<ExchangeLog> exchangeLogQueryWrapper = new LambdaQueryWrapper<>();

        //获取用户id
        Long userId = exchangeLogQueryDTO.getUserId();
        //获取奖品id
        Long rewardId = exchangeLogQueryDTO.getRewardId();

//        User user = UserHolder.getUser();
//        Long userId = user.getId();
        //查询总数
        if(userId!=null){
            exchangeLogQueryWrapper.eq(ExchangeLog::getUserId,userId);
        }

        if (rewardId!=null){
            exchangeLogQueryWrapper.eq(ExchangeLog::getRewardId,rewardId);
        }

        //List<ExchangeLogVO> exchangeLogVOS = new ArrayList<>();
        Long total = exchangeLogMapper.selectCount(exchangeLogQueryWrapper);

        //分页查询
        List<ExchangeLogVO> exchangeLogVOS = exchangeLogMapper.getPageByCondition(exchangeLogQueryDTO, (page - 1) * size, size);
        PageResult<List<ExchangeLogVO>> pageResult = new PageResult<>(page, exchangeLogVOS.size(), total);
        pageResult.setData(exchangeLogVOS);
        return pageResult;

    }

    @Override
    public ExchangeLogVO selectById(Long id) {
        ExchangeLog exchangeLog = exchangeLogMapper.selectById(id);
        Long userId = exchangeLog.getUserId();
        //远程调用用户端与学校端
        User user = userClient.getUserById(userId).getData();
        Clazz clazz = schoolClient.getClazzById(user.getClazzId()).getData();
        Dept dept = schoolClient.selectByDeptId(user.getDeptId()).getData();
        ExchangeLogVO exchangeLogVO = new ExchangeLogVO();
        //添加返回到前端的数据，封装到VO内
        BeanUtil.copyProperties(exchangeLog,exchangeLogVO);
        exchangeLogVO.setExUserName(user.getNickname());
        exchangeLogVO.setClazzName(clazz.getClazzName());
        exchangeLogVO.setDeptName(dept.getDeptName());
        exchangeLogVO.setRewardTitle(new Reward().getTitle());
        // todo 兑换时间的添加
        return null;
    }


}
