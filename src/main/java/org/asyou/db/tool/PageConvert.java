package org.asyou.db.tool;

import org.asyou.db.type.PageData;
import org.asyou.mongo.Page;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-20 15:12
 */
public class PageConvert {
    public static <T> PageData<T> page2pageData4mongo(Page<T> page){
        return new PageData<>(page.getPageIndex(), page.getPageSize(), page.getTotalCount(), page.getList());
    }
}
