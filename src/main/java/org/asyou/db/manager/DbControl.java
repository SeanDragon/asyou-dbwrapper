package org.asyou.db.manager;

import org.asyou.db.exception.DbException;
import org.asyou.db.session_factory.DbSessionFactory;

import java.util.Properties;

/**
 * 方言接口，用来获得自己的方言SessionFactory
 */
public interface DbControl {

        /**
     * 获得一个对应的SessionFactory，并完成数据库初始化
     *
     * @param properties
     *         配置结果
     *
     * @return Session构建类
     */
    DbSessionFactory getSessionFactory(Properties properties) throws DbException;

    /**
     * 获取一个不需要配置的SessionFactory
     *
     * @return Session构建类
     */
    DbSessionFactory getSessionFactory() throws DbException;

}
