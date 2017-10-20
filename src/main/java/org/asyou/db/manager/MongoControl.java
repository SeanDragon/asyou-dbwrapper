package org.asyou.db.manager;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.session_factory.MongoSessionFactory;
import org.asyou.mongo.base.MongoConfig;
import org.asyou.mongo.base.MongoManager;
import org.asyou.mongo.dao.MongoAdapter;

import java.util.Properties;

/**
 * MongoDb的方言,用来获取SessionFactory
 *
 * @author SeanDragon Create By 2017-06-13 14:35
 */
public class MongoControl implements DbControl {

    private String adapterId;

    @Override
    public DbSessionFactory getSessionFactory(Properties properties) throws DbException {
        MongoConfig mongoConfig = new MongoConfig();
        String id = properties.getProperty("config.mongo.id");
        String hostName = properties.getProperty("config.mongo.host");
        Integer port = Integer.valueOf(properties.getProperty("db.mongo.port"));
        String dbName = properties.getProperty("config.mongo.database");
        Integer poolSize = Integer.valueOf(properties.getProperty("config.mongo.poolSize"));
        Integer maxWaitTime = Integer.valueOf(properties.getProperty("config.mongo.maxWaitTime"));
        Integer connectTimeout = Integer.valueOf(properties.getProperty("config.mongo.connectTimeout"));
        Integer blockSize = Integer.valueOf(properties.getProperty("config.mongo.blockSize"));
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
            this.adapterId = id;
            return getSessionFactory();
        } catch (Exception e) {
            throw new DbException("mongoAdapter创建失败", e, DbErrorCode.CONNECT_FAIL);
        }
    }

    @Override
    public DbSessionFactory getSessionFactory() throws DbException {
        try {
            return new MongoSessionFactory(new MongoAdapter(this.adapterId));
        } catch (Exception e) {
            throw new DbException("mongoAdapter创建失败", e, DbErrorCode.CONNECT_FAIL);
        }
    }
}
