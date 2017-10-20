package org.asyou.db.tool;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.mongo.dao.MongoAdapter;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:49
 */
public class ToolMongo implements ToolDb {

    private static volatile MongoAdapter single;

    public synchronized static void resetSingle(String defaultAdapterId) throws DbException {
        try {
            single = new MongoAdapter(defaultAdapterId);
        } catch (Exception e) {
            throw new DbException("创建SequoiaAdapter对象失败", e, DbErrorCode.INIT_FAIL);
        }
    }

    public static MongoAdapter getSingle() {
        return single;
    }
}
