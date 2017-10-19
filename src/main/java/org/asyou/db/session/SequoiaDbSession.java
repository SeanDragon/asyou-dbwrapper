package org.asyou.db.session;

import org.asyou.db.manager.DbSessionFactory;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:23
 */
public class SequoiaDbSession implements DbSession {

    @Override
    public void init(DbSessionFactory dbSessionFactory) {
    }

    @Override
    public <T> T insertOne(T data) {

        return null;
    }

    @Override
    public <T> T deleteOne(T data) {
        return null;
    }

    @Override
    public <T> T updateOne(T data) {
        return null;
    }

    @Override
    public <T> T findOne(T data) {
        return null;
    }
}
