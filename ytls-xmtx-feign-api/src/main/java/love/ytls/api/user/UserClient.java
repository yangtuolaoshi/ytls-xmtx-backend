package love.ytls.api.user;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/{id}")
    Result<User> getById(@PathVariable Long id);

    @GetMapping("list")
    Result<List<User>> list(UserQueryDTO userQueryDTO);
}
