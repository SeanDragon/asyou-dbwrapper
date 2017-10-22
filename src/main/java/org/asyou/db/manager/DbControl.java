package org.asyou.db.manager;

import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;

import java.util.Properties;

/**
 * 方言接口，用来获得自己的方言SessionFactory
 */
public abstract class DbControl {

    private boolean haveInit;

    public void init(String sessionFactoryId, Properties properties) throws DbException {
        addSessionFactory(sessionFactoryId, properties);
        haveInit = true;
    }

    public boolean isInitEd() {
        return haveInit;
    }

    /**
     * 获得一个对应的SessionFactory，并完成数据库初始化
     *
     * @param properties 配置结果
     * @return Session构建类
     */
    public abstract void addSessionFactory(String sessionFactoryId, Properties properties) throws DbException;

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
