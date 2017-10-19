package org.asyou.db.exception;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:13
 */
public class DbException extends Exception {

    private int errCode;

    public DbException(int errCode) {
        super();
        this.errCode = errCode;
    }

    public DbException(String errMsg, int errCode) {
        super(errMsg);
        this.errCode = errCode;
    }

    public DbException(Exception e, int errCode) {
        super(e);
        this.errCode = errCode;
    }

    public DbException(String errMsg, Exception e, int errCode) {
        super(errMsg, e);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public DbException setErrCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        message += ";\r\n数据库异常码为:" + errCode + "\r\n";
        return message;
    }
}
