package org.asyou.db.type;

import java.util.Date;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 14:48
 */
public class FromToDate {

    private String fieldName;
    private Date from;
    private Date to;

    public String getFieldName() {
        return fieldName;
    }

    public FromToDate setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public Date getFrom() {
        return from;
    }

    public FromToDate setFrom(Date from) {
        this.from = from;
        return this;
    }

    public Date getTo() {
        return to;
    }

    public FromToDate setTo(Date to) {
        this.to = to;
        return this;
    }
}
