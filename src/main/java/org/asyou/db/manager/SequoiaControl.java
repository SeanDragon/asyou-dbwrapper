package org.asyou.db.manager;

import com.google.common.base.Splitter;
import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.session_factory.SequoiaSessionFactory;
import org.asyou.sequoia.base.Config;
import org.asyou.sequoia.base.ConfigManager;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;

import java.util.List;
import java.util.Properties;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class SequoiaControl implements DbControl {

    private String adapterId;

    @Override
    public DbSessionFactory getSessionFactory(Properties properties) throws DbException {

        String id = properties.getProperty("db.seqdb.default.id");
        String db = properties.getProperty("db.seqdb.default.db");
        String address = properties.getProperty("db.seqdb.default.address");
        List<String> addressList = Splitter.on(address).splitToList(",");

        try {
            Config config = new Config(id, db, addressList);
            ConfigManager.addConfig(config);
            this.adapterId = id;
            return getSessionFactory();
        } catch (SequoiaAdapterException e) {
            throw new DbException("sequoiaAdapter创建失败", e, DbErrorCode.CONNECT_FAIL);
        }
    }

    @Override
    public DbSessionFactory getSessionFactory() throws DbException {
        try {
            return new SequoiaSessionFactory(new SequoiaAdapter(adapterId));
        } catch (SequoiaAdapterException e) {
            throw new DbException("sequoiaAdapter创建失败", e, DbErrorCode.CONNECT_FAIL);
        }
    }
}
