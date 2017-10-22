package org.asyou.mongo;

import org.asyou.db.exception.DbException;
import org.asyou.db.manager.DbControl;
import org.asyou.db.manager.MongoControl;
import org.asyou.db.tool.ToolMongo;
import org.asyou.model.LoanModel;
import org.junit.BeforeClass;
import pro.tools.data.text.ToolRandoms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created on 17/10/20 21:15 星期五.
 *
 * @author sd
 */
public class MongoBootRunner {

    public static LoanModel buildNewModel() {
        return new LoanModel()
                .setId(ToolRandoms.getRightNanoTime())
                .setLoanName(ToolRandoms.getRandomStr())
                .setAddDate(new Date())
                ;
    }

    public static InputStream load(String fileName) {
        return MongoBootRunner.class.getClassLoader().getResourceAsStream(fileName);
    }

    static DbControl mongoControl;

    @BeforeClass
    public static void bc() {
        mongoControl = new MongoControl();
        Properties dbProp = new Properties();
        try {
            dbProp.load(load("db.properties"));
            mongoControl.addSessionFactory("default", dbProp);
            ToolMongo.resetSingle(dbProp.getProperty("db.mongo.id"));
        } catch (DbException | IOException e) {
            e.printStackTrace();
        }
    }
}
