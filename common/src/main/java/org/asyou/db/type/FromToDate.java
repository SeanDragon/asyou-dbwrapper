package org.asyou.db.type;

import pro.tools.time.DatePlus;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:48
 */
public class FromToDate implements java.io.Serializable{

    private String fieldName;
    private DatePlus from;
    private DatePlus to;
    private boolean isShort;

    public static FromToDate buildShort(String fieldName) {
        return new FromToDate().setFieldName(fieldName).setShort(true);
    }

    public static FromToDate buildShort(String fieldName, DatePlus from, DatePlus to) {
        return new FromToDate(fieldName, from, to, true);
    }

    public static FromToDate buildLong(String fieldName) {
        return new FromToDate().setFieldName(fieldName);
    }

    public static FromToDate buildLong(String fieldName, DatePlus from, DatePlus to) {
        return new FromToDate(fieldName, from, to, false).setShort(false);
    }

    public FromToDate() {
    }

    private FromToDate(String fieldName, DatePlus from, DatePlus to, boolean isShort) {
        this.fieldName = fieldName;
        this.from = from;
        this.to = to;
        this.isShort = isShort;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FromToDate setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public DatePlus getFrom() {
        return from;
    }

    public FromToDate setFrom(DatePlus from) {
        this.from = from;
        return this;
    }

    public DatePlus getTo() {
        return to;
    }

    public FromToDate setTo(DatePlus to) {
        this.to = to;
        return this;
    }

    public boolean isShort() {
        return isShort;
    }

    public FromToDate setShort(boolean aShort) {
        isShort = aShort;
        return this;
    }
}
