package org.asyou.db.session;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.exception.MongoMsg;
import org.asyou.db.tool.PageConvert;
import org.asyou.db.tool.ToolPageInfo;
import org.asyou.db.tool.ToolPrimaryKey;
import org.asyou.db.tool.ToolTable;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.FromToDate;
import org.asyou.db.type.PageData;
import org.asyou.db.type.PageInfo;
import org.asyou.db.type.SearchParam;
import org.asyou.mongo.Aggregate;
import org.asyou.mongo.Count;
import org.asyou.mongo.Find;
import org.asyou.mongo.Page;
import org.asyou.mongo.dao.MongoAdapter;
import org.asyou.mongo.exception.MongoAdapterException;
import org.asyou.mongo.query.QueryAggregate;
import org.asyou.mongo.query.QueryMatcher;
import org.asyou.mongo.query.QueryObject;
import org.asyou.mongo.type.DateTimeFromTo;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.tools.data.decimal.Decimal;
import pro.tools.data.text.ToolJson;
import pro.tools.data.text.ToolStr;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author SeanDragon Create By 2017-06-13 14:37
 */
public class MongoSession implements DbSession {

    private static final Logger log = LoggerFactory.getLogger(MongoSession.class);
    private final MongoAdapter mongoAdapter;

    public MongoSession(MongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    public <T> T getNativeObj() {
        return (T) this.mongoAdapter;
    }

    @Override
    public <T> boolean insertOne(T data) throws DbException {
        try {
            boolean insertResult = mongoAdapter.collection(ToolTable.getName(data)).insert().insertOne(data) == 0;
            log.debug(MongoMsg.insertOne(insertResult).result());
            return insertResult;
        } catch (Exception e) {
            log.warn(MongoMsg.insertOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean insertMany(List<T> dataList) throws DbException {
        try {
            boolean insertResult = mongoAdapter.collection(ToolTable.getName(dataList.get(0))).insert().insertMany(dataList) == 0;
            log.debug(MongoMsg.insertMany(insertResult).result());
            return insertResult;
        } catch (Exception e) {
            log.warn(MongoMsg.insertMany(dataList).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteOne(T data) throws DbException {
        try {
            boolean deleteResult = mongoAdapter.collection(ToolTable.getName(data)).delete().deleteOne(data) > 0;
            log.debug(MongoMsg.deleteOne(deleteResult).result());
            return deleteResult;
        } catch (Exception e) {
            log.warn(MongoMsg.deleteOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean deleteMany(T data) throws DbException {
        try {
            boolean deleteResult = mongoAdapter.collection(ToolTable.getName(data)).delete().deleteMany(data) > 0;
            log.debug(MongoMsg.deleteOne(deleteResult).result());
            return deleteResult;
        } catch (Exception e) {
            log.warn(MongoMsg.deleteMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateOne(T data) throws DbException {
        try {
            T queue = ToolPrimaryKey.getNewPrimaryKeyModel(data);
            boolean updateResult = mongoAdapter.collection(ToolTable.getName(data)).update().updateOne(queue, data) > 0;
            log.debug(MongoMsg.updateOne(updateResult).result());
            return updateResult;
        } catch (Exception e) {
            log.warn(MongoMsg.updateOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateMany(T queue, T data) throws DbException {
        try {
            boolean updateResult = mongoAdapter.collection(ToolTable.getName(data)).update().updateMany(queue, data) > 0;
            log.debug(MongoMsg.updateMany(updateResult).result());
            return updateResult;
        } catch (Exception e) {
            log.warn(MongoMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) throws DbException {
        try {
            return mongoAdapter.collection(ToolTable.getName(data)).find(data).findOne();
        } catch (MongoAdapterException e) {
            log.warn(MongoMsg.updateMany(data).error());
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
    public <T> PageData<T> findPage(T data, PageInfo pageInfo, List<SearchParam> searchParamList) throws DbException {
        pageInfo = ToolPageInfo.valid(pageInfo);
        return find(data, pageInfo.getFromToDate(), pageInfo.getBoolParams(), pageInfo.getSortMap(), pageInfo.getPageIndex(), pageInfo.getPageSize(), null, searchParamList);
    }

    @Override
    public <T> PageData<T> find(T data, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize, List<String> includeFieldList, List<SearchParam> searchParamList) throws DbException {
        try {
            Find find = mongoAdapter.collection(ToolTable.getName(data)).find(data);
            QueryMatcher queryMatcher = new QueryMatcher(data);
            boolean haveMatcher = false;

            if (fromToDate != null) {
                DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName());
                dateTimeFromTo.setFrom(fromToDate.getFrom().getLocalDateTime());
                dateTimeFromTo.setTo(fromToDate.getTo().getLocalDateTime());
                queryMatcher.dateTimeFromTo(dateTimeFromTo);
                haveMatcher = true;
            }

            if (searchParamList != null && !searchParamList.isEmpty()) {
                for (SearchParam searchParam : searchParamList) {
                    Bson inBson = Filters.in(searchParam.getFieldName(), searchParam.getValues());
                    queryMatcher.putAll(inBson);
                }
            }

            if (sortMap != null && !sortMap.isEmpty()) {
                //FIXME 待测试
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    find.sort(new QueryMatcher(sortStr));
                    haveMatcher = true;
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
                find.match(queryMatcher);
            }

            if (includeFieldList != null) {
                Bson inBson = Projections.include(includeFieldList);
                find.projection(QueryObject.create(inBson));
            }

            Page<T> page = find.page(pageIndex, pageSize);
            PageData<T> pageData = PageConvert.page2pageData4mongo(page);
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
    public <T> PageData<T> find(BSONObject bsonObject, Class<T> tClass, int pageIndex, int pageSize) throws DbException {
        Set<String> stringSet = bsonObject.keySet();
        Document document = new Document();
        stringSet.forEach(one -> document.put(one, bsonObject.get(one)));
        Page<T> page;
        try {
            page = mongoAdapter.collection(ToolTable.getName(tClass)).find(document).page(pageIndex, pageSize);
        } catch (MongoAdapterException e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }

        return PageConvert.page2pageData4mongo(page);
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
            Count count = mongoAdapter.collection(ToolTable.getName(data)).count();
            QueryMatcher queryMatcher = new QueryMatcher(data);
            boolean haveMatcher = false;

            if (fromToDate != null) {
                DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName());
                dateTimeFromTo.setFrom(fromToDate.getFrom().getLocalDateTime());
                dateTimeFromTo.setTo(fromToDate.getTo().getLocalDateTime());
                queryMatcher.dateTimeFromTo(dateTimeFromTo);
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
                count.count(queryMatcher.getBson());
            }
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
                Aggregate aggregate = mongoAdapter.collection(ToolTable.getName(data)).aggregate();

                FromToDate fromToDate = pageInfo.getFromToDate();
                BoolParams boolParams = pageInfo.getBoolParams();

                QueryMatcher queryMatcher = new QueryMatcher(data);

                if (searchParamList != null && !searchParamList.isEmpty()) {
                    for (SearchParam searchParam : searchParamList) {
                        Bson inBson = Filters.in(searchParam.getFieldName(), searchParam.getValues());
                        queryMatcher.putAll(inBson);
                    }
                }

                if (fromToDate != null) {
                    DateTimeFromTo dateTimeFromTo = new DateTimeFromTo(fromToDate.getFieldName());
                    dateTimeFromTo.setFrom(fromToDate.getFrom().getLocalDateTime());
                    dateTimeFromTo.setTo(fromToDate.getTo().getLocalDateTime());
                    queryMatcher.dateTimeFromTo(dateTimeFromTo);
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

                QueryAggregate queryAggregate = new QueryAggregate(queryMatcher.toBson());
                aggregate.match(queryAggregate);

                Number number = aggregate.sum(val);
                vale = Decimal.instance(number).moneyValue();
            } catch (MongoAdapterException e) {
                log.warn("total统计出错！", e);
            }
            sumMap.put(key, vale);
        });
        return sumMap;
    }

    @Override
    public <T> Map<String, Number> sum(T data, Map<String, String> fieldNameMap, PageInfo pageInfo) {
        return this.sum(data, fieldNameMap, pageInfo, null);
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<String> includeFieldList, List<SearchParam> searchParamList) throws DbException {
        try {
            pageInfo = ToolPageInfo.valid(pageInfo);

            List<Bson> searchs = Lists.newArrayList();
            for (SearchParam searchParam : searchParamList) {
                searchs.add(Filters.in(searchParam.getFieldName(), searchParam.getValues()));
            }

            FromToDate fromToDate = pageInfo.getFromToDate();
            if (fromToDate != null) {
                if (fromToDate.isShort()) {
                    searchs.add(Filters.gte(fromToDate.getFieldName(), fromToDate.getFrom().getLocalDateTime().toLocalDate()));
                    searchs.add(Filters.lte(fromToDate.getFieldName(), fromToDate.getTo().getLocalDateTime().toLocalDate()));
                } else {
                    searchs.add(Filters.gte(fromToDate.getFieldName(), fromToDate.getFrom().getLocalDateTime()));
                    searchs.add(Filters.lte(fromToDate.getFieldName(), fromToDate.getTo().getLocalDateTime()));
                }
            }

            Bson query = Filters.and(searchs);

            Find find = mongoAdapter.collection(ToolTable.getName(tClass)).find(query).as(tClass);
            Map<String, Integer> sortMap = pageInfo.getSortMap();
            if (!sortMap.isEmpty()) {
                Document document = new Document();
                document.putAll(sortMap);
                QueryMatcher sortQueryMatcher = new QueryMatcher(document);
                find.sort(sortQueryMatcher);
            }

            if (includeFieldList != null) {
                Bson inBson = Projections.include(includeFieldList);
                find.projection(QueryObject.create(inBson));
            }

            Page<T> page = find.page(pageInfo.getPageIndex(), pageInfo.getPageSize());
            PageData<T> pageData = PageConvert.page2pageData4mongo(page);
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
            Bson[] searchs = new Bson[searchParamList.size()];
            for (int i = 0; i < searchParamList.size(); i++) {
                SearchParam searchParam = searchParamList.get(i);
                searchs[i] = Filters.in(searchParam.getFieldName(), searchParam.getValues());
            }
            Bson query = Filters.and(searchs);
            return mongoAdapter.collection(ToolTable.getName(tClass)).count().count(query);
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }
}
