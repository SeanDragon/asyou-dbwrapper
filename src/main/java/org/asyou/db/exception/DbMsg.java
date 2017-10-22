package org.asyou.db.exception;

import pro.tools.data.text.ToolJson;

/**
 * Created on 17/10/22 18:08 星期日.
 *
 * @author sd
 */
public class DbMsg {
    private String doType;
    private String content;

    DbMsg(String doType, Object content) {
        this.doType = doType;
        this.content = ToolJson.anyToJson(content);
    }

    /**
     * 必须由子类重写
     *
     * @return
     */
    public String getDbType() {
        throw new IllegalArgumentException("需要子类实现");
    }

    public static DbMsg insertOne(Object content) {
        return new DbMsg(DoType.INSERT_ONE, content);
    }

    public static DbMsg insertMany(Object content) {
        return new DbMsg(DoType.INSERT_MANY, content);
    }

    public static DbMsg deleteOne(Object content) {
        return new DbMsg(DoType.DELETE_ONE, content);
    }

    public static DbMsg deleteMany(Object content) {
        return new DbMsg(DoType.DELETE_MANY, content);
    }

    public static DbMsg updateOne(Object content) {
        return new DbMsg(DoType.UPDATE_ONE, content);
    }

    public static DbMsg updateMany(Object content) {
        return new DbMsg(DoType.UPDATE_MANY, content);
    }

    /**
     * execResult显示数据库接口返回的数据
     *
     * @return
     */
    public String result() {
        return String.format("DB:%S:%S:RESULT:%S", getDbType(), doType, content);
    }

    /**
     * 一般data接收的是执行操作的原始传入数据
     *
     * @return
     */
    public String error() {
        return String.format("DB:%S:%S:ERROR:%S", getDbType(), doType, content);
    }

    private class DoType {
        private static final String INSERT_ONE = "INSERT_ONE";
        private static final String INSERT_MANY = "INSERT_MANY";
        private static final String DELETE_ONE = "DELETE_ONE";
        private static final String DELETE_MANY = "DELETE_MANY";
        private static final String UPDATE_ONE = "UPDATE_ONE";
        private static final String UPDATE_MANY = "UPDATE_MANY";
    }
}
