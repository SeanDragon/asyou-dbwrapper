package org.asyou.db.tool;

import com.google.common.collect.Maps;
import org.asyou.db.type.BoolParams;
import org.asyou.db.type.FromToDate;
import org.asyou.db.type.PageInfo;
import pro.tools.time.DatePlus;
import pro.tools.time.DateType;

/**
 * Created By SeanDragon At 2016年11月25日, PM 01:26:21
 *
 * @author SeanDragon
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

        if (pageInfo.getBoolParams() == null) {
            pageInfo.setBoolParams(BoolParams.buildAnd());
        }

        if (pageInfo.getFromToDate() != null) {
            FromToDate fromToDate = pageInfo.getFromToDate();
            if (fromToDate.getFrom() == null) {
                fromToDate.setFrom(new DatePlus(1970, 1, 1));
            }
            if (fromToDate.getTo() == null) {
                fromToDate.setTo(new DatePlus().addDay(1).toMinDate(DateType.DAY));
            }
            pageInfo.setFromToDate(fromToDate).setNeedFromToDate(true);
        }
        return pageInfo;
    }
}