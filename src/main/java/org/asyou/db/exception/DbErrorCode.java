package org.asyou.db.exception;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:21
 */
public enum DbErrorCode {
    /**
     * 初始化失败
     */
    INIT_FAIL(101),
    /**
     * 建立连接失败
     */
    CONNECT_FAIL(102),
    /**
     * 执行失败（增删改操作）
     */
    EXEC_FAIL(103),
    /**
     * 查询失败，一般不抛异常，返回null
     */
    FIND_FAIL(104),
    /**
     * 关闭连接失败
     */
    CLOSE_FAIL(105)
    /**
     * 销毁失败
     */
    , DESTORY_FAIL(106);

    private int code;

    DbErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
