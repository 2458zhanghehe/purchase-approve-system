package abc.zyf.purchaseapprovesystem.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //MP 3.3用这个
        //strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        setFieldValByName("updateTime", new Date(), metaObject);
    }
}
