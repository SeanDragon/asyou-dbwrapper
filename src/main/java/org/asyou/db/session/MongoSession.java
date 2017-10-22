package org.asyou.db.session;

import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.asyou.db.exception.DbErrorCode;
import org.asyou.db.exception.DbException;
import org.asyou.db.exception.MongoMsg;
import org.asyou.db.tool.PageConvert;
import org.asyou.db.tool.ToolMongo;
import org.asyou.db.tool.ToolPageInfo;
import org.asyou.db.tool.ToolPrimaryKey;
import org.asyou.db.type.*;
import org.asyou.mongo.Count;
import org.asyou.mongo.FindMany;
import org.asyou.mongo.Page;
import org.asyou.mongo.base.MongoManager;
import org.asyou.mongo.dao.IMongoAdapter;
import org.asyou.mongo.dao.MongoHost;
import org.asyou.mongo.query.IQuery;
import org.asyou.mongo.query.QueryFactory;
import org.asyou.mongo.query.QueryUtil;
import org.asyou.mongo.wrapper.DateFromTo;
import org.asyou.mongo.wrapper.DateWrapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.tools.data.text.ToolJson;
import pro.tools.data.text.ToolStr;

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
    public <T> boolean insertOne(T data) throws DbException {
        try {
            boolean insertResult = mongoAdapter.insertOne(data) == 0;
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
            boolean insertResult = mongoAdapter.insertMany(dataList) == 0;
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
            boolean deleteResult = mongoAdapter.deleteOne(data) > 0;
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
            boolean deleteResult = mongoAdapter.deleteMany(data) > 0;
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
            boolean updateResult = mongoAdapter.updateOne(queue, data).getModifiedCount() > 0;
            log.debug(MongoMsg.updateOne(updateResult).result());
            return updateResult;
        } catch (Exception e) {
            log.warn(MongoMsg.updateOne(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> boolean updateMany(T data) throws DbException {
        try {
            T queue = ToolPrimaryKey.getNewPrimaryKeyModel(data);
            boolean updateResult = mongoAdapter.updateMany(queue, data).getModifiedCount() > 0;
            log.debug(MongoMsg.updateMany(updateResult).result());
            return updateResult;
        } catch (Exception e) {
            log.warn(MongoMsg.updateMany(data).error());
            throw new DbException(e, DbErrorCode.EXEC_FAIL);
        }
    }

    @Override
    public <T> T findOne(T data) {
        return mongoAdapter.findOne(data);
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
            FindMany findMany = mongoAdapter.findMany(data);
            if (fromToDate != null) {
                DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName()
                        , new DateWrapper(fromToDate.getFrom())
                        , new DateWrapper(fromToDate.getTo()));
                findMany = findMany.dateFromTo(dateFromTo);
            }
            if (sortMap != null) {
                String sortStr = ToolJson.mapToJson(sortMap);
                if (ToolStr.notBlank(sortStr)) {
                    findMany = findMany.sort(sortStr);
                }
            }
            if (boolParams.getContain()) {
                findMany = findMany.contain();
            }
            if (boolParams.getOr()) {
                findMany = findMany.OR();
            }
            if (boolParams.getNot()) {
                findMany = findMany.NOT();
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
            Count count = mongoAdapter.count(data);
            if (fromToDate != null) {
                DateFromTo dateFromTo = new DateFromTo(fromToDate.getFieldName(), new DateWrapper(fromToDate.getFrom()), new DateWrapper(fromToDate.getTo()));
                count = count.dateFromTo(dateFromTo);
            }
            if (boolParams.getContain()) {
                count = count.contain();
            }
            if (boolParams.getOr()) {
                count = count.OR();
            }
            if (boolParams.getNot()) {
                count = count.NOT();
            }
            return count.count();
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> PageData<T> findAny(PageInfo pageInfo, Class<T> clazz, List<SearchParam<T>> searchParamList) throws DbException {
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

            IMongoAdapter adapter = ToolMongo.getSingle();

            MongoCollection<Document> collection = new MongoHost(adapter, MongoManager.getMongoConfig(adapter.getId())).getDatabase()
                    .getCollection(
                            QueryUtil.getCollectionName(clazz, null)
                    );

            IQuery query = new QueryFactory().createQuery(iQuery.toString());

            long totalCount = collection.count(query.toDocument());

            FindIterable<Document> findIterable = collection.find(query.toDocument());

            //分页
            findIterable.skip(pageInfo.getPageIndex() * pageInfo.getPageSize())
                    .limit(pageInfo.getPageSize());

            List<T> list = Lists.newLinkedList();

            for (Document document : findIterable) {
                list.add(ToolJson.mapToModel(document, clazz));
            }

            return new PageData<>(pageInfo.getPageIndex(), pageInfo.getPageSize(), totalCount, list);
        } catch (Exception e) {
            throw new DbException(e, DbErrorCode.FIND_FAIL);
        }
    }

    @Override
    public <T> long countAny(Class<T> clazz, List<SearchParam<T>> searchParamList) throws DbException {
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

            IMongoAdapter adapter = ToolMongo.getSingle();

            MongoCollection<Document> collection = new MongoHost(adapter, MongoManager.getMongoConfig(adapter.getId())).getDatabase()
                    .getCollection(
                            QueryUtil.getCollectionName(clazz, null)
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
