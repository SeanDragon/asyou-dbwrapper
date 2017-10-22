package org.asyou.db.tool;

import org.asyou.db.annotation.Table;
import pro.tools.data.text.ToolStr;

/**
 * Created on 17/10/22 19:52 星期日.
 *
 * @author sd
 */
public class ToolTable {
    public static String getName(Class clazz) {
        String cn;
        Table c = (Table) clazz.getDeclaredAnnotation(Table.class);
        if (c != null) {
            if (ToolStr.notBlank(c.value())) {
                cn = c.value();
            } else {
                cn = clazz.getSimpleName();
            }
        } else {
            cn = clazz.getSimpleName();
        }
        return cn;
    }

    public static <T> String getName(T data) {
        return getName(data.getClass());
    }
}
