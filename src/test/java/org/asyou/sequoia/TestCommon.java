package org.asyou.sequoia;

import org.asyou.db.exception.DbException;
import org.asyou.db.manager.SequoiaControl;
import org.asyou.db.session.DbSession;
import org.asyou.db.sessionfactory.DbSessionFactory;
import org.asyou.sequoia.dao.SequoiaAdapter;
import org.junit.Test;

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
}