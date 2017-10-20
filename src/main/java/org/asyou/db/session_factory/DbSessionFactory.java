package org.asyou.db.session_factory;

import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;

/**
 * Session工厂，用来获得当前Session
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:56
 */
public interface DbSessionFactory {

        /**
         * 连接数据库
         *
         * @throws DbException
         */
        public void connectDb() throws DbException;

        /**
         * 打开一个Session
         *
         * @return
         *
         * @throws DbException
         */
        public DbSession openSession() throws DbException;

        /**
         * 创建一个业务类
         * @param tClass 泛型类
         * @param <T> 泛型
         * @return 泛型
         */
        public <T> T createService(Class<T> tClass);

        /**
         * 获得当前线程中的Session
         *
         * @return
         *
         * @throws DbException
         */
        public DbSession getCurrentSession() throws DbException;

        /**
         * 关闭数据库连接
         *
         * @throws DbException
         */
        public void close() throws DbException;

    }
