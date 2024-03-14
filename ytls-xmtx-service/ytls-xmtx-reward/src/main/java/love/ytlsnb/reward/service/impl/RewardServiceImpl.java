package love.ytlsnb.reward.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.RewardConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.reward.mapper.RewardMapper;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:25
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {
    @Autowired
    private RewardMapper rewardMapper;

    /**
     * 新增奖品
     * @param rewardDTO
     */
    @Override
    @Transactional
    public void add(RewardDTO rewardDTO) {
        // 校验参数
        if (BeanUtil.hasNullField(rewardDTO)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "传入参数不完整");
        }
        //TODO 判断新建奖品名是否重复
        String title = rewardDTO.getTitle();
        if(title != null) {
            Reward selectOne = rewardMapper.selectOne(new LambdaQueryWrapper<Reward>()
                    .eq(Reward::getTitle,title));
            if(selectOne != null){
                throw new BusinessException(ResultCodes.FORBIDDEN,"已存在该名称的商品");
            }
        }
        Reward reward = new Reward();
        BeanUtils.copyProperties(rewardDTO,reward);
        reward.setCreateTime(LocalDateTime.now());
        reward.setUpdateTime(null);
        reward.setStatus(RewardConstant.ENABLED);
        reward.setDeleted(null);
        rewardMapper.insert(reward);
    }

    /**
     * 分页查询
     *
     * @param rewardQueryDTO
     * @return
     */
    @Override
    public PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO,int page,int size) {
        LambdaQueryWrapper<Reward> queryWrapper = new LambdaQueryWrapper<>();
        String title = rewardQueryDTO.getTitle();
        if (title != null) {
            queryWrapper.likeRight(Reward::getTitle, title);// 百分号只在右侧，防止索引失效
        }

        Integer cost = rewardQueryDTO.getCost();
        if(cost != null && cost > 0){
            //花费查询
            queryWrapper.le(Reward::getCost,cost);
        }

        Byte status = rewardQueryDTO.getStatus();
        if(status != null){
            //状态查询
            queryWrapper.eq(Reward::getStatus,status);
        }

        List<RewardVO> rewardVOS = new ArrayList();
        Long total = rewardMapper.selectCount(queryWrapper);
        List<Reward> rewards = rewardMapper.selectList(queryWrapper);

        BeanUtils.copyProperties(rewards,rewardVOS);



        // 分页查询
        if (title != null) {
            rewardQueryDTO.setTitle(title + "%");
        }
//        List<RewardVO> rewardVOS = rewardMapper.getPageByCondition(rewardQueryDTO, (page - 1) * size, size);

        PageResult<List<RewardVO>> pageResult = new PageResult<>(page, rewardVOS.size(), total);
        pageResult.setData(rewardVOS);
        return pageResult;

    }

    /**
     * 修改奖品
     * @param rewardUpdateDTO
     * @return
     */
    @Override
    public Boolean update(RewardDTO rewardUpdateDTO) {
        Reward reward = new Reward();
        BeanUtils.copyProperties(rewardUpdateDTO,reward);
        reward.setUpdateTime(LocalDateTime.now());
        reward.setCreateTime(null);
        reward.setDeleted(null);

        return rewardMapper.updateById(reward)>0;
    }

    /**
     * 根据id删除奖品
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
            Reward reward = rewardMapper.selectById(id);
            if(reward==null){
                return false;
            }
            if(reward.getStatus()==RewardConstant.ENABLED){
                throw new RuntimeException(RewardConstant.REWARD_ON_SALE);
        }
        return rewardMapper.deleteById(id)>0;
    }


}
