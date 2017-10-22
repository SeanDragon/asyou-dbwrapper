package org.asyou.db.manager;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.session_factory.SequoiaSessionFactory;
import org.asyou.sequoia.base.Config;
import org.asyou.sequoia.base.ConfigManager;
import org.asyou.sequoia.dao.SequoiaAdapter;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class SequoiaControl extends DbControl {

    private static Map<String, DbSessionFactory> dbSessionFactoryMap = Maps.newConcurrentMap();

    @Override
    public void addSessionFactory(String sessionFactoryId, Properties properties) throws DbException {
        String id = properties.getProperty("db.seqdb.default.id");
        String db = properties.getProperty("db.seqdb.default.db");
        String address = properties.getProperty("db.seqdb.default.address");
        List<String> addressList = Splitter.on(address).splitToList(",");

        try {
            Config config = new Config(id, db, addressList);
            ConfigManager.addConfig(config);
            dbSessionFactoryMap.put(sessionFactoryId, buildNewSessionFactory(sessionFactoryId));
        } catch (Exception e) {
            throw new DbException("ConfigManager 添加新的sequoiaConfig失败", e, DbErrorCode.CONNECT_FAIL);
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
