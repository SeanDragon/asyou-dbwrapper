package org.asyou;

import com.google.common.base.Splitter;
import org.asyou.db.exception.DbException;
import org.asyou.db.manager.DbProp;
import org.asyou.db.manager.DbSessionFactory;
import org.asyou.db.manager.SequoiaDbSessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 16:10
 */
public class TestSessionFactory {
    @Test
    public void test1() {
        DbSessionFactory dbSessionFactory = new SequoiaDbSessionFactory();
        Properties properties = new Properties();
        properties.put("db.seqdb.default.id", "default");
        properties.put("db.seqdb.default.address", "default");
        // dbSessionFactory.refresh();
    }

    private static Properties PROPERTIES;

    @BeforeClass
    public static void bc() throws URISyntaxException, IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("db.properties");
        PROPERTIES = new Properties();
        InputStream inputStream = Files.newInputStream(Paths.get(resource.toURI()));
        PROPERTIES.load(inputStream);
    }

    @Test
    public void test2() {
        DbSessionFactory dbSessionFactory = new SequoiaDbSessionFactory();
        DbProp dbProp = new DbProp();
        String id = PROPERTIES.getProperty("db.seqdb.default.id");
        String db = PROPERTIES.getProperty("db.seqdb.default.db");
        String address = PROPERTIES.getProperty("db.seqdb.default.address");
        List<String> addressList = Splitter.on(address).splitToList(",");
        dbProp.setId(id)
                .setDb(db)
                .setAddressList(addressList);
        try {
            dbSessionFactory.init(dbProp);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }
}
