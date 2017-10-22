package org.asyou.db.manager;

import com.google.common.collect.Maps;
import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.session_factory.MongoSessionFactory;
import org.asyou.mongo.base.MongoConfig;
import org.asyou.mongo.base.MongoManager;
import org.asyou.mongo.dao.MongoAdapter;

import java.util.Map;
import java.util.Properties;

/**
 * MongoDb的方言,用来获取SessionFactory
 *
 * @author SeanDragon Create By 2017-06-13 14:35
 */
public class MongoControl extends DbControl {

    private static Map<String, DbSessionFactory> dbSessionFactoryMap = Maps.newConcurrentMap();

    @Override
    public void addSessionFactory(String sessionFactoryId, Properties properties) throws DbException {
        MongoConfig mongoConfig = new MongoConfig();
        String id = properties.getProperty("db.mongo.id");
        String hostName = properties.getProperty("db.mongo.host");
        Integer port = Integer.valueOf(properties.getProperty("db.mongo.port"));
        String dbName = properties.getProperty("db.mongo.database");
        Integer poolSize = Integer.valueOf(properties.getProperty("db.mongo.poolSize"));
        Integer maxWaitTime = Integer.valueOf(properties.getProperty("db.mongo.maxWaitTime"));
        Integer connectTimeout = Integer.valueOf(properties.getProperty("db.mongo.connectTimeout"));
        Integer blockSize = Integer.valueOf(properties.getProperty("db.mongo.blockSize"));
        mongoConfig.setId(id);
        mongoConfig.setHostName(hostName);
        mongoConfig.setPort(port);
        mongoConfig.setDatabaseName(dbName);
        mongoConfig.setPoolSize(poolSize);
        mongoConfig.setMaxWaitTime(maxWaitTime);
        mongoConfig.setConnectTimeout(connectTimeout);
        mongoConfig.setBlockSize(blockSize);
        try {
            MongoManager.putMongoConfig(mongoConfig);
            dbSessionFactoryMap.put(sessionFactoryId, buildNewSessionFactory(sessionFactoryId));
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
