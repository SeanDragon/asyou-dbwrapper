package org.asyou.db.type;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 多种并列查询条件
 * @author SeanDragon
 */
public class SearchParam<T> implements java.io.Serializable {
    private String fieldName;
    private String clazzName;
    private List<T> values;

    public SearchParam() {
    }

    public SearchParam(String fieldName, Class<T> clazz, List<T> values) {
        this.fieldName = fieldName;
        this.clazzName = clazz.getName();
        this.values = values;
    }

    public SearchParam(String fieldName, Class<T> clazz, T... values) {
        this.fieldName = fieldName;
        this.clazzName = clazz.getName();
        this.values = Lists.newArrayList(values);
    }

    public List getValues() {
        return values;
    }

    public SearchParam setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public SearchParam setValues(List values) {
        this.values = values;
        return this;
    }

    public SearchParam setValues(T... values) {
        this.values = Lists.newArrayList(values);
        return this;
    }

    public String getClazzName() {
        return clazzName;
    }

    public SearchParam setClazzName(String clazzName) {
        this.clazzName = clazzName;
        return this;
    }
}