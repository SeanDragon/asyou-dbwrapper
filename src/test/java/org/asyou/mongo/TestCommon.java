package org.asyou.mongo;

import org.asyou.db.exception.DbException;
import org.asyou.db.session.DbSession;
import org.asyou.db.session_factory.DbSessionFactory;
import org.asyou.db.tool.ToolMongo;
import org.asyou.db.type.SearchParam;
import org.asyou.mongo.dao.MongoAdapter;
import org.asyou.sequoia.model.Matchers;
import org.asyou.service.LoanService;
import org.bson.BSONObject;
import org.junit.Test;
import pro.tools.data.text.ToolJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 17/10/21 15:09 星期六.
 *
 * @author sd
 */
public class TestCommon extends MongoBootRunner {
    @Test
    public void test1() {
        try {
            DbSessionFactory defaultSF = mongoControl.getSessionFactory("default");
            DbSession session = defaultSF.getCurrentSession();
            LoanService service = defaultSF.createService(LoanService.class);
            service.buyLoan();
            MongoAdapter mongoAdapter = defaultSF.getNativeObj();
            System.out.println(session);
            System.out.println(mongoAdapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
        MongoAdapter single = ToolMongo.getSingle();
        long l = single.insertOne(buildNewModel());
        System.out.println(l);
    }

    @Test
    public void testMongoQuery() {
        List<SearchParam> searchParamList = new ArrayList<>();
        SearchParam<String> searchParam = new SearchParam<String>("id", String.class, "661098995501863");
        searchParamList.add(searchParam);

        StringBuilder iQuery = new StringBuilder("{");
        searchParamList.forEach(one -> {
            Class<?> oneClazz;
            try {
                oneClazz = Class.forName(one.getClazzName());
            } catch (ClassNotFoundException e) {
                return;
            }
            String listAttrStr = list2bson(one.getValues(), oneClazz);
            String oneIQuery = "'" + one.getFieldName() + "':{'$in':" + listAttrStr + "}";
            iQuery.append(oneIQuery).append(",");
        });
        iQuery.deleteCharAt(iQuery.length() - 1);
        iQuery.append("}");
        System.out.println(iQuery);
        //{'id':{'$in':["661098995501863"]}}
    }

    @Test
    public void testSequoiaQuery() {
        BSONObject id = Matchers.in("id", "661098995501863");
        System.out.println(id);
        //{ "id" : { "$in" : [ "661098995501863"]}}
    }


    private static <T> String list2bson(final List objects, Class<T> clazz) {
        return list2str("[", "]", ",", objects, clazz);
    }

    private static <T> String list2str(final String start, final String end, final String sep, final List objects, Class<T> clazz) {
        StringBuilder stringBuilder = new StringBuilder(start);

        objects.forEach(object -> {
            if (object instanceof String) {
                stringBuilder.append("\"").append(object.toString()).append("\"").append(sep);
            } else if (object.getClass().equals(clazz)) {
                stringBuilder.append(clazz.cast(object)).append(sep);
            } else {
                T jsonToAny = ToolJson.jsonToAny(ToolJson.anyToJson(object), clazz);
                stringBuilder.append(clazz.cast(jsonToAny)).append(sep);
            }
        });
        int i = stringBuilder.lastIndexOf(sep);
        return stringBuilder.substring(0, i) + end;
    }
}