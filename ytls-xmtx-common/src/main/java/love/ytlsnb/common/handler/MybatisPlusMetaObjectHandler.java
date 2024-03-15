package love.ytlsnb.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.PojoConstant;
import love.ytlsnb.common.constants.UserConstant;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/9 11:00
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增时自动注入对象的createTime、updateTime和deleted
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("正在进行insertFill");
        this.strictInsertFill(metaObject, PojoConstant.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, PojoConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, PojoConstant.DELETED, Byte.class, UserConstant.UNDELETED);
    }

    /**
     * 修改时自动注入对象的updateTime
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("正在进行updateFill");
        this.setFieldValByName(PojoConstant.UPDATE_TIME, LocalDateTime.now(), metaObject);
        // 注释代码为官方推荐，但是更新时由于已有更新时间不为空，默认不覆盖，则存在逻辑问题，这里直接使用老方法强行进行赋值
        // this.strictUpdateFill(metaObject, PojoConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }
}
