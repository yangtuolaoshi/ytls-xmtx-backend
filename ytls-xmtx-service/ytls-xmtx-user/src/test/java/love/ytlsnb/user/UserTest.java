package love.ytlsnb.user;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.controller.UserController;
import love.ytlsnb.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author ula
 */
@SpringBootTest
public class UserTest {
    @Autowired
    UserController userController;

    @Test
    public void test(){
        UserQueryDTO userQueryDTO=new UserQueryDTO();
        userQueryDTO.setIdentified(UserConstant.IDENTIFIED);
        userQueryDTO.setGender(UserConstant.DEFAULT_GENDER);
        Result<List<User>> listResult = userController.list(userQueryDTO);
        List<User> list = listResult.getData();
        System.out.println(list);
    }
    @Test
    public void test1(){
        for (int i = 0; i < 31; i++) {
            LocalDate localDate = LocalDate.now().plusDays(i);
            System.out.println(localDate.getDayOfMonth());
        }
        System.out.println("--------------------------------------------------");
        for (int i = 0; i <7; i++) {
            LocalDate localDate = LocalDate.now().plusDays(i);
            System.out.println(localDate.getDayOfWeek().getValue());
        }
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < 31; i++) {
            LocalDate localDate = LocalDate.now().plusDays(i);
            System.out.println(localDate.getDayOfYear());
        }
    }
}
