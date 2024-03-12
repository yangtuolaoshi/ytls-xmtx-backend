package love.ytlsnb.user;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.vo.UserVO;
import love.ytlsnb.user.controller.UserController;
import love.ytlsnb.user.service.UserService;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author ula
 */
//@SpringBootTest
public class UserTest {
//    @Autowired
//    UserController userController;
//
//    @Autowired
//    AliUtil aliUtil;

    @Test
    public void test() throws Exception {
        User user = new User();
        user.setPhone("123");
        UserVO userVO = new UserVO();
        userVO.setPhone("456");
        System.out.println(user);
        BeanUtil.copyProperties(userVO, user);
        System.out.println(user);
    }
}