package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.WorkbookUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import love.ytls.api.quest.QuestClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.QuestConstant;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.AdminHolder;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.user.dto.UserInsertBatchDTO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.SchoolService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/3 14:59
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private QuestClient questClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private AliUtil aliUtil;

    /**
     * 根据传入的用户新增数据传输对象新增用户数据，内部会检查数据合法性
     *
     * @param userInsertDTO 用户新增数据传输对象
     */
    @Override
    public void addUser(UserInsertDTO userInsertDTO) throws Exception {
        // 参数合法性校验
        // 必填参数（手机号）为空
        if (StrUtil.isBlankIfStr(userInsertDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "手机号不能为空");
        }
        // 手机号不合法
        if (!PhoneUtil.isPhone(userInsertDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的手机号");
        }
        // 用户真实照片校验
        if (userInsertDTO.getRealPhoto() != null) {
            aliUtil.faceDetect(userInsertDTO.getRealPhoto());
        }
        if (userInsertDTO.getIdNumber() != null
                && !IdcardUtil.isValidCard(userInsertDTO.getIdNumber())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的身份证号");
        }

        // 为用户添加学校相关属性
        Admin admin = AdminHolder.getAdmin();
        Long schoolId = admin.getSchoolId();
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "当前学校信息为空");
        }
        userInsertDTO.setSchoolId(schoolId);
        userInsertDTO.setSchoolName(school.getSchoolName());
        Result<User> userResult = userClient.addUser(userInsertDTO);
        if (userResult.getCode() != ResultCodes.OK) {
            // 远程调用失败
            throw new BusinessException(userResult.getCode(), userResult.getMsg());
        }
    }

    @Override
    public void addUserBatch(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 后续使用properties修改 TODO
        HashMap<String, String> headerAliasMap = new HashMap<>();
        headerAliasMap.put("学号", "studentId");
        headerAliasMap.put("身份证号", "idNumber");
        headerAliasMap.put("姓名", "name");
        headerAliasMap.put("手机号", "photo");

        reader.setHeaderAlias(headerAliasMap);
        List<UserInsertBatchDTO> userInsertBatchDTOList = reader.readAll(UserInsertBatchDTO.class);
        // 重复性校验
        Set<String> validateUserPhoneSet = new HashSet<>();
        Set<String> validateUserStudentIdSet = new HashSet<>();

        Admin admin = AdminHolder.getAdmin();
        School school = schoolMapper.selectById(admin.getSchoolId());
        List<User> userList=new ArrayList<>();
        for (UserInsertBatchDTO userInsertBatchDTO : userInsertBatchDTOList) {
            String phone = userInsertBatchDTO.getPhone();
            String studentId = userInsertBatchDTO.getStudentId();
            if (validateUserPhoneSet.contains(phone)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复手机号");
            }
            if (validateUserStudentIdSet.contains(studentId)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复学号");
            }
            validateUserPhoneSet.add(phone);
            validateUserStudentIdSet.add(studentId);

            User user=new User();
            UserInfo userInfo=new UserInfo();
            user.setPhone(phone);
            user.setSchoolId(school.getId());
            userInfo.setSchoolName(school.getSchoolName());
            // TODO
        }
    }
}
