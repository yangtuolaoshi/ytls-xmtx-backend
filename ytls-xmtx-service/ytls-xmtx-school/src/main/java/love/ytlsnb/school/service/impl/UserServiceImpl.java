package love.ytlsnb.school.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.user.dto.UserInsertBatchDTO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.vo.UserVO;
import love.ytlsnb.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author ula
 * @date 2024/3/15 16:47
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserClient userClient;
    @Override
    public void addUserBatch(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        Map<String, String> headerAliasMap = Map.of("学院", "deptName",
                "班级", "clazzName",
                "学号", "studentId",
                "姓名", "name",
                "身份证号", "idNumber",
                "手机号", "phone");
        reader.setHeaderAlias(headerAliasMap);
        List<UserInsertBatchDTO> userInsertBatchDTOList = reader.readAll(UserInsertBatchDTO.class);
        Result result = userClient.addUserBatch(userInsertBatchDTOList);
        if (result.getCode() != ResultCodes.OK) {
            throw new BusinessException(result.getCode(), result.getMsg());
        }
    }

    @Override
    public List<UserVO> listUserByConditions(UserQueryDTO userQueryDTO) {
        Coladmin coladmin = ColadminHolder.getColadmin();
        userQueryDTO.setSchoolId(coladmin.getSchoolId());
        PageResult<List<UserVO>> listPageResult = userClient.listByConditions(userQueryDTO);
        if (listPageResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(listPageResult.getCode(), listPageResult.getMsg());
        }
        return listPageResult.getData();
    }

    @Override
    public void addUser(UserInsertDTO userInsertDTO) {
        Result result = userClient.addUser(userInsertDTO);
        if (result.getCode() != ResultCodes.OK) {
            throw new BusinessException(result.getCode(), result.getMsg());
        }
    }

    @Override
    public void deleteUserById(Long id) {
        Result result = userClient.deleteUserById(id);
        if (result.getCode() != ResultCodes.OK) {
            throw new BusinessException(result.getCode(), result.getMsg());
        }
    }

    @Override
    public void updateUserById(UserInsertDTO userInsertDTO, Long id) {
        Result result = userClient.updateUserById(userInsertDTO, id);
        if (result.getCode() != ResultCodes.OK) {
            throw new BusinessException(result.getCode(), result.getMsg());
        }
    }

    @Override
    public UserVO getUserVOById(Long id) {
        Result<UserVO> userVOResult = userClient.getUserVOById(id);
        if (userVOResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(userVOResult.getCode(), userVOResult.getMsg());
        }
        return userVOResult.getData();
    }
}
