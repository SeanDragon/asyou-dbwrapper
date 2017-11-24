package org.asyou.db.manager;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:16
 */
public class DbProp {
    private String id;

    public DbProp(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public DbProp setId(String id) {
        this.id = id;
        return this;
    }
}
