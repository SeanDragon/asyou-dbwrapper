package org.asyou.db.type;

import java.util.List;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-11-08 14:19
 */
public class FieldFilter {

    private List<String> descFieldNameList;
    private List<String> ascFieldNameList;

    public String[] getDescFieldNames() {
        return descFieldNameList.toArray(new String[]{});
    }

    public List<String> getDescFieldNameList() {
        return descFieldNameList;
    }

    public FieldFilter setDescFieldNameList(List<String> descFieldNameList) {
        this.descFieldNameList = descFieldNameList;
        return this;
    }

    public String[] getAscFieldNames() {
        return ascFieldNameList.toArray(new String[]{});
    }

    public List<String> getAscFieldNameList() {
        return ascFieldNameList;
    }

    public FieldFilter setAscFieldNameList(List<String> ascFieldNameList) {
        this.ascFieldNameList = ascFieldNameList;
        return this;
    }
}
