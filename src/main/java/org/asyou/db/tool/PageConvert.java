package org.asyou.db.tool;

import org.asyou.db.type.PageData;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-20 15:12
 */
public class PageConvert {
    public static <T> PageData<T> page2pageData4mongo(org.asyou.mongo.Page<T> page) {
        return new PageData<>(page.getPageIndex(), page.getPerPageCount(), page.getTotalCount(), page.getList());
    }

    public static <T> PageData<T> page2pageData4sequoia(org.asyou.sequoia.Page<T> page) {
        return new PageData<T>(page.getPageIndex(), page.getPerPageCount(), page.getTotalCount(), page.getList());
    }
}
