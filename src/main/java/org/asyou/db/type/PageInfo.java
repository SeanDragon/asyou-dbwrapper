package org.asyou.db.type;

import com.google.common.base.MoreObjects;
import org.asyou.db.tool.ToolPageInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页用
 *
 * @author SeanDragon
 */
public class PageInfo implements Serializable {

    public PageInfo() {
        this.pageIndex = 0;
        this.pageSize = ToolPageInfo.DEFAULT_SIZE;
        this.sortMap = new HashMap<>();
        this.boolParams = BoolParams.buildAnd();
    }

    /**
     * 页码（就是第几页）
     */
    private int pageIndex;
    /**
     * 每页显示的数据条数
     */
    private int pageSize;
    /**
     * 排序用 K 字段名称 V 1 正序 -1 倒序
     */
    private Map<String, Integer> sortMap;
    /**
     * 日期范围查询
     */
    private FromToDate fromToDate;
    /**
     * 不由外部修改
     */
    private boolean needFromToDate;
    /**
     * 查询参数
     * contain 实体的每个属性之间是模糊查询
     * or 实体的每个属性之间的或的关系
     */
    private BoolParams boolParams;

    /**
     * b-jui orders 暂时冗余 后期看情况去除！
     */
    private String orders;

    public int getPageIndex() {
        return pageIndex;
    }

    public PageInfo setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageInfo setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Map<String, Integer> getSortMap() {
        return sortMap;
    }

    public PageInfo setSortMap(Map<String, Integer> sortMap) {
        this.sortMap = sortMap;
        return this;
    }

    public FromToDate getFromToDate() {
        return fromToDate;
    }

    public PageInfo setFromToDate(FromToDate fromToDate) {
        this.needFromToDate = fromToDate != null;
        this.fromToDate = fromToDate;
        return this;
    }

    public boolean isNeedFromToDate() {
        return needFromToDate;
    }

    public String getOrders() {
        return orders;
    }

    public PageInfo setOrders(String orders) {
        this.orders = orders;
        return this;
    }

    public BoolParams getBoolParams() {
        return boolParams;
    }

    public PageInfo setBoolParams(BoolParams boolParams) {
        this.boolParams = boolParams;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pageIndex", pageIndex)
                .add("pageSize", pageSize)
                .add("sortMap", sortMap)
                .add("fromToDate", fromToDate)
                .add("needFromToDate", needFromToDate)
                .add("boolParams", boolParams)
                .add("orders", orders)
                .toString();
    }

    public static PageInfo getMax() {
        return new PageInfo().
                setPageSize(Integer.MAX_VALUE);
    }
}
