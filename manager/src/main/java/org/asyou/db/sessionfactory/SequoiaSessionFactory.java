package org.asyou.db.sessionfactory;

import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session.SequoiaSession;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.transaction.proxy.ProxyFactory;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-20 16:04
 */
public class SequoiaSessionFactory implements DbSessionFactory {

    private SequoiaAdapter sequoiaAdapter;
    private DbSession instanceSession;

    public SequoiaSessionFactory(SequoiaAdapter adapter) {
        this.sequoiaAdapter = adapter;
    }

    @Override
    public DbSession openSession() throws DbException {
        return new SequoiaSession(sequoiaAdapter);
    }

    @Override
    public <T> T createService(Class<T> tClass) {
        return ProxyFactory.create(tClass, sequoiaAdapter);
    }

    @Override
    public DbSession getCurrentSession() throws DbException {
        if (instanceSession == null) {
            instanceSession = new SequoiaSession(sequoiaAdapter);
        }
        return instanceSession;
    }

    @Override
    public void close() throws DbException {
        instanceSession = null;
    }

    @Override
    public <T> T getNativeObj() {
        return (T) sequoiaAdapter;
    }
}
