package love.ytlsnb.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.user.dto.*;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.vo.UserVO;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * 用户基本信息控制器层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SchoolClient schoolClient;

    /**
     * 鼠鼠用来调试的接口
     */
    @GetMapping("test")
    public void test() {
        log.info("user-test");
        schoolClient.getColadminById(1L);
    }

    @PostMapping
    public Result addUser(@RequestBody UserInsertDTO userInsertDTO) throws Exception {
        log.info("新增用户:{}", userInsertDTO);
        userService.addUser(userInsertDTO);
        return Result.ok();
    }

    @PostMapping("/batch")
    public Result addUserBatch(@RequestBody List<UserInsertBatchDTO> userInsertBatchDTOList) throws IOException {
        log.info("通过Excel批量新增用户数据:{}", userInsertBatchDTOList);
        userService.addUserBatch(userInsertBatchDTOList);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable Long id) {
        log.info("根据用户ID删除用户:{}", id);
        userService.deleteUserById(id);
        return Result.ok();
    }

    /**
     * 用户的修改接口
     *
     * @param userUpdateDTO 用户修改数据传输对象
     * @return
     */
    @PutMapping
    public Result updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("修改用户:{}", userUpdateDTO);
        userService.update(userUpdateDTO);
        return Result.ok();
    }

    /**
     * 学校管理员修改用户数据的接口
     *
     * @param userInsertDTO 用户修改数据传输对象（二者参数一致，这里采用同一个模型）
     * @param id            待修改的用户ID
     * @return
     * @throws Exception 修改失败的异常
     */
    @PutMapping("/{id}")
    public Result updateUserById(@RequestBody UserInsertDTO userInsertDTO, @PathVariable Long id) throws Exception {
        log.info("修改用户:{}", userInsertDTO);
        userService.updateUserById(userInsertDTO, id);
        return Result.ok();
    }

    @PutMapping("/password")
    public Result updatePassword(@RequestBody UserUpdatePasswordDTO userUpdatePasswordDTO) {
        log.info("用户重置密码:{}", userUpdatePasswordDTO);
        userService.updatePassword(userUpdatePasswordDTO);
        return Result.ok();
    }

    @GetMapping
    public Result<User> getUser() {
        log.info("用户端查询用户");
        User user = userService.getUser();
        return Result.ok(user);
    }
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        log.info("根据用户ID查询用户:{}", id);
        User user = userService.getById(id);
        user.setPassword(UserConstant.INSENSITIVE_PASSWORD);
        return Result.ok(user);
    }

    @GetMapping("/detail/{id}")
    public Result<UserVO> getUserVOById(@PathVariable Long id) {
        log.info("根据用户ID查询用户:{}", id);
        UserVO userVO = userService.getUserVOById(id);
        return Result.ok(userVO);
    }

    @GetMapping("/listByConditions")
    public PageResult<List<UserVO>> listByConditions(UserQueryDTO userQueryDTO) {
        log.info("查询用户，userQueryDTO:{}", userQueryDTO);
        List<UserVO> userVOList = userService.listByConditions(userQueryDTO);
        return new PageResult<>(userQueryDTO.getCurrentPage(),
                userQueryDTO.getPageSize(),
                userVOList,
                (long) userVOList.size());
    }

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("正在上传文件 {} 至阿里云云端", file);
        return Result.ok(userService.upload(file));
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

    @GetMapping("/sign/list")
    public Result<Boolean[]> listSign() {
        log.info("获取本月所有签到数据");
        Boolean[] signList = userService.listSign();
        return Result.ok(signList);
    }

    /**
     * 向指定手机号发送验证码
     *
     * @param phone 路径参数，指定的手机号
     * @return 发送成功返回的结果
     * @throws Exception 发送失败的异常
     */
    @GetMapping("/code/{phone}")
    public Result sendShortMessage(@PathVariable String phone) throws Exception {
        log.info("发送验证码:{}", phone);
        userService.sendShortMessage(phone);
        return Result.ok();
    }

    /**
     * @param idCard 代表身份证的OSS存储路径
     * @return
     */
    @PostMapping("/idCard")
    public Result uploadIdCard(@RequestBody String idCard) throws Exception {
        log.info("上传身份证:{}", idCard);
        userService.uploadIdCard(idCard);
        return Result.ok();
    }

    /**
     * @param admissionLetter 代表用户录取通知书的OSS存储路径
     * @return
     */
    @PostMapping("/admissionLetter")
    public Result uploadAdmissionLetter(@RequestBody String admissionLetter) throws Exception {
        log.info("上传录取通知书:{}", admissionLetter);
        userService.uploadAdmissionLetter(admissionLetter);
        return Result.ok();
    }

    /**
     * @param realPhoto 用户上传的真实照片
     * @return
     */
    @PostMapping("/realPhoto")
    public Result uploadRealPhoto(@RequestBody String realPhoto) throws Exception {
        log.info("上传真实照片:{}", realPhoto);
        userService.uploadRealPhoto(realPhoto);
        return Result.ok();
    }
}