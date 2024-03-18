package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.coladmin.dto.ColadminLoginDTO;
import love.ytlsnb.model.coladmin.dto.ColadminRegisterDTO;
import love.ytlsnb.model.coladmin.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.school.service.ColadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/2/28 20:07
 */
@Slf4j
@RestController
@RequestMapping("/coladmin")
public class ColadminController {
    @Autowired
    private ColadminService coladminService;

    @GetMapping("/{coladminId}")
    public Result<Coladmin> getColadminById(@PathVariable Long coladminId) {
        log.info("根据ID获取Coladmin的信息");
        Coladmin coladmin = coladminService.getById(coladminId);
        return Result.ok(coladmin);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody ColadminLoginDTO coladminLoginDTO, HttpServletRequest request) {
        log.info("管理员登录:{}", coladminLoginDTO);
        String jwt = coladminService.login(coladminLoginDTO, request);
        return Result.ok(jwt);
    }

    @PostMapping("/register")
    public Result register(@RequestBody ColadminRegisterDTO coladminRegisterDTO) {
        log.info("管理员注册:{}", coladminRegisterDTO);
        coladminService.register(coladminRegisterDTO);
        return Result.ok();
    }

    @GetMapping("/insensitiveColadmin/{id}")
    public Result<Coladmin> getInsensitiveColadminById(@PathVariable Long id) {
        log.info("获取不敏感的学校管理员相关信息");
        Coladmin coladmin = coladminService.selectInsensitiveAdminById(id);
        return Result.ok(coladmin);
    }

    //-------------------------User-------------------------
    @PostMapping("/user")
    public Result addUser(@RequestBody UserInsertDTO userInsertDTO) {
        log.info("添加用户:{}", userInsertDTO);
        coladminService.addUser(userInsertDTO);
        return Result.ok();
    }

    @PostMapping("/user/batch")
    public Result addUserBatch(MultipartFile file) throws IOException {
        log.info("批量添加用户");
        coladminService.addUserBatch(file);
        return Result.ok();
    }

    @DeleteMapping("/user/{id}")
    public Result deleteUserById(@PathVariable Long id) {
        log.info("根据用户ID删除用户:{}", id);
        coladminService.deleteUserById(id);
        return Result.ok();
    }

    @PutMapping("/user/{id}")
    public Result updateUserById(@RequestBody UserInsertDTO userInsertDTO, @PathVariable Long id) {
        log.info("修改用户信息:{},{}", userInsertDTO, id);
        coladminService.updateUserById(userInsertDTO, id);
        return Result.ok();
    }

    @GetMapping("/user/detail/{id}")
    public Result<UserVO> getUserVOById(@PathVariable Long id) {
        log.info("根据用户ID查询用户:{}", id);
        UserVO userVO = coladminService.getUserVOById(id);
        return Result.ok(userVO);
    }

    @GetMapping("/user/listByConditions")
    public PageResult<List<UserVO>> listUserByConditions(UserQueryDTO userQueryDTO) {
        log.info("条件查询用户:{}", userQueryDTO);
        List<UserVO> userVOList = coladminService.listUserByConditions(userQueryDTO);
        return new PageResult<>(userQueryDTO.getCurrentPage(),
                userQueryDTO.getPageSize(),
                userVOList,
                (long) userVOList.size());
    }
}
