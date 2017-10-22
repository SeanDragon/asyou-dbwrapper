package org.asyou.db.exception;

/**
 * Created on 17/10/22 18:24 星期日.
 *
 * @author sd
 */
public class MongoMsg extends DbMsg {


    MongoMsg(String doType, Object content) {
        super(doType, content);
    }

    @Override
    public String getDbType() {
        return "MONGO";
    }
}
