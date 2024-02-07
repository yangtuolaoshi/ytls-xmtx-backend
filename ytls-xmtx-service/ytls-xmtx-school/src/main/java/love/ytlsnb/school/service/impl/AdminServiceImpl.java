package love.ytlsnb.school.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.mapper.AdminMapper;
import love.ytlsnb.school.service.AdminService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/6 16:28
 */
@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 学校管理人员登录
     *
     * @param adminLoginDTO 封装学校管理人员的登录账户，密码
     * @param request       用户请求对象，用于获取用户的jwt令牌
     * @return
     */
    @Override
    public String login(AdminLoginDTO adminLoginDTO, HttpServletRequest request) {
        // 校验传入参数
        if (StrUtil.isBlankIfStr(adminLoginDTO.getUsername()) || StrUtil.isBlankIfStr(adminLoginDTO.getPassword())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "登录账户登录信息不全");
        }
        Admin admin = this.getOne(new QueryWrapper<Admin>().eq(SchoolConstant.USERNAME, adminLoginDTO.getUsername()));

        // 账号不存在
        if (admin == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "账号不存在");
        }

        // 账户存在，提前创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(SchoolConstant.ADMIN_ID, admin.getId());
        String jwt = JwtUtil.createJwt(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        log.info("生成的JWT令牌：{}", jwt);
        // jwt令牌中的签名,用来做唯一登录校验
        String newSignature = jwt.substring(jwt.lastIndexOf('.') + 1);

        // 尝试登录
        String adminLoginLockName = RedisConstant.ADMIN_LOGIN_LOCK_PREFIX + admin.getId();
        RLock lock = redissonClient.getLock(adminLoginLockName);

        try {
            // 上锁，同一时间只允许管理员账号在一处地方进行登录操作
            boolean success = lock.tryLock();
            if (!success) {
                // 多人同时登录同一账号
                throw new BusinessException(ResultCodes.FORBIDDEN, "多人登录同一管理员账号");
            }
            // 登录，记录当前登录对应的签名
            redisTemplate.opsForValue()
                    .set(RedisConstant.ADMIN_LOGIN_PREFIX + admin.getId(),
                            newSignature,
                            jwtProperties.getAdminTtl(),
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

    @Override
    public Admin selectInsensitiveAdminById(Long adminId) {
        if (adminId==null){
            throw new NullPointerException("传入管理员账号ID为空");
        }
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq(SchoolConstant.ID,adminId)
                .eq(SchoolConstant.STATUS,SchoolConstant.NORMAL));

        if(admin==null){
            throw new BusinessException(ResultCodes.UNAUTHORIZED,"管理员账号不存在/状态异常");
        }

        admin.setPassword(SchoolConstant.INSENSITIVE_PASSWORD);
        admin.setCreateTime(null);
        admin.setUpdateTime(null);
        admin.setDeleted(null);
        return admin;
    }
}
