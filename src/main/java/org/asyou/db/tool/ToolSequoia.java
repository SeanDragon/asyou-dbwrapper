package org.asyou.db.tool;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;

/**
 * 巨衫数据库的集成类
 *
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:31
 */
public class ToolSequoia implements ToolDb {

    private static volatile SequoiaAdapter single;

    public synchronized static void resetSingle(String defaultAdapterId) throws DbException {
        try {
            single = new SequoiaAdapter(defaultAdapterId);
        } catch (SequoiaAdapterException e) {
            throw new DbException("创建SequoiaAdapter对象失败", e, DbErrorCode.INIT_FAIL);
        }
    }

    public static SequoiaAdapter getSingle() {
        return single;
    }
}
