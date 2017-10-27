package org.asyou.db.session;

import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.exception.SequoiaMsg;
import org.asyou.db.tool.PageConvert;
import org.asyou.db.tool.ToolPageInfo;
import org.asyou.db.tool.ToolPrimaryKey;
import org.asyou.db.tool.ToolTable;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.FromToDate;
import org.asyou.db.type.PageData;
import org.asyou.db.type.PageInfo;
import org.asyou.db.type.SearchParam;
import org.asyou.sequoia.Count;
import org.asyou.sequoia.FindMany;
import org.asyou.sequoia.Page;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;
import org.asyou.sequoia.model.Matchers;
import org.asyou.sequoia.query.QueryMatcher;
import org.asyou.sequoia.type.DateFromTo;
import org.asyou.sequoia.type.DateTimeFromTo;
import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.tools.data.decimal.Decimal;
import pro.tools.data.text.ToolJson;
import pro.tools.data.text.ToolStr;

import java.util.HashMap;
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
    public <T> T getNativeObj() {
        return (T) this.sequoiaAdapter;
    }

    @Override
    public <T> boolean insertOne(T data) throws DbException {
        try {
            int insertResult = sequoiaAdapter.collection(ToolTable.getName(data)).insert().insertOneT(data);
            log.debug(SequoiaMsg.insertOne(insertResult).result());
            return insertResult == 1;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.insertOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean insertMany(List<T> dataList) throws DbException {
        try {
            int insertResult = sequoiaAdapter.collection(ToolTable.getName(dataList.get(0))).insert().insertManyT(dataList);
            log.debug(SequoiaMsg.insertMany(insertResult).result());
            return insertResult == 1;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.insertMany(dataList).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteOne(T data) throws DbException {
        try {
            //1 成功
            //0 匹配性错误，如：未匹配到和匹配超过一条
            int deleteResult = sequoiaAdapter.collection(ToolTable.getName(data)).delete(data).deleteOneT();
            log.debug(SequoiaMsg.deleteOne(deleteResult).result());
            return deleteResult == 1;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.deleteOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteMany(T data) throws DbException {
        try {
            //>0 该值返回是影响的行数
            //0 匹配性错误，没有匹配到数据
            int deleteResult = sequoiaAdapter.collection(ToolTable.getName(data)).delete(data).deleteManyT();
            log.debug(SequoiaMsg.deleteMany(deleteResult).result());
            return deleteResult > 0;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.deleteMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateOne(T data) throws DbException {
        try {
            T queue = ToolPrimaryKey.getNewPrimaryKeyModel(data);
            //1 成功
            //0 匹配性错误，如：未匹配到和匹配超过一条
            int updateResult = sequoiaAdapter.collection(ToolTable.getName(data)).update().updateOneT(queue, data);
            log.debug(SequoiaMsg.updateOne(updateResult).result());
            return updateResult == 1;
        } catch (SequoiaAdapterException | IllegalAccessException | InstantiationException e) {
            log.warn(SequoiaMsg.updateOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateMany(T queue, T data) throws DbException {
        try {
            //>0 该值返回是影响的行数
            //0 匹配性错误，没有匹配到数据
            int updateResult = sequoiaAdapter.collection(ToolTable.getName(data)).update().updateManyT(queue, data);
            log.debug(SequoiaMsg.updateMany(updateResult).result());
            return updateResult > 0;
        } catch (SequoiaAdapterException e) {
            log.warn(SequoiaMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) throws DbException {
        try {
            return sequoiaAdapter.collection(ToolTable.getName(data)).findOne(data).get();
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
        PageData<T> pageData = find(data, pageInfo.getFromToDate(), pageInfo.getBoolParams(), pageInfo.getSortMap(), pageInfo.getPageIndex(), pageInfo.getPageSize());
        pageData.setPageInfo(pageInfo);
        return pageData;
    }

    @Override
    public <T> PageData<T> find(T data, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize) throws DbException {
        try {
            FindMany findMany = sequoiaAdapter.collection(ToolTable.getName(data)).findMany(data);
            QueryMatcher queryMatcher = new QueryMatcher(data);
            boolean haveMatcher = false;

            if (fromToDate != null) {
                if (fromToDate.isShort()) {
                    DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName());
                    dateFromTo.setFrom(fromToDate.getFrom().getLocalDateTime().toLocalDate());
                    dateFromTo.setTo(fromToDate.getTo().getLocalDateTime().toLocalDate());
                    queryMatcher.dateFromTo(dateFromTo);
                } else {
                    DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName());
                    dateTimeFromTo.setFrom(fromToDate.getFrom().getLocalDateTime());
                    dateTimeFromTo.setTo(fromToDate.getTo().getLocalDateTime());
                    queryMatcher.dateTimeFromTo(dateTimeFromTo);
                }
                haveMatcher = true;
            }
            if (sortMap != null && !sortMap.isEmpty()) {
                //FIXME 待测试
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    findMany.sort(new QueryMatcher(sortStr));
                    haveMatcher = true;
                }
            }

            //FIXME 待测试
            if (boolParams != null) {
                if (boolParams.getContain()) {
                    queryMatcher.contain();
                }
                if (boolParams.getOr()) {
                    queryMatcher.or();
                }
                if (boolParams.getNot()) {
                    queryMatcher.not();
                }
                haveMatcher = true;
            }

            if (haveMatcher) {
                findMany.matcher(queryMatcher);
            }

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
            Count count = sequoiaAdapter.collection(ToolTable.getName(data)).count(data);
            QueryMatcher queryMatcher = new QueryMatcher(data);
            boolean haveMatcher = false;

            if (fromToDate != null) {
                if (fromToDate.isShort()) {
                    DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName());
                    dateFromTo.setFrom(fromToDate.getFrom().getLocalDateTime().toLocalDate());
                    dateFromTo.setTo(fromToDate.getTo().getLocalDateTime().toLocalDate());
                    queryMatcher.dateFromTo(dateFromTo);
                } else {
                    DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName());
                    dateTimeFromTo.setFrom(fromToDate.getFrom().getLocalDateTime());
                    dateTimeFromTo.setTo(fromToDate.getTo().getLocalDateTime());
                    queryMatcher.dateTimeFromTo(dateTimeFromTo);
                }
                haveMatcher = true;
            }

            if (boolParams != null) {
                //FIXME 待测试
                if (boolParams.getContain()) {
                    queryMatcher.contain();
                }
                if (boolParams.getOr()) {
                    queryMatcher.or();
                }
                if (boolParams.getNot()) {
                    queryMatcher.not();
                }
                haveMatcher = true;
            }

            if (haveMatcher) {
                count.matcher(queryMatcher);
            }

            return count.getCount();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> Map<String, Number> sum(T data, List<String> fieldNameList) {
        Map<String, Number> sumMap = new HashMap<>();
        fieldNameList.forEach(fieldName -> {
            Number val = 0;
            try {
                val = Decimal.instance(sequoiaAdapter.collection(ToolTable.getName(data)).total(data).sum(fieldName)).moneyValue();
            } catch (SequoiaAdapterException e) {
                e.printStackTrace();
            }
            sumMap.put(fieldName, val);
        });
        return sumMap;
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam> searchParamList) throws DbException {
        try {
            pageInfo = ToolPageInfo.valid(pageInfo);

            BSONObject[] searchs = new BSONObject[searchParamList.size()];
            for (int i = 0; i < searchParamList.size(); i++) {
                SearchParam searchParam = searchParamList.get(i);
                searchs[i] = Matchers.in(searchParam.getFieldName(), searchParam.getValues());
            }
            BSONObject query = Matchers.and(searchs);

            FindMany findMany = sequoiaAdapter.collection(ToolTable.getName(tClass)).findMany(query).as(tClass);
            Page<T> page = findMany.page(pageInfo.getPageIndex(), pageInfo.getPageSize());
            PageData<T> pageData = PageConvert.page2pageData4sequoia(page);
            return pageData;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> long countAny(Class<T> tClass, List<SearchParam> searchParamList) throws DbException {
        try {
            BSONObject[] searchs = new BSONObject[searchParamList.size()];
            for (int i = 0; i < searchParamList.size(); i++) {
                SearchParam searchParam = searchParamList.get(i);
                searchs[i] = Matchers.in(searchParam.getFieldName(), searchParam.getValues());
            }
            BSONObject query = Matchers.and(searchs);
            Count count = sequoiaAdapter.collection(ToolTable.getName(tClass)).count(query);
            return count.getCount();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }
}
