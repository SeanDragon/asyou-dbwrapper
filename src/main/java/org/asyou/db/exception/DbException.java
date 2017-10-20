package org.asyou.db.exception;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class DbException extends Exception {

    private DbErrorCode errCode;

    public DbException(DbErrorCode errCode) {
        super();
        this.errCode = errCode;
    }

    public DbException(String errMsg, DbErrorCode errCode) {
        super(errMsg);
        this.errCode = errCode;
    }

    public DbException(Exception e, DbErrorCode errCode) {
        super(e);
        this.errCode = errCode;
    }

    public DbException(String errMsg, Exception e, DbErrorCode errCode) {
        super(errMsg, e);
        this.errCode = errCode;
    }

    public DbErrorCode getErrCode() {
        return errCode;
    }

    public DbException setErrCode(DbErrorCode errCode) {
        this.errCode = errCode;
        return this;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        message += ";\r\n数据库异常码为:" + errCode.getCode() + "\r\n";
        return message;
    }
}
