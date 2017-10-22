package org.asyou.mongo;

import org.asyou.BootRunner;
import org.asyou.db.exception.DbException;
import org.asyou.db.manager.DbControl;
import org.asyou.db.manager.MongoControl;
import org.asyou.db.manager.MongoProp;
import org.asyou.db.tool.ToolMongo;
import org.junit.BeforeClass;

import java.util.Properties;

/**
 * Created on 17/10/20 21:15 星期五.
 *
 * @author sd
 */
public class MongoBootRunner extends BootRunner {
    static DbControl mongoControl;

    @BeforeClass
    public static void bc() {
        mongoControl = MongoControl.getSingle();
        Properties dbProp = load("db.mongo.properties");
        try {
            String id = dbProp.getProperty("db.mongo.id");
            String hostName = dbProp.getProperty("db.mongo.host");
            Integer port = Integer.valueOf(dbProp.getProperty("db.mongo.port"));
            String dbName = dbProp.getProperty("db.mongo.database");
            Integer poolSize = Integer.valueOf(dbProp.getProperty("db.mongo.poolSize"));
            Integer maxWaitTime = Integer.valueOf(dbProp.getProperty("db.mongo.maxWaitTime"));
            Integer connectTimeout = Integer.valueOf(dbProp.getProperty("db.mongo.connectTimeout"));
            Integer blockSize = Integer.valueOf(dbProp.getProperty("db.mongo.blockSize"));

            MongoProp mongoProp = new MongoProp(id);
            mongoProp.setHostName(hostName)
                    .setPort(port)
                    .setHostName(hostName)
                    .setDbName(dbName)
                    .setPoolSize(poolSize)
                    .setMaxWaitTime(maxWaitTime)
                    .setConnectTimeout(connectTimeout)
                    .setBlockSize(blockSize)
            ;

            mongoControl.addSessionFactory(mongoProp);
            ToolMongo.resetSingle(dbProp.getProperty("db.mongo.id"));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
