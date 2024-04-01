package love.ytlsnb.reward.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.properties.AliOssProperties;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.common.utils.OSSUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.reward.mapper.RewardPhotoMapper;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author QiaoQiao
 * @date 2024/3/20 16:49
 */
@RestController
@Slf4j
@RequestMapping("/reward")
public class UploadPhotoController {
    @Autowired
    private RewardService rewardService;

    @PostMapping("/uploadPhotos")
    public Result<String> uploadPhotos(MultipartFile file) {
        log.info("上传奖品图片{}到阿里云云端:", file);
        return Result.ok(rewardService.upload(file));
    }
}
