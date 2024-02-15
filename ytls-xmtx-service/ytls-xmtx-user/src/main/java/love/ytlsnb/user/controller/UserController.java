package love.ytlsnb.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.PhotoProperties;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.service.UserInfoService;
import love.ytlsnb.user.service.UserService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 用户基本信息控制器层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AliUtil aliUtil;
    @Autowired
    private PhotoProperties photoProperties;

    @PostMapping("upload")
    public Result<String> upload(MultipartFile multipartFile) {
        log.info("正在上传文件 {} 至阿里云云端", multipartFile);

        //获取上传文件的名字
        String originalFilename = multipartFile.getOriginalFilename();
        // 获取创传文件的后缀名
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        if (!photoProperties.getSupportedTypes().contains(suffix)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前图片类型不支持");
        }
        try {
            // 获取出入流
            InputStream inputStream = multipartFile.getInputStream();
            // 创建一个临时输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 判断文件大小
            if (multipartFile.getSize() > photoProperties.getMaxSize() * 1024 * 1024) {
                // 文件大小过大，进行压缩
                // 计算压缩比：注意，经过赋值这里计算的压缩比之后还是并不保证文件大小为maxSize，
                // 因为这里计算采用的线性函数计算，而实际的压缩质量与outputQuality并非线性关系，但多次测试下发现能够保证最终大小小于maxSize
                float ratio = photoProperties.getMaxSize() * 1024 * 1024 / multipartFile.getSize();
                Thumbnails.of(inputStream)
                        .size(4096, 4096)
                        .outputQuality(ratio)
                        .toOutputStream(outputStream);
                // 更新输入流
                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            } else {
                // 控制文件分辨率
                Thumbnails.of(inputStream)
                        .size(4096, 4096)
                        .toOutputStream(outputStream);
                // 更新输入流
                inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            }
            //获取随机UUID同时拼接上上传文件的后缀名
            String name = UUID.randomUUID() + suffix;
            String fileurl = aliUtil.upload(inputStream, name);
            return Result.ok(fileurl);
        } catch (IOException e) {
            log.error("文件上传失败 ->", e);
            return Result.fail(ResultCodes.SERVER_ERROR, "文件上传异常");
        }
    }

    @GetMapping("/list")
    public Result<List<User>> list(UserQueryDTO userQueryDTO) {
        log.info("查询用户，userQueryDTO:{}", userQueryDTO);
        return Result.ok(userService.list(userQueryDTO));
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("用户登录:{}", userLoginDTO);
        String jwt = userService.login(userLoginDTO, request);
        return Result.ok(jwt);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册:{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.ok();
    }

    @GetMapping("/user/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        log.info("查询用户，id:{}", id);
        return Result.ok(userService.getById(id));
    }

    @GetMapping("/userInfo/{id}")
    public Result<UserInfo> getUserInfoById(@PathVariable Long id) {
        log.info("查询用户，id:{}", id);
        return Result.ok(userInfoService.getById(id));
    }

    /**
     * 用户签到接口，当用户不能签到时理应不能访问该接口
     *
     * @return 用户签到的结果
     */
    @PostMapping("/sign")
    public Result sign() {
        if (userService.sign()) {
            return Result.ok();
        } else {
            return Result.fail(ResultCodes.FORBIDDEN, "您今日已签到");
        }
    }

    /**
     * 获取用户今日的签到状态
     *
     * @return 用户今日的签到状态:true 已签到   false 未签到
     */
    @GetMapping("/sign")
    public Result<Boolean> getSignStatus() {
        return Result.ok(userService.isSigned());
    }

    /**
     * 向指定手机号发送验证码
     *
     * @param phone 路径参数，指定的手机号
     * @return 发送成功返回的结果
     * @throws Exception 发送失败的异常
     */
    @GetMapping("/code/{phone}")
    private Result sendShortMessage(@PathVariable String phone) throws Exception {
        log.info("发送验证码:{}", phone);
        userService.sendShortMessage(phone);
        return Result.ok();
    }
}
