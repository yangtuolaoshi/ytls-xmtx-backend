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
        this.strictInsertFill(metaObject, PojoConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }
}
