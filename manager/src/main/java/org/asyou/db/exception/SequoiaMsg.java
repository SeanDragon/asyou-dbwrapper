package org.asyou.db.exception;

import pro.tools.data.text.ToolJson;

/**
 * Created on 17/10/22 18:36 星期日.
 *
 * @author sd
 */
public class SequoiaMsg {
    private String doType;
    private String content;

    SequoiaMsg(String doType, Object content) {
        this.doType = doType;
        this.content = ToolJson.anyToJson(content);
    }

    public static SequoiaMsg insertOne(Object content) {
        return new SequoiaMsg(DoType.INSERT_ONE, content);
    }

    public static SequoiaMsg insertMany(Object content) {
        return new SequoiaMsg(DoType.INSERT_MANY, content);
    }

    public static SequoiaMsg deleteOne(Object content) {
        return new SequoiaMsg(DoType.DELETE_ONE, content);
    }

    public static SequoiaMsg deleteMany(Object content) {
        return new SequoiaMsg(DoType.DELETE_MANY, content);
    }

    public static SequoiaMsg updateOne(Object content) {
        return new SequoiaMsg(DoType.UPDATE_ONE, content);
    }

    public static SequoiaMsg updateMany(Object content) {
        return new SequoiaMsg(DoType.UPDATE_MANY, content);
    }

    /**
     * execResult显示数据库接口返回的数据
     *
     * @return
     */
    public String result() {
        return String.format("DB:%S:%S:RESULT:%S", "SEQUOIA", doType, content);
    }

    /**
     * 一般data接收的是执行操作的原始传入数据
     *
     * @return
     */
    public String error() {
        return String.format("DB:%S:%S:ERROR:%S", "SEQUOIA", doType, content);
    }
}
