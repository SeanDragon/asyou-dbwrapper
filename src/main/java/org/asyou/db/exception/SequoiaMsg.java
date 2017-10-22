package org.asyou.db.exception;

/**
 * Created on 17/10/22 18:36 星期日.
 *
 * @author sd
 */
public class SequoiaMsg extends DbMsg {
    SequoiaMsg(String doType, Object content) {
        super(doType, content);
    }

    @Override
    public String getDbType() {
        return "SEQUOIA";
    }
}
