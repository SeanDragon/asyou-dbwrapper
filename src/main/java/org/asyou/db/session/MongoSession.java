package org.asyou.db.session;

import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
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
import org.asyou.mongo.Count;
import org.asyou.mongo.FindMany;
import org.asyou.mongo.Page;
import org.asyou.mongo.base.MongoManager;
import org.asyou.mongo.dao.IMongoAdapter;
import org.asyou.mongo.dao.MongoHost;
import org.asyou.mongo.query.IQuery;
import org.asyou.mongo.query.QueryFactory;
import org.asyou.mongo.wrapper.DateFromTo;
import org.asyou.mongo.wrapper.DateWrapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.tools.data.text.ToolJson;
import pro.tools.data.text.ToolStr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SeanDragon Create By 2017-06-13 14:37
 */
public class MongoSession implements DbSession {

    private static final Logger log = LoggerFactory.getLogger(MongoSession.class);
    private final IMongoAdapter mongoAdapter;

    public MongoSession(IMongoAdapter mongoAdapter) {
        this.mongoAdapter = mongoAdapter;
    }

    @Override
    public <T> T getNativeObj() {
        return (T) this.mongoAdapter;
    }

    @Override
    public <T> boolean insertOne(T data) throws DbException {
        try {
            boolean insertResult = mongoAdapter.collection(ToolTable.getName(data)).insertOne(data) == 0;
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
            boolean insertResult = mongoAdapter.collection(ToolTable.getName(dataList.get(0))).insertMany(dataList) == 0;
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
            boolean deleteResult = mongoAdapter.collection(ToolTable.getName(data)).deleteOne(data) > 0;
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
            boolean deleteResult = mongoAdapter.collection(ToolTable.getName(data)).deleteMany(data) > 0;
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
            boolean updateResult = mongoAdapter.collection(ToolTable.getName(data)).updateOne(queue, data).getModifiedCount() > 0;
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
            boolean updateResult = mongoAdapter.collection(ToolTable.getName(data)).updateMany(queue, data).getModifiedCount() > 0;
            log.debug(MongoMsg.updateMany(updateResult).result());
            return updateResult;
        } catch (Exception e) {
            log.warn(MongoMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) {
        return mongoAdapter.collection(ToolTable.getName(data)).findOne(data);
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
    public <T> PageData<T> find(T data, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize) throws DbException {
        try {
            FindMany findMany = mongoAdapter.collection(ToolTable.getName(data)).findMany(data);
            if (fromToDate != null) {
                DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName()
                        , new DateWrapper(fromToDate.getFrom().toDate())
                        , new DateWrapper(fromToDate.getTo().toDate()));
                findMany.dateFromTo(dateFromTo);
            }
            if (sortMap != null && !sortMap.isEmpty()) {
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    findMany.sort(sortStr);
                }
            }

            if (boolParams != null) {
                if (boolParams.getContain()) {
                    findMany.contain();
                }
                if (boolParams.getOr()) {
                    findMany.OR();
                }
                if (boolParams.getNot()) {
                    findMany.NOT();
                }
            }
            Page<T> page = findMany.page(pageIndex, pageSize);
            PageData<T> pageData = PageConvert.page2pageData4mongo(page);
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
            Count count = mongoAdapter.collection(ToolTable.getName(data)).count(data);
            if (fromToDate != null) {
                DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName(), new DateWrapper(fromToDate.getFrom().toDate()), new DateWrapper(fromToDate.getTo().toDate()));
                count.dateFromTo(dateFromTo);
            }
            if (boolParams.getContain()) {
                count.contain();
            }
            if (boolParams.getOr()) {
                count.OR();
            }
            if (boolParams.getNot()) {
                count.NOT();
            }
            return count.count();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> Map<String, Number> sum(T data, List<String> fieldNameList) {
        Map<String, Number> sumMap = new HashMap<>();
        fieldNameList.forEach(fieldName -> {
            Number fieldNameSum = mongoAdapter.total(data, fieldName).sum(fieldName);
            sumMap.put(fieldName, fieldNameSum);
        });
        return sumMap;
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam> searchParamList) throws DbException {
        try {
            pageInfo = ToolPageInfo.valid(pageInfo);
            StringBuilder iQuery = new StringBuilder("{");
            searchParamList.forEach(one -> {
                Class<?> oneClazz;
                try {
                    oneClazz = Class.forName(one.getClazzName());
                } catch (ClassNotFoundException e) {
                    return;
                }
                String listAttrStr = list2bson(one.getValues(), oneClazz);
                String oneIQuery = "'" + one.getFieldName() + "':{'$in':" + listAttrStr + "}";
                iQuery.append(oneIQuery).append(",");
            });
            iQuery.deleteCharAt(iQuery.length() - 1);
            iQuery.append("}");
            MongoCollection<Document> collection = new MongoHost(mongoAdapter, MongoManager.getMongoConfig(mongoAdapter.getId())).getDatabase()
                    .getCollection(
                            ToolTable.getName(tClass)
                    );

            IQuery query = new QueryFactory().createQuery(iQuery.toString());

            long totalCount = collection.count(query.toDocument());

            FindIterable<Document> findIterable = collection.find(query.toDocument());

            Map<String, Integer> sortMap = pageInfo.getSortMap();
            if(!sortMap.isEmpty()) {
                IQuery sortQuery = new QueryFactory().createQuery(sortMap);
                findIterable.sort(sortQuery.toDocument());
            }

            //分页
            findIterable.skip(pageInfo.getPageIndex() * pageInfo.getPageSize())
                    .limit(pageInfo.getPageSize());

            List<T> list = Lists.newLinkedList();

            for (Document document : findIterable) {
                list.add(ToolJson.mapToModel(document, tClass));
            }

            return new PageData<>(pageInfo.getPageIndex(), pageInfo.getPageSize(), totalCount, list);
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> long countAny(Class<T> tClass, List<SearchParam> searchParamList) throws DbException {
        try {
            StringBuilder iQuery = new StringBuilder("{");
            searchParamList.forEach(one -> {
                Class<?> oneClazz;
                try {
                    oneClazz = Class.forName(one.getClazzName());
                } catch (ClassNotFoundException e) {
                    return;
                }
                String listAttrStr = list2bson(one.getValues(), oneClazz);
                String oneIQuery = "'" + one.getFieldName() + "':{'$in':" + listAttrStr + "}";
                iQuery.append(oneIQuery).append(",");
            });
            iQuery.deleteCharAt(iQuery.length() - 1);
            iQuery.append("}");

            MongoCollection<Document> collection = new MongoHost(mongoAdapter, MongoManager.getMongoConfig(mongoAdapter.getId())).getDatabase()
                    .getCollection(
                            ToolTable.getName(tClass)
                    );

            IQuery query = new QueryFactory().createQuery(iQuery.toString());

            return collection.count(query.toDocument());
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    private static <T> String list2bson(final List objects, Class<T> clazz) {
        return list2str("[", "]", ",", objects, clazz);
    }

    private static <T> String list2str(final String start, final String end, final String sep, final List objects, Class<T> clazz) {
        StringBuilder stringBuilder = new StringBuilder(start);

        objects.forEach(object -> {
            if (object instanceof String) {
                stringBuilder.append("\"").append(object.toString()).append("\"").append(sep);
            } else if (object.getClass().equals(clazz)) {
                stringBuilder.append(clazz.cast(object)).append(sep);
            } else {
                T jsonToAny = ToolJson.jsonToAny(ToolJson.anyToJson(object), clazz);
                stringBuilder.append(clazz.cast(jsonToAny)).append(sep);
            }
        });
        int i = stringBuilder.lastIndexOf(sep);
        return stringBuilder.substring(0, i) + end;
    }
}
