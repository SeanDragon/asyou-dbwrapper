package org.asyou.sequoia;

import org.asyou.db.exception.DbException;
import org.asyou.db.manager.SequoiaControl;
import org.asyou.db.session.DbSession;
import org.asyou.db.sessionfactory.DbSessionFactory;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.junit.Test;
import pro.tools.data.decimal.Decimal;
import pro.tools.data.text.ToolJson;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-23 9:34
 */
public class TestCommon extends SequoiaBootRunner {
    @Test
    public void testInsert() {
        try {
            DbSessionFactory sessionFactory = SequoiaControl.getSingle().getSessionFactory("default");
            DbSession session = sessionFactory.getCurrentSession();
            SequoiaAdapter adapter = session.getNativeObj();
            boolean b = session.insertOne(buildNewModel());
            System.out.println(b);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static final ExecutorService exec = Executors.newFixedThreadPool(100);

    @Test
    public void testFind() {
        for (int i = 0; i < 100; i++) {
            exec.execute(() -> {
                //TODO
            });
        }
    }

    @Test
    public void testDate() {
        Date date = new Date();
        String s = ToolJson.anyToJson(date);
        System.out.println(s);
    }

    @Test
    public void testDecimal1() {
        Decimal.setDefaultMathContext(new MathContext(32, RoundingMode.HALF_UP));
        System.out.println(Decimal.instance(0.0D).sub(0.0D).moneyValue());
    }
}
