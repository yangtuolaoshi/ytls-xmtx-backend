package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.ColadminLoginDTO;
import love.ytlsnb.model.school.dto.ColadminRegisterDTO;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.user.dto.UserInsertBatchDTO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.vo.UserVO;
import love.ytlsnb.school.mapper.ColadminMapper;
import love.ytlsnb.school.service.ColadminService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/28 20:08
 */
@Slf4j
@Service
@RefreshScope
public class ColadminServiceImpl extends ServiceImpl<ColadminMapper, Coladmin> implements ColadminService {
    @Autowired
    private ColadminMapper coladminMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Lazy
    @Autowired
    private UserClient userClient;
    @Autowired
    private AliUtil aliUtil;

    @Value("${xmtx.jwt.coladmin-secret-key}")
    private String coladminSecretKey;
    @Value("${xmtx.jwt.user-secret-key}")
    private String userSecretKey;

    /**
     * 学校管理人员登录
     *
     * @param coladminLoginDTO 封装学校管理人员的登录账户，密码
     * @param request          用户请求对象，用于获取用户的jwt令牌
     * @return 登录成功的JWT令牌
     */
    @Override
    public String login(ColadminLoginDTO coladminLoginDTO, HttpServletRequest request) {
        log.info(coladminSecretKey);
        log.info(userSecretKey);
        // 校验传入参数
        if (StrUtil.isBlankIfStr(coladminLoginDTO.getUsername()) || StrUtil.isBlankIfStr(coladminLoginDTO.getPassword())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "登录账户登录信息不全");
        }
        Coladmin coladmin = this.getOne(new LambdaQueryWrapper<Coladmin>()
                .eq(Coladmin::getUsername, coladminLoginDTO.getUsername()));

        // 账号不存在
        if (coladmin == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "账号不存在");
        }

        // 账户存在，校验密码
        if (!BCrypt.checkpw(coladminLoginDTO.getPassword(), coladmin.getPassword())) {
            // 密码错误
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "密码错误");
        }
        // 提前创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(SchoolConstant.COLADMIN_ID, coladmin.getId());
        System.out.println(jwtProperties);
        System.out.println(jwtProperties.getColadminSecretKey());
        String jwt = JwtUtil.createJwt(jwtProperties.getColadminSecretKey(), jwtProperties.getColadminTtl(), claims);
        log.info("生成的JWT令牌：{}", jwt);
        // jwt令牌中的签名,用来做唯一登录校验
        String newSignature = jwt.substring(jwt.lastIndexOf('.') + 1);

        // 登录
        String adminLoginLockKey = RedisConstant.COLADMIN_LOGIN_LOCK_PREFIX + coladmin.getId();
        RLock lock = redissonClient.getLock(adminLoginLockKey);

        try {
            // 上锁，同一时间只允许管理员账号在一处地方进行登录操作
            boolean success = lock.tryLock();
            if (!success) {
                // 多人同时登录同一账号
                throw new BusinessException(ResultCodes.FORBIDDEN, "多人登录同一管理员账号");
            }
            // 登录，记录当前登录对应的签名
            redisTemplate.opsForValue()
                    .set(RedisConstant.COLADMIN_LOGIN_PREFIX + coladmin.getId(),
                            newSignature,
                            jwtProperties.getColadminTtl(),
                            TimeUnit.MILLISECONDS);
            return jwt;

        } finally {
            // 释放锁
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", lock);
            }
        }
    }

    /**
     * 根据传入管理员ID查找并返回脱敏后的管理员信息
     *
     * @param adminId 待查找的管理员ID
     * @return 脱敏后的管理员信息
     */
    @Override
    public Coladmin selectInsensitiveAdminById(Long adminId) {
        if (adminId == null) {
            throw new NullPointerException("传入管理员账号ID为空");
        }
        Coladmin coladmin = coladminMapper.selectOne(new LambdaQueryWrapper<Coladmin>()
                .eq(Coladmin::getId, adminId)
                .eq(Coladmin::getStatus, SchoolConstant.ENABLED));

        if (coladmin == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "管理员账号不存在/状态异常");
        }

        coladmin.setPassword(SchoolConstant.INSENSITIVE_PASSWORD);
        coladmin.setCreateTime(null);
        coladmin.setUpdateTime(null);
        coladmin.setDeleted(null);
        return coladmin;
    }

    /**
     * 注册管理员账号，创建后需要手动赋予权限
     *
     * @param coladminRegisterDTO 管理员注册数据传输对象
     */
    @Override
    @Transactional
    public void register(ColadminRegisterDTO coladminRegisterDTO) {
        // 校验参数
        if (BeanUtil.hasNullField(coladminRegisterDTO)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "传入参数不完整");
        }

        // 用户名校验
        String username = coladminRegisterDTO.getUsername();
        Coladmin selectOne = coladminMapper.selectOne(new LambdaQueryWrapper<Coladmin>()
                .eq(Coladmin::getUsername, username));
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
        }

        // 对写入操作进行上锁
        String adminRegisterLockKey = RedisConstant.COLADMIN_REGISTER_LOCK_PREFIX + coladminRegisterDTO.getUsername();
        RLock lock = redissonClient.getLock(adminRegisterLockKey);
        try {
            boolean success = lock.tryLock();
            if (!success) {
                // 多人使用同一用户名进行注册
                throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
            }

            // 获取锁成功，进行double check
            selectOne = coladminMapper.selectOne(new LambdaQueryWrapper<Coladmin>()
                    .eq(Coladmin::getUsername, username));
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
            }

            Coladmin coladmin = new Coladmin();
            BeanUtil.copyProperties(coladminRegisterDTO, coladmin);

            // 密码加盐加密
            coladmin.setPassword(BCrypt.hashpw(coladmin.getPassword()));
            // 设置属性初始值
            coladmin.setStatus(SchoolConstant.DISABLED);
            coladminMapper.insert(coladmin);
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", adminRegisterLockKey);
            }
        }
    }

}
