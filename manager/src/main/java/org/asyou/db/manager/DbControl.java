package org.asyou.db.manager;

import org.asyou.db.exception.DbException;
import org.asyou.db.sessionfactory.DbSessionFactory;

/**
 * 方言接口，用来获得自己的方言SessionFactory
 * @author SeanDragon
 */
public abstract class DbControl {
    /**
     * 获得一个对应的SessionFactory，并完成数据库初始化
     *
     * @param dbProp 配置结果
     * @return Session构建类
     * @throws DbException 数据库异常类，这个方法报的应该是INIT_FAIL
     */
    public abstract void addSessionFactory(DbProp dbProp) throws DbException;

    /**
     * 获取一个不需要配置的SessionFactory
     *
     * @return Session构建类
     * @throws DbException 数据库异常类，这个方法报的应该是CONNECT_FAIL
     */
    public abstract DbSessionFactory getSessionFactory(String sessionFactoryId) throws DbException;

    /**
     * 构建一个新的SessionFactory，只用于DbControl及其自类调用
     * @param sessionFactoryId 新的sessionFactory的编号
     * @return 新对象
     * @throws Exception 用于调用时更好的捕获
     */
    abstract DbSessionFactory buildNewSessionFactory(String sessionFactoryId) throws Exception;
}
