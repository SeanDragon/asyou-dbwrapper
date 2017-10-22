package org.asyou.db.manager;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.session_factory.MongoSessionFactory;
import org.asyou.mongo.base.MongoConfig;
import org.asyou.mongo.base.MongoManager;
import org.asyou.mongo.dao.MongoAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MongoDb的方言,用来获取SessionFactory
 *
 * @author SeanDragon Create By 2017-06-13 14:35
 */
public class MongoControl extends DbControl {

    private static MongoControl single;
    private static Map<String, DbSessionFactory> dbSessionFactoryMap;

    public static MongoControl getSingle() {
        if (single == null) {
            single = new MongoControl();
        }
        return single;
    }

    private MongoControl() {
        dbSessionFactoryMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addSessionFactory(DbProp dbProp) throws DbException {
        MongoProp prop = (MongoProp) dbProp;

        MongoConfig mongoConfig = new MongoConfig();
        mongoConfig.setId(prop.getId());
        mongoConfig.setHostName(prop.getHostName());
        mongoConfig.setPort(prop.getPort());
        mongoConfig.setDatabaseName(prop.getDbName());
        mongoConfig.setPoolSize(prop.getPoolSize());
        mongoConfig.setMaxWaitTime(prop.getMaxWaitTime());
        mongoConfig.setConnectTimeout(prop.getConnectTimeout());
        mongoConfig.setBlockSize(prop.getBlockSize());
        try {
            MongoManager.putMongoConfig(mongoConfig);
            dbSessionFactoryMap.put(dbProp.getId(), buildNewSessionFactory(dbProp.getId()));
        } catch (Exception e) {
            throw new DbException("MongoManager 添加新的mongoConfig失败", e, DbErrorCode.INIT_FAIL);
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
        return new MongoSessionFactory(new MongoAdapter(sessionFactoryId));
    }
}
