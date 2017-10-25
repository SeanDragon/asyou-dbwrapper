package org.asyou.db.type;

import org.asyou.db.tool.ToolPageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 规范化的分页对象
 *
 * @author SeanDragon Create By 2017-06-13 14:23
 */
public class PageData<T> implements Serializable {

    private int pageIndex;
    private int pageSize;
    private int perPageCount;
    private long totalCount;
    private PageInfo pageInfo;
    private List<T> list;

    public static <T> PageData<T> getEmpty() {
        return new PageData<>(0, ToolPageInfo.DEFAULT_SIZE, 0);
    }

    /**
     * 存放total数值
     */
    private Map totals;

    public int getPageIndex() {
        return pageIndex;
    }

    public PageData setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageData setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPerPageCount() {
        return perPageCount;
    }

    public PageData setPerPageCount(int perPageCount) {
        this.perPageCount = perPageCount;
        return this;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public PageData setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public PageData setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public List<T> getList() {
        return list;
    }

    public PageData setList(List<T> list) {
        this.list = list;
        return this;
    }

    public PageData(int pageIndex, int perPageCount, long totalCount) {
        this.pageIndex = pageIndex;
        this.perPageCount = perPageCount;
        this.totalCount = totalCount;
        this.count();
    }

    public PageData(int pageIndex, int perPageCount, long totalCount, List<T> list) {
        this.pageIndex = pageIndex;
        this.perPageCount = perPageCount;
        this.totalCount = totalCount;
        this.list = list;
        this.count();
    }

    private void count() {
        int mod = (int) (this.totalCount % (long) this.perPageCount);
        if (mod == 0) {
            this.pageSize = (int) (this.totalCount / (long) this.perPageCount);
        } else {
            this.pageSize = (int) (this.totalCount / (long) this.perPageCount) + 1;
        }

        if (this.pageIndex < 0) {
            this.pageIndex = 0;
        }
    }

    public PageData firstPage() {
        this.pageIndex = 0;
        this.count();
        return this;
    }

    public PageData lastPage() {
        this.pageIndex = this.pageSize - 1;
        this.count();
        return this;
    }

    public PageData nextPage() {
        ++this.pageIndex;
        this.count();
        return this;
    }

    public PageData prePage() {
        --this.pageIndex;
        this.count();
        return this;
    }

    public PageData gotoPage(int pageIndex) {
        this.pageIndex = pageIndex;
        this.count();
        return this;
    }

    public Map getTotals() {
        return totals;
    }

    public PageData setTotals(Map totals) {
        this.totals = totals;
        return this;
    }
}