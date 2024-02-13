package love.ytlsnb.common.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.utils.AliOssUtil;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author ula
 * @date 2024/2/13 10:32
 */
@Slf4j
@RestController
@RequestMapping("common")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;


}