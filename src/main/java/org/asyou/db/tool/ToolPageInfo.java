package org.asyou.db.tool;

import com.google.common.collect.Maps;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.PageInfo;

/**
 * Created By SeanDragon
 * At 2016年11月25日, PM 01:26:21
 */
public class ToolPageInfo {

    public static final int DEFAULT_SIZE = 20;

    public static PageInfo valid(PageInfo pageInfo) {
        if (pageInfo == null) {
            return new PageInfo();
        }

        if (pageInfo.getSortMap() == null) {
            pageInfo.setSortMap(Maps.newHashMap());
        }

        if (pageInfo.getPageIndex() != 0) {
            pageInfo.setPageIndex(pageInfo.getPageIndex() - 1);
        }

        if (pageInfo.getPageSize() == 0) {
            pageInfo.setPageSize(DEFAULT_SIZE);
        }

        if(pageInfo.getBoolParams()==null) {
            pageInfo.setBoolParams(BoolParams.buildAnd());
        }
        return pageInfo;
    }
}
