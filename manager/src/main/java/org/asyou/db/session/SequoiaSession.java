package org.asyou.db.session;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import org.asyou.sequoia.Aggregate;
import org.asyou.sequoia.Count;
import org.asyou.sequoia.Find;
import org.asyou.sequoia.Page;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.model.Commons;
import org.asyou.sequoia.model.Matchers;
import org.asyou.sequoia.query.QueryAggregate;
import org.asyou.sequoia.query.QueryMatcher;
import org.asyou.sequoia.query.QueryObject;
import org.asyou.sequoia.type.DateFromTo;
import org.asyou.sequoia.type.DateTimeFromTo;
import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.tools.data.decimal.Decimal;
import pro.tools.data.text.ToolJson;
import pro.tools.data.text.ToolStr;

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
        } catch (Exception e) {
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
        } catch (Exception e) {
            log.warn(SequoiaMsg.insertMany(dataList).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteOne(T data) throws DbException {
        try {
            //1 成功
            //0 匹配性错误，如：未匹配到和匹配超过一条
            int deleteResult = sequoiaAdapter.collection(ToolTable.getName(data)).delete().deleteOneT(data);
            log.debug(SequoiaMsg.deleteOne(deleteResult).result());
            return deleteResult == 1;
        } catch (Exception e) {
            log.warn(SequoiaMsg.deleteOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteMany(T data) throws DbException {
        try {
            //>0 该值返回是影响的行数
            //0 匹配性错误，没有匹配到数据
            int deleteResult = sequoiaAdapter.collection(ToolTable.getName(data)).delete().deleteManyT(data);
            log.debug(SequoiaMsg.deleteMany(deleteResult).result());
            return deleteResult > 0;
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            log.warn(SequoiaMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) throws DbException {
        try {
            return sequoiaAdapter.collection(ToolTable.getName(data)).find(data).findOne();
        } catch (Exception e) {
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
    public <T> PageData<T> findPage(T data, PageInfo pageInfo, List<SearchParam> searchParamList) throws DbException {
        pageInfo = ToolPageInfo.valid(pageInfo);
        PageData<T> pageData = find(data, pageInfo.getFromToDate(), pageInfo.getBoolParams(), pageInfo.getSortMap(), pageInfo.getPageIndex(), pageInfo.getPageSize(), null, searchParamList);
        pageData.setPageInfo(pageInfo);
        return pageData;
    }

    @Override
    public <T> PageData<T> find(T data, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize, List<String> includeFieldList, List<SearchParam> searchParamList) throws DbException {
        try {
            Find find = sequoiaAdapter.collection(ToolTable.getName(data)).find(data);
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

            if (searchParamList != null && !searchParamList.isEmpty()) {
                for (SearchParam searchParam : searchParamList) {
                    queryMatcher.getBsonObject().putAll(Matchers.in(searchParam.getFieldName(), searchParam.getValues()));
                }
            }

            if (sortMap != null && !sortMap.isEmpty()) {
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    find.sort(new QueryMatcher(sortStr));
                }
            }

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
                find.matcher(queryMatcher);
            }

            if (includeFieldList != null) {
                BSONObject includeBSONObject = Commons.combine(1, includeFieldList);
                find.selector(QueryObject.create(includeBSONObject));
            }

            Page<T> page = find.page(pageIndex, pageSize);
            PageData<T> pageData = PageConvert.page2pageData4sequoia(page);
            return pageData;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> PageData<T> find(T data, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize) throws DbException {
        return find(data, fromToDate, boolParams, sortMap, pageIndex, pageSize, null, null);
    }

    @Override
    public <T> PageData<T> find(BSONObject bsonObject, Class<T> tClass, Map<String, Integer> sortMap, int pageIndex, int pageSize, List<String> includeFieldList) throws DbException {
        Page<T> page;
        try {
            Find find = sequoiaAdapter.collection(ToolTable.getName(tClass)).find(bsonObject).as(tClass);

            if (sortMap != null && !sortMap.isEmpty()) {
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    find.sort(new QueryMatcher(sortStr));
                }
            }

            if (includeFieldList != null) {
                BSONObject includeBSONObject = Commons.combine(1, includeFieldList);
                find.selector(QueryObject.create(includeBSONObject));
            }

            page = find.page(pageIndex, pageSize);
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
        return PageConvert.page2pageData4sequoia(page);
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
            Count count = sequoiaAdapter.collection(ToolTable.getName(data)).count();
            QueryMatcher queryMatcher = new QueryMatcher(data);

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
            }

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
            }

            count.matcher(queryMatcher);

            return count.count();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> Map<String, Number> sum(T data, Map<String, String> fieldNameMap, PageInfo pageInfo, List<SearchParam> searchParamList) {
        Map<String, Number> sumMap = Maps.newHashMapWithExpectedSize(fieldNameMap.size());
        fieldNameMap.forEach((key, val) -> {
            Number vale = 0D;
            try {
                Aggregate aggregate = sequoiaAdapter.collection(ToolTable.getName(data)).aggregate();

                FromToDate fromToDate = pageInfo.getFromToDate();
                BoolParams boolParams = pageInfo.getBoolParams();

                QueryMatcher queryMatcher = new QueryMatcher(data);

                if (searchParamList != null && !searchParamList.isEmpty()) {
                    for (SearchParam searchParam : searchParamList) {
                        queryMatcher.getBsonObject().putAll(Matchers.in(searchParam.getFieldName(), searchParam.getValues()));
                    }
                }

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
                }

                QueryAggregate queryAggregate = new QueryAggregate(queryMatcher.toBSONObject());
                aggregate.matcher(queryAggregate);

                vale = Decimal.instance(aggregate.sum(val)).moneyValue();
            } catch (Exception e) {
                log.warn("total统计出错！", e);
            }
            sumMap.put(key, vale);
        });
        return sumMap;
    }

    @Override
    public <T> Map<String, Number> sum(T data, Map<String, String> fieldNameMap, PageInfo pageInfo) {
        return sum(data, fieldNameMap, pageInfo, null);
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<String> includeFieldList, List<SearchParam> searchParamList) throws DbException {
        try {
            pageInfo = ToolPageInfo.valid(pageInfo);

            List<BSONObject> searchs = Lists.newArrayList();
            for (SearchParam searchParam : searchParamList) {
                searchs.add(Matchers.in(searchParam.getFieldName(), searchParam.getValues()));
            }

            FromToDate fromToDate = pageInfo.getFromToDate();
            if (fromToDate != null) {
                if (fromToDate.isShort()) {
                    searchs.add(Matchers.gte(fromToDate.getFieldName(), fromToDate.getFrom().getLocalDateTime().toLocalDate()));
                    searchs.add(Matchers.lte(fromToDate.getFieldName(), fromToDate.getTo().getLocalDateTime().toLocalDate()));
                } else {
                    searchs.add(Matchers.gte(fromToDate.getFieldName(), fromToDate.getFrom().getLocalDateTime()));
                    searchs.add(Matchers.lte(fromToDate.getFieldName(), fromToDate.getTo().getLocalDateTime()));
                }
            }

            BSONObject query = Matchers.and(searchs);

            Find find = sequoiaAdapter.collection(ToolTable.getName(tClass)).find(query).as(tClass);
            Map<String, Integer> sortMap = pageInfo.getSortMap();
            if (!sortMap.isEmpty()) {
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    find.sort(new QueryMatcher(sortStr));
                }
            }

            if (includeFieldList != null) {
                BSONObject includeBSONObject = Commons.combine(1, includeFieldList);
                find.selector(QueryObject.create(includeBSONObject));
            }

            Page<T> page = find.page(pageInfo.getPageIndex(), pageInfo.getPageSize());
            PageData<T> pageData = PageConvert.page2pageData4sequoia(page);
            return pageData;
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam> searchParamList) throws DbException {
        return findAny(pageInfo, tClass, null, searchParamList);
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
            return sequoiaAdapter.collection(ToolTable.getName(tClass)).count().count(query);
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }
}
