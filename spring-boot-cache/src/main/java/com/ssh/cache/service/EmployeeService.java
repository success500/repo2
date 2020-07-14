package com.ssh.cache.service;

import com.ssh.cache.bean.Employee;
import com.ssh.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
@CacheConfig(cacheNames = "emp")//抽取缓存中的公共配置
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * @Cacheable 将方法的运行结果进行缓存，以后再要有相同的数据，直接从缓存中获取，不用再调用方法
     * CacheManager管理多个Cache组件，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字，
     *          比如说：emp组件
     *
     *  几个属性：
     *   cacheNames/value:指定组件的名字，只能使用其中的一个，比如说emp组件，组件名可以有多个,cacheNames ={"emp","employee"}
     *                    将方法返回的结果放在缓存中，是数组的方式，可以指定多个缓存
     *   key:缓存数据使用的key;用它来指定emp的Cache组件中的Entry<K,V>,因为emp组件中有多个Entry，所以需要key来指定，获取Entry中的value值，
     *       默认使用的是方法的参数的值，也就是id的值，例如id=1=key-方法的返回值参数,
     *       也可以编写SpEL：#id；参数id的值，等同于 #a0 #p0 #root.args[0]
     *       SpEl表达式：key="#root.methodName+'['+#id+']'",== key=getEmp[2]
     *
     *   keyGenerator:key的生成器，可以自己指定key的生成器的组件的id
     *         key/keyGenerator:两者只能选择一个使用
     *         keyGenerator = "myKeyGenerator";在MyCacheConfig中key=key=getEmp[[2]]
     *
     *    CacheManager：指定缓存管理器或者cacheResolver指定获取解析器，
     *            例如：有两个缓冲管理器，一个是currentManager，另一个是Redis，但是他们都有emp组件，必须指定一个使用，
     *    condition:指定符合条件的情况下才缓存    condition = "#id>0" id>0的情况下才会缓存
     *             condition = "#a0>1"第一个参数的值大于1才能进行缓存
     *
     *    unless：否定缓存，当unless的指定的条件为true的时候，方法的返回值就不会缓存，可以获取到结果进行判断
     *          例如unless = "#result==null",也就是结果不为null的时候才会缓存
     *          unless = "#a0==2"如果第一个参数的之后是2，结果就不缓存
     *
     *    sync:是否使用异步模式
     *
     *
     * Cache自动配置原理：
     *         1、自动配置类：CacheAutoConfiguration
     *         2、缓存的配置类
     *         org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration
     *         org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *
     *      3、默认配置类生效，只有一个：
     *       SimpleCacheConfiguration matched:
     *       - Cache org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration automatic cache type (CacheCondition)
     *       - @ConditionalOnMissingBean (types: org.springframework.cache.CacheManager; SearchStrategy: all) did not find any beans (OnBeanCondition)
     *
     *       4、该容器中注册一个CacheManager：ConcurrentMapCacheManager
     *       5、可以获取和创建ConcurrentMapCache类型的缓存组件，他们的作用将数据保存在ConcurrentMap中
     *
     *    运行的流程：
     *   @Cacheable：
     *   1、方法运行之前，先去查询Cache（缓存组件），按照CacheName指定的名字获取,在ConcurrentMapCacheManager类中：
     *     (CacheManager先获取相应的缓存)，第一次获取缓存如果没有Cache组件会自动创建
     *    public Cache getCache(String name) { //name="emp"
     * 		Cache cache = this.cacheMap.get(name);//name="emp"
     * 		if (cache == null && this.dynamic) {
     * 			synchronized (this.cacheMap) {
     * 				cache = this.cacheMap.get(name);
     * 				if (cache == null) {
     * 					cache = createConcurrentMapCache(name);//自动创建Cache组件
     * 					this.cacheMap.put(name, cache);
     *                }
     *            }
     *        }
     * 		return cache;
     *    }
     *
     *    2、去Cache缓存中查找缓存的内容，使用一个key，默认就是方法的参数：在ConcurrentMapCache类中的lookup方法：
     *    	protected Object lookup(Object key) {//key=id=1
     * 		return this.store.get(key);
     *        }
     *      key是按照某种策略生成的，默认使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key
     *             SimpleKeyGenerator生成key的默认策略：
     *              如果没有参数：key=new SimpleKey()
     *              如果有一个参数：key=参数的值
     *              如果有多个参数：key=new SimpleKeyGenerator（params）
     *
     *    3、没有查到缓存的时候就调用目标方法
     *    4、将目标方法返回的结果，放进缓存中
     *
     *    @Cacheable 标注的方法执行之前先来检查缓存中有没有这个数据，默认按照参数的值作为key去查缓存
     *    如果没有就运行方法并将结果放到缓存中,以后再来调用直接使用缓存中的数据
     *
     *    核心：
     *      1）、使用CacheManager【ConcurrentMapCacheManager】按照名字等到Cache【ConcurrentMapCache】组件
     *      2）、key使用KeyGenerator生成的，默认是SimpleKeyGenerator
     * @param id
     * @return
     */
    @Cacheable(value ="emp",key = "#id")
    public Employee getEmp(Integer id){
        System.out.println("第"+id+"员工");
        Employee employee=employeeMapper.getById(id);
        return employee;
    }

    /**
     * @CachePut ：即调用方法，又更新数据缓存
     * 修改了数据库中的数据，又更新了缓存
     * 运行的时机：
     *   1、先调用目标方法
     *   2、将目标方法结果缓存起来
     *
     *   测试步骤：
     *   1、查询2号员工，查询的结果会放在缓存里面
     *     key: 1  value: lastName :张三
     *
     *   2、以后查询2号还是之前的结果
     *   3、更新2号员工
     *       将方法返回的值也放进缓存里面
     *       key:传入的employee对象，值：返回的employee对象
     *    4、查询2号员工
     *        当@CachePut里面没有写key的时候，是没有写入到缓存里面
     *        应该是更新后的员工：
     *         key = "#employee.id"：使用传入的参数的员工id
     *         等同于key = "#result.id"：使用返回后的id
     *
     *         更新在地址栏上输入的http://localhost:8080/emp?id=2&lastName=董万均&email=1554289942
     *         是无法将更新的数据写到EmployeeController类的更新方法中
     *
     *         为什么是没有更新前的？【2号员工没有在缓存中更新】
     *         @CachePut是在方法执行之后执行
     *         @Cacheable是在方法执行之前执行
     * @param employee
     * @return
     */

    @CachePut(value = "emp",key = "#employee.id")
    public Employee updateEmp(Employee employee){
        System.out.println("更新缓存"+employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     * @CacheEvict :缓存清除
     * value：指定要清除的缓存组件
     * key:指定要清除缓存组件中的那些数据
     * allEntries=true:清除这个缓存组件中的所有的数据
     * beforeInvocation =false(默认)：表示缓存的清除在方法执行之后执行，如果出现异常，就不清除缓存
     *                  true：表示缓存的清除在方法执行之前执行，不管方法有没有异常，都会清除缓存
     * @param id
     */
   @CacheEvict(value = "emp",key = "#id",beforeInvocation = true)//和@Cacheable的value只能有一个缓存组件，必须一样
    public void deleteEmpTest(Integer id){
        System.out.println("删除缓存"+id);
        //employeeMapper.deleteEmp(id);不加这个就没有删除数据库的数据
       int i=12/0;
    }

    //@Caching定义复杂组件
    @Caching(
            cacheable = {
                    @Cacheable(/*value = "emp"*/key = "#lastName")
            },
            put = {
                    @CachePut(/*value = "emp"*/key = "#result.id"),
                    @CachePut(/*value = "emp"*/key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
       return employeeMapper.getByLastName(lastName);
    }

}
