package org.asyou.db.manager;

import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:56
 */
public interface DbSessionFactory {
    void init(DbProp dbProp) throws DbException;

    DbSession getSession();

    void close();
}
