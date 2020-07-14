package com.ssh.cache.controller;

import com.ssh.cache.bean.Employee;
import com.ssh.cache.mapper.EmployeeMapper;
import com.ssh.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
   EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee  empById(@PathVariable("id") Integer id){
        Employee employee=employeeService.getEmp(id);
        return  employee;
    }


    @GetMapping("/emp")
    public Employee updateEmployee(Employee employee){
        Employee emp=employeeService.updateEmp(employee);
        return emp;
    }


    @GetMapping("/delete")
    public String deleteEmployee(Integer id){
        employeeService.deleteEmpTest(id);
        return "success";
    }

    @GetMapping("/emp/lastName/{lastName}")
    public Employee getEmpByLastName(@PathVariable("lastName") String lastName){
        return employeeService.getEmpByLastName(lastName);
    }
}
