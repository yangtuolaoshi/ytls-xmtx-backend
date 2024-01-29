package love.ytls.api.user;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id);
}
