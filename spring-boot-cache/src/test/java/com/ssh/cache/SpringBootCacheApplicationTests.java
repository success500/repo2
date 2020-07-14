package com.ssh.cache;

import com.ssh.cache.bean.Employee;
import com.ssh.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.xml.ws.soap.Addressing;

@SpringBootTest
class SpringBootCacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;//它的k-V是操作字符串的

    @Autowired
    RedisTemplate redisTemplate;//它的k-V是操作对象的

    @Autowired
    RedisTemplate<Object, Employee>  myRedisTemplate;

    /**
     * Redis常见的五大类型：
     *  String（字符串）、List（列表）、Set（集合）、Hash（散列）、ZSet（有序集合）
     *         stringRedisTemplate.opsForHash();
     *         stringRedisTemplate.opsForSet();
     *         stringRedisTemplate.opsForZSet();
     *         stringRedisTemplate.opsForList();
     *         stringRedisTemplate.opsForValue();
     *
     */

    @Test
    public void test1(){
       //给redis中保存数据
       //stringRedisTemplate.opsForValue().append("msg","hello");
//        String value=stringRedisTemplate.opsForValue().get("msg");
//        System.out.println(value);
        stringRedisTemplate.opsForList().leftPush("myList","1");
        stringRedisTemplate.opsForList().leftPush("myList","2");

    }

    //测试保存对象
    @Test
    public void test2(){
        Employee employee=employeeMapper.getById(1);
        //默认如果保存对象，使用jdk序列化机制，序列化的数据保存在redis中
        //redisTemplate.opsForValue().set("emp",employee);
        /**
         * 1、将数据已json的方式保存
         *           1)、自己将对象转为json
         *           2)、redisTemplate默认序列化规则
         *
         */
        myRedisTemplate.opsForValue().set("emp",employee);


    }

    @Test
    void contextLoads() {
       Employee employee= employeeMapper.getById(1);
        System.out.println(employee);
    }

}
