package org.asyou.db.session;

import org.asyou.db.exception.DbException;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.FromToDate;
import org.asyou.db.type.PageData;
import org.asyou.db.type.PageInfo;
import org.asyou.db.type.SearchParam;

import java.util.List;
import java.util.Map;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:56
 */
public interface DbSession {

    /**
     * 获取原生对象
     * @param <T>
     * @return
     */
    <T> T getNativeObj();

    <T> boolean insertOne(T data) throws DbException;

    <T> boolean insertMany(List<T> dataList) throws DbException;

    <T> boolean deleteOne(T data) throws DbException;

    <T> boolean deleteMany(T data) throws DbException;

    <T> boolean updateOne(T data) throws DbException;

    <T> boolean updateMany(T data) throws DbException;

    <T> T findOne(T data) throws DbException;

    <T> PageData<T> findPage(T data) throws DbException;

    <T> PageData<T> findPage(T data, BoolParams boolParams) throws DbException;

    <T> PageData<T> findPage(T data, PageInfo pageInfo) throws DbException;

    <T> long count(T data) throws DbException;

    <T> long count(T data, BoolParams boolParams) throws DbException;

    <T> long count(T data, FromToDate fromToDate, BoolParams boolParams) throws DbException;

    <T> PageData<T> find(T t, FromToDate fromToDate, BoolParams boolParams, Map<String, Integer> sortMap, int pageIndex, int pageSize) throws DbException;

    <T> PageData<T> findAny(PageInfo pageInfo, Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException;

    <T> long countAny(Class<T> tClass, List<SearchParam<T>> searchParamList) throws DbException;
}
