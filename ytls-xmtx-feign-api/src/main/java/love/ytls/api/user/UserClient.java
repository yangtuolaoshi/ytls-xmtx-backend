package love.ytls.api.user;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.*;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.model.user.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping("/api/user")
    Result addUser(@RequestBody UserInsertDTO userInsertDTO);

    @PostMapping("/api/user/batch")
    Result addUserBatch(@RequestBody List<UserInsertBatchDTO> userInsertBatchDTOList);

    @DeleteMapping("/api/user/{id}")
    Result deleteUserById(@PathVariable Long id);

    @PutMapping("/api/user/{id}")
    Result updateUserById(@RequestBody UserInsertDTO userInsertDTO, @PathVariable Long id);

    @GetMapping("/api/user/{id}")
    Result<User> getUserById(@PathVariable Long id);

    @GetMapping("/api/user/detail/{id}")
    Result<UserVO> getUserVOById(@PathVariable Long id);

    @GetMapping("/api/user/list")
    public Result<List<User>> list();

    @GetMapping("/api/user/listByConditions")
    PageResult<List<UserVO>> listByConditions(@SpringQueryMap UserQueryDTO userQueryDTO);

    @PostMapping("/api/user/addPoint")
    Result<Boolean> addPoint(@RequestParam int reward);
    @PostMapping("/api/user/exchangeReward/{rewardId}")
    Result exchangeReward(@PathVariable Long rewardId);

    @GetMapping("/api/user/list/{total}/{index}")
    Result<List<User>> listBySharding(@PathVariable Integer total, @PathVariable Integer index);
}
