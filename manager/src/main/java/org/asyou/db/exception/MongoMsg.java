package org.asyou.db.exception;

import pro.tools.data.text.ToolJson;

/**
 * Created on 17/10/22 18:24 星期日.
 *
 * @author sd
 */
public class MongoMsg {
    private String doType;
    private String content;

    MongoMsg(String doType, Object content) {
        this.doType = doType;
        this.content = ToolJson.anyToJson(content);
    }

    public static MongoMsg insertOne(Object content) {
        return new MongoMsg(DoType.INSERT_ONE, content);
    }

    public static MongoMsg insertMany(Object content) {
        return new MongoMsg(DoType.INSERT_MANY, content);
    }

    public static MongoMsg deleteOne(Object content) {
        return new MongoMsg(DoType.DELETE_ONE, content);
    }

    public static MongoMsg deleteMany(Object content) {
        return new MongoMsg(DoType.DELETE_MANY, content);
    }

    public static MongoMsg updateOne(Object content) {
        return new MongoMsg(DoType.UPDATE_ONE, content);
    }

    public static MongoMsg updateMany(Object content) {
        return new MongoMsg(DoType.UPDATE_MANY, content);
    }

    /**
     * execResult显示数据库接口返回的数据
     *
     * @return
     */
    public String result() {
        return String.format("DB:%S:%S:RESULT:%S", "MONGO", doType, content);
    }

    /**
     * 一般data接收的是执行操作的原始传入数据
     *
     * @return
     */
    public String error() {
        return String.format("DB:%S:%S:ERROR:%S", "MONGO", doType, content);
    }
}
