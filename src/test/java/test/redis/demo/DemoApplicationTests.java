package test.redis.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import test.redis.demo.utils.RedisUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    @Test
    void testsortedset(){
        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet();
        ZSetOperations.TypedTuple<Object> answerVoInZset1 = new DefaultTypedTuple("id", 11D);
        ZSetOperations.TypedTuple<Object> answerVoInZset2 = new DefaultTypedTuple("name", 22D);
        ZSetOperations.TypedTuple<Object> answerVoInZset3 = new DefaultTypedTuple("age", 33D);
        set.add(answerVoInZset1);
        set.add(answerVoInZset2);
        set.add(answerVoInZset3);
        redisTemplate.opsForZSet().add("set", set);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 增量id的分数
        redisTemplate.opsForZSet().incrementScore("set","id",6D);

        Set<ZSetOperations.TypedTuple<Object>> set1 = redisTemplate.opsForZSet().rangeWithScores("set", 0L, -1L);

        for (ZSetOperations.TypedTuple<Object> objectTypedTuple : set1) {
            Double score = objectTypedTuple.getScore();
            Object value = objectTypedTuple.getValue();
            System.out.println(score+"------"+value);
        }
    }


    @Test
    void testhash(){
        redisUtil.hset("user","id",12);
        redisUtil.hset("user","name","hash");
        redisUtil.hset("user","age",23);

        redisUtil.hincr("user","id",1);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<Object, Object> user = redisUtil.hmget("user");
        System.out.println(user);

    }
    @Test
    void testlistrange(){
        List<Object> list = redisUtil.lGet("list", 0, -1);
        System.out.println(list);

    }

    @Test
    void  testlist() throws InterruptedException {
        Map map = new HashMap();
        map.put("id","1");
        map.put("name","ceshi");
        Map map1 = new HashMap();
        map1.put("id","2");
        map1.put("name","mmp");
        redisUtil.lSet("list",map);
        redisUtil.lSet("list",map1);

        TimeUnit.SECONDS.sleep(2);


        Object list = redisUtil.lPop("list", 0L);

        System.out.println(list);
    }

    @Test
    void contextLoads() throws InterruptedException {
        Map map = new HashMap();
        map.put("id","1");
        map.put("name","ceshi");
        boolean q = redisUtil.set("q", map);
        TimeUnit.SECONDS.sleep(2);

        Map q1 = (Map) redisUtil.get("q");
        System.out.println(map);
    }

    @Test
    void delete(){
//        redisUtil.del("set");
        redisTemplate.opsForZSet().removeRange("set",0L,-1L);
    }

}
