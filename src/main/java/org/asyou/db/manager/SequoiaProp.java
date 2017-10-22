package org.asyou.db.manager;

import java.util.List;

/**
 * Created on 17/10/22 20:42 星期日.
 *
 * @author sd
 */
public class SequoiaProp extends DbProp{

    private String dbName;
    private List<String> addressList;

    public SequoiaProp(String id) {
        super(id);
    }

    public String getDbName() {
        return dbName;
    }

    public SequoiaProp setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public SequoiaProp setAddressList(List<String> addressList) {
        this.addressList = addressList;
        return this;
    }
}
