package love.ytlsnb.reward.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytls.api.school.SchoolClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.ExchangeLogQueryDTO;
import love.ytlsnb.model.reward.po.ExchangeLog;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.reward.mapper.ExchangeLogMapper;
import love.ytlsnb.reward.mapper.RewardMapper;
import love.ytlsnb.reward.service.ExchangeLogService;
import love.ytlsnb.school.mapper.DeptMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        LambdaQueryWrapper<Reward> rewardQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Dept> deptQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<ExchangeLog> exchangeLogQueryWrapper = new LambdaQueryWrapper<>();

        Long userId = exchangeLogQueryDTO.getUserId();
        Long rewardId = exchangeLogQueryDTO.getRewardId();
        if (rewardId != null) {
            rewardQueryWrapper.likeRight(Reward::getTitle, exchangeLogQueryDTO.getRewardTitle());// 百分号只在右侧，防止索引失效
        }


        Byte status = exchangeLogQueryDTO.getStatus();
        if (status != null) {
            //状态查询
            rewardQueryWrapper.eq(Reward::getStatus, status);
        }

        String deptName = exchangeLogQueryDTO.getDeptName();

        if (deptName != null) {
            deptQueryWrapper.eq(Dept::getDeptName, deptName);
        }

        User user = userClient.getUserById(userId).getData();

        Long clazzId = user.getClazzId();
        Long deptId = user.getDeptId();
        Long schoolId = exchangeLogQueryDTO.getSchoolId();
        schoolClient.listClazzBySchoolId(schoolId);
        schoolClient.listDeptBySchoolId(schoolId);
        List<ExchangeLogVO> exchangeLogVOS = new ArrayList();

        Long total = exchangeLogMapper.selectCount(exchangeLogQueryWrapper);
        //List<ExchangeLog> rewards = rewardMapper.selectList(queryWrapper);
        List<ExchangeLog> exchangeLogs = exchangeLogMapper.selectList(exchangeLogQueryWrapper);
        BeanUtils.copyProperties(exchangeLogs, exchangeLogVOS);
        ExchangeLogVO rewardVO = new ExchangeLogVO();


        //分页查询
        PageResult<List<ExchangeLogVO>> pageResult = new PageResult<>(page, exchangeLogVOS.size(), total);
        pageResult.setData(exchangeLogVOS);
        return pageResult;

    }
}
