package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ula
 * @date 2024/2/3 14:57
 */
public interface SchoolService extends IService<School> {

    void addUser(UserInsertDTO userInsertDTO) throws Exception;

    void addUserBatch(MultipartFile multipartFile) throws IOException;
}
