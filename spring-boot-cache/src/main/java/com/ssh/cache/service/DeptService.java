package com.ssh.cache.service;

import com.ssh.cache.bean.Department;
import com.ssh.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@Service
public class DeptService {
    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    RedisCacheManager cacheManager;

//    @Cacheable(value = "dept",key = "#id")
//    public Department deptById(Integer id){
//        System.out.println("部门号："+id);
//        Department department=departmentMapper.getById(id);
//        return  department;
//    }


    //使用缓存管理器得到缓存，进行api调用
    public Department deptById(Integer id){
        System.out.println("部门号："+id);
        Department department=departmentMapper.getById(id);

        //获取缓存，操作缓存
        Cache cache=cacheManager.getCache("dept");
        cache.put("dept:1",department);
        return  department;
    }
}
