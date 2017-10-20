package org.asyou.db.session_factory;


import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session.MongoSession;
import org.asyou.mongo.dao.IMongoAdapter;

/**
 * @author SeanDragon Create By 2017-06-13 14:36
 */
public class MongoSessionFactory implements DbSessionFactory {

    private IMongoAdapter mongoAdapter;
    private DbSession instanceSession;

    public MongoSessionFactory(IMongoAdapter adapter) {
        this.mongoAdapter = adapter;
    }

    @Override
    public void connectDb() throws DbException {
        if (instanceSession == null) {
            instanceSession = new MongoSession(mongoAdapter);
        }
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
        return instanceSession;
    }

    @Override
    public void close() throws DbException {
        instanceSession = null;
    }
}
