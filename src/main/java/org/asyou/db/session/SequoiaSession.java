package org.asyou.db.session;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.exception.SequoiaMsg;
import org.asyou.db.tool.PageConvert;
import org.asyou.db.tool.ToolPageInfo;
import org.asyou.db.tool.ToolPrimaryKey;
import org.asyou.db.type.*;
import org.asyou.sequoia.Count;
import org.asyou.sequoia.FindMany;
import org.asyou.sequoia.Page;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;
import org.asyou.sequoia.type.DateTime;
import org.asyou.sequoia.type.DateTimeFromTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:23
 */
public class SequoiaSession implements DbSession {

    private static final Logger log = LoggerFactory.getLogger(SequoiaSession.class);
    private final SequoiaAdapter sequoiaAdapter;

    public SequoiaSession(SequoiaAdapter sequoiaAdapter) {
        this.sequoiaAdapter = sequoiaAdapter;
    }

    @Override
    public <T> boolean insertOne(T data) throws DbException {
        try {
            boolean insertResult = sequoiaAdapter.insert().insertOneT(data) == 1;
            log.debug(SequoiaMsg.insertOne(insertResult).result());
            return insertResult;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.insertOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean insertMany(List<T> dataList) throws DbException {
        try {
            boolean insertResult = sequoiaAdapter.insert().insertManyT(dataList) == 1;
            log.debug(SequoiaMsg.insertMany(insertResult).result());
            return insertResult;
        } catch (SequoiaAdapterException e) {
            throw new DbException("巨衫数据库发生错误", e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteOne(T data) throws DbException {
        try {
            boolean deleteResult = sequoiaAdapter.delete(data).deleteOneT() == 1;
            log.debug(SequoiaMsg.deleteOne(deleteResult).result());
            return deleteResult;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.deleteOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteMany(T data) throws DbException {
        try {
            boolean deleteResult = sequoiaAdapter.delete(data).deleteManyT() == 1;
            log.debug(SequoiaMsg.deleteMany(deleteResult).result());
            return deleteResult;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.deleteMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateOne(T data) throws DbException {
        try {
            T queue = ToolPrimaryKey.getNewPrimaryKeyModel(data);
            boolean updateResult = sequoiaAdapter.update().updateOneT(queue, data) == 1;
            log.debug(SequoiaMsg.updateOne(updateResult).result());
            return updateResult;
        } catch (SequoiaAdapterException | IllegalAccessException | InstantiationException e) {
            log.warn(SequoiaMsg.updateOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateMany(T data) throws DbException {
        try {
            T queue = ToolPrimaryKey.getNewPrimaryKeyModel(data);
            boolean updateResult = sequoiaAdapter.update().updateManyT(queue, data) == 1;
            log.debug(SequoiaMsg.updateMany(updateResult).result());
            return updateResult;
        } catch (SequoiaAdapterException | IllegalAccessException | InstantiationException e) {
            log.warn(SequoiaMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) throws DbException {
        try {
            return sequoiaAdapter.findOne(data).getOne();
        } catch (SequoiaAdapterException e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> PageData<T> findPage(T data) throws DbException {
        return find(data, null, null, null, 0, Integer.MAX_VALUE);
    }

    @Override
    public <T> PageData<T> findPage(T data, BoolParams boolParams) throws DbException {
        return find(data, null, boolParams, null, 0, Integer.MAX_VALUE);
    }

    @Override
    public <T> PageData<T> findPage(T data, PageInfo pageInfo) throws DbException {
        pageInfo = ToolPageInfo.valid(pageInfo);
        return find(data, pageInfo.getFromToDate(), pageInfo.getBoolParams(), pageInfo.getSortMap(), pageInfo.getPageIndex(), pageInfo.getPageSize());
    }

    @Override
    public <T> PageData<T> find(T t, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize) throws DbException {
        try {
            FindMany findMany = sequoiaAdapter.findMany(t);
            if (fromToDate != null) {
                DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName()
                        , new DateTime(fromToDate.getFrom())
                        , new DateTime(fromToDate.getTo()));
                //TODO
            }
            if (sortMap != null) {
                //TODO 还未添加
            }

            //TODO contain or not 都没加

            Page<T> page = findMany.page(pageIndex, pageSize);
            PageData<T> pageData = PageConvert.page2pageData4sequoia(page);
            return pageData;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> long count(T data) throws DbException {
        return count(data, null);
    }

    @Override
    public <T> long count(T data, BoolParams boolParams) throws DbException {
        return count(data, null, boolParams);
    }

    @Override
    public <T> long count(T data, FromToDate fromToDate, BoolParams boolParams) throws DbException {
        try {
            Count count = sequoiaAdapter.count(data);
            if (fromToDate != null) {
                //TODO
            }
            //TODO contain or not
            return count.getCount();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException {
        try {
            pageInfo = ToolPageInfo.valid(pageInfo);
            //TODO
            return null;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> long countAny(Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException {
        try {
            //TODO
            return 0;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }
}
