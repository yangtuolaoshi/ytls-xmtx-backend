package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.LogOperation;
import love.ytlsnb.model.common.Operator;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.vo.UserVO;
import love.ytlsnb.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/15 16:43
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public Result addUser(@RequestBody UserInsertDTO userInsertDTO) {
        log.info("添加用户:{}", userInsertDTO);
        userService.addUser(userInsertDTO);
        return Result.ok();
    }

    @PostMapping("/batch")
    public Result addUserBatch(MultipartFile file) throws IOException {
        log.info("批量添加用户");
        userService.addUserBatch(file);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable Long id) {
        log.info("根据用户ID删除用户:{}", id);
        userService.deleteUserById(id);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result updateUserById(@RequestBody UserInsertDTO userInsertDTO, @PathVariable Long id) {
        log.info("修改用户信息:{},{}", userInsertDTO, id);
        userService.updateUserById(userInsertDTO, id);
        return Result.ok();
    }

    @LogOperation(Operator.COLADMIN)
    @GetMapping("/detail/{id}")
    public Result<UserVO> getUserVOById(@PathVariable Long id) {
        log.info("根据用户ID查询用户:{}", id);
        UserVO userVO = userService.getUserVOById(id);
        return Result.ok(userVO);
    }

    @GetMapping("/listByConditions")
    public PageResult<List<UserVO>> listUserByConditions(UserQueryDTO userQueryDTO) {
        log.info("条件查询用户:{}", userQueryDTO);
        List<UserVO> userVOList = userService.listUserByConditions(userQueryDTO);
        return new PageResult<>(userQueryDTO.getCurrentPage(),
                userQueryDTO.getPageSize(),
                userVOList,
                (long) userVOList.size());
    }
}
