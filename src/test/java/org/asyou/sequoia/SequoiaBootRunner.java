package org.asyou.sequoia;

import com.google.common.base.Splitter;
import org.asyou.BootRunner;
import org.asyou.db.exception.DbException;
import org.asyou.db.manager.SequoiaControl;
import org.asyou.db.manager.SequoiaProp;
import org.junit.BeforeClass;

import java.util.List;
import java.util.Properties;

/**
 * Created on 17/10/22 20:44 星期日.
 *
 * @author sd
 */
public class SequoiaBootRunner extends BootRunner {

    @BeforeClass
    public static void bc() {
        Properties properties = load("db.sequoia.properties");
        String id = properties.getProperty("db.seqdb.default.id");
        String db = properties.getProperty("db.seqdb.default.db");
        String address = properties.getProperty("db.seqdb.default.address");
        address = address.replace('[', ' ').replace(']', ' ').trim();
        List<String> addressList = Splitter.on(",").splitToList(address);
        SequoiaProp sequoiaProp = new SequoiaProp(id);
        sequoiaProp.setDbName(db)
                .setAddressList(addressList);
        try {
            SequoiaControl.getSingle().addSessionFactory(sequoiaProp);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
