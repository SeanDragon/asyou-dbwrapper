package org.asyou.db.tool;

import org.asyou.db.annotation.PrimaryKey;

import java.lang.reflect.Field;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-20 15:26
 */
public class ToolPrimaryKey {
    public static <T> T getNewPrimaryKeyModel(T data) throws IllegalAccessException, InstantiationException {
        T queue = (T) data.getClass().newInstance();
        for (Field field : data.getClass().getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                field.set(queue, field.get(data));
            }
            field.setAccessible(accessible);
        }
        return queue;
    }
}
