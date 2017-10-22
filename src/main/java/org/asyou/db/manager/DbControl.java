package org.asyou.db.manager;

import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;

/**
 * 方言接口，用来获得自己的方言SessionFactory
 */
public abstract class DbControl {
    /**
     * 获得一个对应的SessionFactory，并完成数据库初始化
     *
     * @param dbProp 配置结果
     * @return Session构建类
     */
    public abstract void addSessionFactory(DbProp dbProp) throws DbException;

    /**
     * 获取一个不需要配置的SessionFactory
     *
     * @return Session构建类
     */
    public abstract DbSessionFactory getSessionFactory(String sessionFactoryId) throws DbException;

    /**
     * @param sessionFactoryId
     * @return
     * @throws Exception
     */
    abstract DbSessionFactory buildNewSessionFactory(String sessionFactoryId) throws Exception;
}
