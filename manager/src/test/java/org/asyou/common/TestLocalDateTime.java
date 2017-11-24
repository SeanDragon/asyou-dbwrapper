package org.asyou.common;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-25 18:38
 */
public class TestLocalDateTime {
    @Test
    public void test1() {
        // DatePlus datePlus1 = new DatePlus();
        // DatePlus datePlus2 = new DatePlus().addDay(1).addHour(8);

        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime localDateTime2 = LocalDateTime.now().plusHours(8);

        System.out.println(localDateTime1);
        System.out.println(localDateTime2);

        long between = ChronoUnit.DAYS.between(localDateTime1.toLocalDate(), localDateTime2.toLocalDate());
        System.out.println(between);

        long between1 = ChronoUnit.NANOS.between(localDateTime1, localDateTime2);
        System.out.println(between1);
    }
}
