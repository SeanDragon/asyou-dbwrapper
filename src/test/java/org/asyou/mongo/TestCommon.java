package org.asyou.mongo;

import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.tool.ToolMongo;
import org.asyou.mongo.dao.MongoAdapter;
import org.asyou.service.LoanService;
import org.junit.Test;

/**
 * Created on 17/10/21 15:09 星期六.
 *
 * @author sd
 */
public class TestCommon extends MongoBootRunner {
    @Test
    public void test1() {
        try {
            DbSessionFactory defaultSF = mongoControl.getSessionFactory("default");
            DbSession session = defaultSF.getCurrentSession();
            LoanService service = defaultSF.createService(LoanService.class);
            service.buyLoan();
            MongoAdapter mongoAdapter = defaultSF.getNativeObj();
            System.out.println(session);
            System.out.println(mongoAdapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
        MongoAdapter single = ToolMongo.getSingle();
        long l = single.insertOne(buildNewModel());
        System.out.println(l);
    }
}