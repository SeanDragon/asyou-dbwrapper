package org.asyou.db.manager;

import java.util.List;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:16
 */
public class DbProp {
    private String id;
    private List<String> addressList;
    private String db;
    private String user;
    private String pwd;

    public String getId() {
        return id;
    }

    public DbProp setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public DbProp setAddressList(List<String> addressList) {
        this.addressList = addressList;
        return this;
    }

    public String getDb() {
        return db;
    }

    public DbProp setDb(String db) {
        this.db = db;
        return this;
    }

    public String getUser() {
        return user;
    }

    public DbProp setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPwd() {
        return pwd;
    }

    public DbProp setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }
}
