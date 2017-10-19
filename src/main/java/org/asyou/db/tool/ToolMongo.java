package org.asyou.db.tool;

import org.asyou.mongo.dao.MongoAdapter;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:49
 */
public class ToolMongo implements ToolDb{

    private static MongoAdapter single;

    public ToolMongo(MongoAdapter mongoAdapter) {
        if (single == null) {
            refresh(mongoAdapter);
        }
    }

    public synchronized static void refresh(MongoAdapter mongoAdapter) {
        single = mongoAdapter;
    }
}
