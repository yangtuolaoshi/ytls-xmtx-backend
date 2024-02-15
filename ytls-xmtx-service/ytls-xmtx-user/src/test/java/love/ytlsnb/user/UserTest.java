package love.ytlsnb.user;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
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
@SpringBootTest
public class UserTest {
    @Autowired
    UserController userController;

    @Autowired
    AliUtil aliUtil;

    @Test
    public void test() throws Exception {
        aliUtil.faceCompare("https://xmtx-wwxula.oss-cn-nanjing.aliyuncs.com/635c70e2-4693-445e-a9aa-ae1b0eb9fbd7.jpg",
                "https://xmtx-wwxula.oss-cn-nanjing.aliyuncs.com/68921bc6-d468-48c0-b601-5c3f2bdcb604.jpg");
    }
}