package org.asyou.sequoia;

import com.google.common.base.Splitter;
import org.asyou.BootRunner;
import org.junit.BeforeClass;

import java.util.List;
import java.util.Properties;

/**
 * Created on 17/10/22 20:44 星期日.
 *
 * @author sd
 */
public class SequoiaBootRunner extends BootRunner{
    @BeforeClass
    public static void bc() {

        Properties properties = load("db.sequoia.properties");
        String id = properties.getProperty("db.seqdb.default.id");
        String db = properties.getProperty("db.seqdb.default.db");
        String address = properties.getProperty("db.seqdb.default.address");
        List<String> addressList = Splitter.on(address).splitToList(",");

    }
}
