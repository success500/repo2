package com.ssh.cache.controller;

import com.ssh.cache.bean.Department;
import com.ssh.cache.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeptController {

    @Autowired
    DeptService deptService;

    @GetMapping("/dept/{id}")
    public Department deptById(@PathVariable("id") Integer id){
       return  deptService.deptById(id);
    }
}
