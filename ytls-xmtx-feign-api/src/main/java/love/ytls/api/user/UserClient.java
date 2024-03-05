package love.ytls.api.user;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/user/{id}")
    Result<User> getById(@PathVariable Long id);

//    @PostMapping("/api/user")
//    Result<User> addUser(@RequestBody UserInsertDTO userInsertDTO);

//    @PostMapping("/api/user/saveUserAndUserInfoBatch")
//    Result saveUserAndUserInfoBatch(List<User> userList, List<UserInfo> userInfoList);
//
//    @GetMapping("/api/user/list")
//    Result<List<User>> list(UserQueryDTO userQueryDTO);
}
