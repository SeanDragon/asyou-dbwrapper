package org.asyou.db.manager;

import org.asyou.sequoia.dao.SequoiaAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SeanDragon
 * <p>
 * Create By 2017-10-19 17:59
 */
public class SequoiaDbFactory {
    private static Map<String, SequoiaAdapter> sequoiaAdapterMap;

    public static void refresh(List<SequoiaAdapter> adapterList) {
        sequoiaAdapterMap = new HashMap<>(adapterList.size());
        adapterList.forEach(adapter -> {
            sequoiaAdapterMap.put(adapter.getId(), adapter);
        });
    }

    public static void add(String adapterId, SequoiaAdapter adapter, boolean replace) {
        boolean contains = sequoiaAdapterMap.containsKey(adapterId);
        if ((!contains) || (contains && replace)) {
            sequoiaAdapterMap.put(adapterId, adapter);
        }
    }

    public static SequoiaAdapter get(String adapterId) {
        if (!sequoiaAdapterMap.containsKey(adapterId)) {
            throw new NullPointerException("没有adapterId为" + adapterId + "的adapter");
        }
        return sequoiaAdapterMap.get(adapterId);
    }
}
