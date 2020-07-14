package com.ssh.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 一、搭建基本环境
 * 1、导入数据库文件，创建department和employee表
 * 2、创建JavaBEan封装数据
 * 3、整合Mybatis操作数据库
 *   1）、配置数据源信息
 *   2）、使用注解版Mybatis
 *      1）、@MapperScan指定所需要扫描的mapper接口所在包
 *  二、快速体验缓存
 *    步骤：
 *      1、开启基于注解的缓存@EnabledCaching
 *      2、标注缓存注解即可
 *         @Cacheable
 *         @CacheEvict
 *         @CachePut
 *
 * 默认使用的是ConcurrentMapCacheManager==ConcurrentMapCache，将数据报错到ConcurrentMap<Object,Object>
 * 开发使用缓存中间件：Redis、memcached、ehcache
 * 三、整合Redis作为缓存
 *  Redis是一个开源（BSD许可）的，内存的数据结构储存系统，它可以用作数据库、缓存和消息中间件
 *  1、安装Redis：使用docker，解决下载Redis镜像慢的方法：在镜像中国里面
 *        docker pull registry.docker-cn.com/library/redis
 *  2、引入Redis的starter
 *    <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-data-redis</artifactId>
 *     </dependency>
 *  3、配置redis
 *     spring.redis.host=192.168.2.179
 *   4、测试缓存
 *    原理：CacheManager==Cache缓存组件来实际给缓存中储存数据
 *     1）、引入Redis的starter，容器中报错的时候RedisCacheManager
 *     2)、RedisCacheManager帮助我们创建RedisCache来作为缓存组件，RedisCache通过操作redis缓存数据
 *     3）、默认保存数据K-V都是Object，利用序列化保存，如何保存为json
 *        1、引入redis的starter，CacheManager变为RedisCacheManager、
 *        2、默认创建RedisCacheManager操作redis的时候使用的是RedisTemplate<Object,Object>
 *        3、RedisTemplate<Object,Object>是默认使用jdk的序列化机制
 *     4）、自定义cacheManager
 */
@MapperScan("com.ssh.cache.mapper")
@SpringBootApplication
@EnableCaching
public class SpringBootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCacheApplication.class, args);
    }

}
