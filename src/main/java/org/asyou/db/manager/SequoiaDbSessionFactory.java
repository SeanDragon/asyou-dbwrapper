package org.asyou.db.manager;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session.SequoiaDbSession;
import org.asyou.sequoia.base.Config;
import org.asyou.sequoia.base.ConfigManager;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class SequoiaDbSessionFactory implements DbSessionFactory {

    private SequoiaAdapter adapter;

    @Override
    public void init(DbProp dbProp) throws DbException {
        try {
            String adapterId = dbProp.getId();
            Config config = new Config(adapterId, dbProp.getDb(), dbProp.getAddressList());
            ConfigManager.addConfig(config);
            adapter = new SequoiaAdapter(adapterId);
        } catch (SequoiaAdapterException e) {
            throw new DbException(e, DbErrorCode.INIT_FAIL);
        }
    }

    @Override
    public DbSession getSession() {
        DbSession dbSession = new SequoiaDbSession();
        dbSession.init(this);
        return dbSession;
    }

    @Override
    public void close() {

    }
}
