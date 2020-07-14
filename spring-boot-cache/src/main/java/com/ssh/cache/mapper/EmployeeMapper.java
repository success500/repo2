package com.ssh.cache.mapper;

import com.ssh.cache.bean.Employee;
import org.apache.ibatis.annotations.*;


public interface EmployeeMapper {

    @Select("select * from employee where id=#{id}")
    Employee getById(Integer id);

    @Update("update employee set lastName=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} where id=#{id}")
    void updateEmp(Employee employee);

    @Delete("delete from employee where id=#{id} ")
    void deleteEmp(Integer id);

    @Insert("insert into employee(lastName,email,gender,d_id) values(#{lastName},#{email},#{gender},#{dId})")
    void insertEmp(Employee employee);


    @Select("select * from employee where lastName=#{lastName}")
    Employee getByLastName(String lastName);
}
