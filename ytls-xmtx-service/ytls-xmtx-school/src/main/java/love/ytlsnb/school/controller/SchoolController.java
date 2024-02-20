package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.*;
import love.ytlsnb.model.school.po.Location;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.vo.LocationVO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.school.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 14:56
 */
@Slf4j
@RestController
@RequestMapping("school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("list")
    public Result<List<School>> list() {
        List<School> list = schoolService.list();
        return Result.ok(list);
    }
}