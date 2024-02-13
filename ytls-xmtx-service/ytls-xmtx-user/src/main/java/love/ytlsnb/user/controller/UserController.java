package love.ytlsnb.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.utils.AliOssUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.service.UserInfoService;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
    private AliOssUtil aliOssUtil;
    @PostMapping("upload")
    public Result<String> upload(MultipartFile multipartFile) {
        log.info("正在上传文件 {} 至阿里云云端", multipartFile);

        //获取上传文件的名字
        String originalFilename = multipartFile.getOriginalFilename();

        //获取随机UUID同时拼接上上传文件的后缀名
        String name = null;
        if (originalFilename != null) {
            name = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        try {
            String fileurl = aliOssUtil.upload(multipartFile.getInputStream(), name);
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
        log.info("用户登录：{}", userLoginDTO);
        String jwt = userService.login(userLoginDTO, request);
        return Result.ok(jwt);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
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
}
