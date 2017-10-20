package org.asyou.db.session;

import org.asyou.db.exception.DbException;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.FromToDate;
import org.asyou.db.type.PageData;
import org.asyou.db.type.PageInfo;
import org.asyou.db.type.SearchParam;
import org.asyou.sequoia.dao.SequoiaAdapter;

import java.util.List;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:23
 */
public class SequoiaSession implements DbSession {

    private final SequoiaAdapter sequoiaAdapter;

    public SequoiaSession(SequoiaAdapter sequoiaAdapter) {
        this.sequoiaAdapter = sequoiaAdapter;
    }

    @Override
    public <T> boolean insertOne(T data) throws DbException {
        return false;
    }

    @Override
    public <T> boolean insertMany(List<T> dataList) throws DbException {
        return false;
    }

    @Override
    public <T> boolean deleteOne(T data) throws DbException {
        return false;
    }

    @Override
    public <T> boolean deleteMany(T data) throws DbException {
        return false;
    }

    @Override
    public <T> boolean updateOne(T data) throws DbException {
        return false;
    }

    @Override
    public <T> boolean updateMany(T data) throws DbException {
        return false;
    }

    @Override
    public <T> T findOne(T data) throws DbException {
        return null;
    }

    @Override
    public <T> PageData<T> findPage(T data) throws DbException {
        return null;
    }

    @Override
    public <T> PageData<T> findPage(T data, BoolParams boolParams) throws DbException {
        return null;
    }

    @Override
    public <T> PageData<T> findPage(T data, PageInfo pageInfo) throws DbException {
        return null;
    }

    @Override
    public <T> long count(T data) throws DbException {
        return 0;
    }

    @Override
    public <T> long count(T data, BoolParams boolParams) throws DbException {
        return 0;
    }

    @Override
    public <T> long count(T data, FromToDate fromToDate, BoolParams boolParams) throws DbException {
        return 0;
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException {
        return null;
    }

    @Override
    public <T> long countAny(Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException {
        return 0;
    }
}
