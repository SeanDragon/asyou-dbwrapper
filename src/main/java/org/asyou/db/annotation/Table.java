package org.asyou.db.annotation;

import java.lang.annotation.*;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 15:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
    String value();
}
