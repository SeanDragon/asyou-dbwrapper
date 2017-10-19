package org.asyou.db.tool;

import org.asyou.sequoia.dao.SequoiaAdapter;
import org.asyou.sequoia.exception.SequoiaAdapterException;

/**
 * 巨衫数据库的集成类
 *
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:31
 */
public class ToolSequoiadb implements ToolDb{

    private SequoiaAdapter adapter;

    public ToolSequoiadb(SequoiaAdapter sequoiaAdapter) {
        if (adapter == null) {
            refresh(sequoiaAdapter);
        }
    }

    public void refresh(SequoiaAdapter sequoiaAdapter) {
        adapter = sequoiaAdapter;
    }


    {
        try {


            adapter.insert().insertOneT("");
            adapter.update().updateOneT("","");

            adapter.delete("").deleteOneT();
            adapter.count("").getCount();
            adapter.total("").sum("");
            adapter.findMany("");
            adapter.findOne("");


        } catch (SequoiaAdapterException e) {
            e.printStackTrace();
        }
    }
}
