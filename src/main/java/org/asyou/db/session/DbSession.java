package org.asyou.db.session;

import org.asyou.db.manager.DbSessionFactory;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:56
 */
public interface DbSession {

    void init(DbSessionFactory dbSessionFactory);
    <T> T insertOne(T data);
    <T> T deleteOne(T data);
    <T> T updateOne(T data);
    <T> T findOne(T data);
}
