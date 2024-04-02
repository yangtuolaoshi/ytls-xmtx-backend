package love.ytlsnb.reward.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.RewardConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.PhotoProperties;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardPhotoDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.reward.mapper.RewardMapper;
import love.ytlsnb.reward.mapper.RewardPhotoMapper;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ula
 * @date 2024/2/6 14:25
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {
    @Autowired
    private RewardMapper rewardMapper;
    @Autowired
    private RewardPhotoMapper rewardPhotoMapper;
    @Autowired
    private PhotoProperties photoProperties;
    @Autowired
    private AliUtil aliUtil;


    /**
     * 新增奖品
     *
     * @param rewardDTO
     */
    @Override
    @Transactional
    public void add(RewardDTO rewardDTO) {
        // 校验参数
        if (BeanUtil.hasNullField(rewardDTO)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "传入参数不完整");
        }
        //判断新建奖品名是否重复
        String title = rewardDTO.getTitle();
        if (title != null) {
            Reward selectOne = rewardMapper.selectOne(new LambdaQueryWrapper<Reward>()
                    .eq(Reward::getTitle, title));
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "已存在该名称的商品");
            }
        }
        Reward reward = new Reward();
        BeanUtils.copyProperties(rewardDTO, reward);
        reward.setCreateTime(LocalDateTime.now());
        reward.setStatus(RewardConstant.ENABLED);
        Long rewardId = reward.getId();

        List<RewardPhoto> photos = rewardDTO.getRewardPhotos();
        RewardPhoto rewardCover = photos.get(0);
        String cover = rewardCover.getPhoto();
        //String cover = rewardPhotoMapper.selectCover(rewardId);
        rewardPhotoMapper.insertCover(cover);

        if (photos != null || photos.size() > 0) {
            photos.forEach(rewardPhoto -> {
                rewardPhoto.setRewardId(rewardId);
            });
            rewardPhotoMapper.insert(photos);
        }
        rewardMapper.insert(reward);

    }

    /**
     * 分页查询
     *
     * @param rewardQueryDTO
     * @return
     */
    @Override
    @Transactional
    public PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size) {
        LambdaQueryWrapper<Reward> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<RewardPhoto> photoQueryWrapper = new LambdaQueryWrapper<>();
        String title = rewardQueryDTO.getTitle();
        String rewardId = rewardQueryDTO.getId();
        if (title != null) {
            queryWrapper.likeRight(Reward::getTitle, title);// 百分号只在右侧，防止索引失效
        }

        Integer cost = rewardQueryDTO.getCost();
        if (cost != null && cost > 0) {
            //花费查询
            queryWrapper.le(Reward::getCost, cost);
        }

        Byte status = rewardQueryDTO.getStatus();
        if (status != null) {
            //状态查询
            queryWrapper.eq(Reward::getStatus, status);
        }
        photoQueryWrapper.eq(RewardPhoto::getRewardId, rewardId);

        List<RewardVO> rewardVOS = new ArrayList();
        Long total = rewardMapper.selectCount(queryWrapper);
        List<Reward> rewards = rewardMapper.selectList(queryWrapper);

        BeanUtils.copyProperties(rewards, rewardVOS);
        RewardVO rewardVO = new RewardVO();
        List<RewardPhoto> rewardPhotos = rewardPhotoMapper.getByRewardId(rewardId);
        rewardVO.setRewardPhotos(rewardPhotos);


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
     *
     * @param rewardUpdateDTO
     * @return
     */
    @Override
    @Transactional
    public Boolean update(RewardDTO rewardUpdateDTO) {
        Reward reward = new Reward();
        //RewardPhoto rewardPhoto = new RewardPhoto();
        //rewardPhoto.setRewardId(rewardUpdateDTO.getId());
        BeanUtils.copyProperties(rewardUpdateDTO, reward);
        reward.setUpdateTime(LocalDateTime.now());

        List<RewardPhoto> rewardPhotos = rewardUpdateDTO.getRewardPhotos();
        if (rewardPhotos != null || rewardPhotos.size() > 0){
            rewardPhotos.forEach(rewardPhoto -> {
                rewardPhoto.setRewardId(rewardUpdateDTO.getId());
            });
        }
            rewardPhotoMapper.insert(rewardPhotos);
            return rewardMapper.updateById(reward) > 0;
    }

    /**
     * 根据id删除奖品
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteWithPhotoById(Long id) {
        Reward reward = rewardMapper.selectById(id);
//        if (reward == null) {
//            return false;
//        }
        if (reward.getStatus().equals(RewardConstant.ENABLED)) {
            throw new RuntimeException(RewardConstant.REWARD_ON_SALE);
        }

        //根据奖品id删除图片数据
        rewardPhotoMapper.deleteById(id);
        //根据奖品id删除奖品数据
        rewardMapper.deleteById(id);
    }

    @Override
    public List<Reward> selectBySchoolId(Long schoolId) {
        return rewardMapper.selectList(new LambdaQueryWrapper<Reward>()
                .eq(Reward::getSchoolId, schoolId));

    }

    /**
     * 上传不必要的图片时进行删除
     * @param rewardPhotoDTO
     */
    @Override
    public void deleteByPhoto(RewardPhotoDTO rewardPhotoDTO) {
        Long photoId = rewardPhotoDTO.getId();
        rewardMapper.deleteByPhotoId(photoId);
    }

    @Override
    public String upload(MultipartFile file) {
        long size = file.getSize();
        if (size > Integer.MAX_VALUE) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "上传文件过大");
        }
        //获取上传文件的名字
        String originalFilename = file.getOriginalFilename();
        // 获取创传文件的后缀名
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        if (!photoProperties.getSupportedTypes().contains(suffix)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前图片类型不支持");
        }
        try {
            // 获取出入流
            InputStream inputStream = file.getInputStream();
            //获取随机UUID同时拼接上上传文件的后缀名
            String name = UUID.randomUUID() + suffix;
            return aliUtil.upload(inputStream, (int) size, name);
        } catch (IOException e) {
            log.error("文件上传失败 ->", e);
            throw new BusinessException(ResultCodes.SERVER_ERROR, "文件上传异常");
        }

    }


}

