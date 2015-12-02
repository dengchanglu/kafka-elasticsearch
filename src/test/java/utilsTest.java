

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.mongodb.util.JSONSerializers;
import org.elasticsearch.index.mapper.object.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by perfection on 15-11-30.
 */
public class utilsTest {
    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put("index","nihao");
        map.put("type","靠");
        String mapStr = new String();
        mapStr = map.toString();
        String jsonStr = JSON.toJSONString(map);
        System.out.println(jsonStr);
        jsonStr = "{tit:123,qwe:123,asd:789}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Map map1 = (Map)jsonObject;
        System.out.println(map1.toString());

    }
}
