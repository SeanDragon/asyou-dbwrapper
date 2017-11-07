package org.asyou.db.sessionfactory;


import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session.MongoSession;
import org.asyou.mongo.dao.MongoAdapter;

/**
 * @author SeanDragon Create By 2017-06-13 14:36
 */
public class MongoSessionFactory implements DbSessionFactory {

    private MongoAdapter mongoAdapter;
    private DbSession instanceSession;

    public MongoSessionFactory(MongoAdapter adapter) {
        this.mongoAdapter = adapter;
    }

    @Override
    public DbSession openSession() throws DbException {
        return new MongoSession(mongoAdapter);
    }

    @Override
    public <T> T createService(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DbSession getCurrentSession() throws DbException {
        if (instanceSession == null) {
            instanceSession = new MongoSession(mongoAdapter);
        }
        return instanceSession;
    }

    @Override
    public void close() throws DbException {
        instanceSession = null;
    }

    @Override
    public <T> T getNativeObj() {
        return (T) mongoAdapter;
    }
}
