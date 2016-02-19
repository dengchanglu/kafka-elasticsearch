import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;

/**
 * Created by perfection on 15-11-30.
 */
public class utilsTest {
    public static void main(String[] args) {
//        Map map = new HashMap<>();
//        map.put("index","nihao");
//        map.put("type","靠");
//        String mapStr = new String();
//        mapStr = map.toString();
//        String jsonStr = JSON.toJSONString(map);
//        System.out.println(jsonStr);
//        jsonStr = "{tit:123,qwe:123,asd:789}";
//        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//        Map map1 = (Map)jsonObject;
//        System.out.println(map1.toString());
        Jedis jedis = new Jedis("192.168.1.32");
        System.out.println("start");
        System.out.println("success " + jedis.ping());
//        jedis.lpush("tutorial-list", "Redis");
//        jedis.lpush("tutorial-list", "Mongodb");
//        jedis.lpush("tutorial-list", "Mysql");
        // Get the stored data and print it
        List<String> list = jedis.lrange("tutorial-list", 0, 17);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Stored string in redis:" + (i + 1) + ": " + list.get(i));
        }
        Iterator iterator = jedis.keys("*").iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
        ;
    }
}
