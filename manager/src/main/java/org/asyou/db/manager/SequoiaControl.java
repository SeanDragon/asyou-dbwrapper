package org.asyou.db.manager;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.sessionfactory.DbSessionFactory;
import org.asyou.db.sessionfactory.SequoiaSessionFactory;
import org.asyou.sequoia.base.Config;
import org.asyou.sequoia.base.ConfigManager;
import org.asyou.sequoia.dao.SequoiaAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class SequoiaControl extends DbControl {

    private static SequoiaControl single;
    private static Map<String, DbSessionFactory> dbSessionFactoryMap;

    static {
        dbSessionFactoryMap = new ConcurrentHashMap<>();
    }

    public static SequoiaControl getSingle() {
        if (single == null) {
            single = new SequoiaControl();
        }
        return single;
    }

    @Override
    public void addSessionFactory(DbProp dbProp) throws DbException {

        SequoiaProp prop = (SequoiaProp) dbProp;

        try {
            Config config = new Config(prop.getId(), prop.getDbName(), prop.getAddressList());
            ConfigManager.addConfig(config);
            DbSessionFactory sessionFactory = buildNewSessionFactory(dbProp.getId());
            dbSessionFactoryMap.put(dbProp.getId(), sessionFactory);
        } catch (Exception e) {
            throw new DbException("ConfigManager 添加新的sequoiaConfig失败", e, DbErrorCode.INIT_FAIL);
        }
    }

    @Override
    public DbSessionFactory getSessionFactory(String sessionFactoryId) throws DbException {
        if (dbSessionFactoryMap.get(sessionFactoryId) == null) {
            throw new DbException("不存在该sessionFactory", DbErrorCode.CONNECT_FAIL);
        }

        return dbSessionFactoryMap.get(sessionFactoryId);
    }

    @Override
    DbSessionFactory buildNewSessionFactory(String sessionFactoryId) throws Exception {
        return new SequoiaSessionFactory(new SequoiaAdapter(sessionFactoryId));
    }
}
