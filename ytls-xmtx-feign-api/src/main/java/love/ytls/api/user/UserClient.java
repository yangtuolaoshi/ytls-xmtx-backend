package love.ytls.api.user;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("user-service")
public interface UserClient {
}
