package love.ytls.api.user;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/user/user/{id}")
    Result<User> getById(@PathVariable Long id);

    @PostMapping("/api/user/user")
    Result<User> addUser(@RequestBody UserInsertDTO userInsertDTO);

    @GetMapping("/api/user/list")
    Result<List<User>> list(UserQueryDTO userQueryDTO);
}
