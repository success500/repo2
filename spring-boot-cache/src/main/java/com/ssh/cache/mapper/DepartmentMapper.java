package com.ssh.cache.mapper;

import com.ssh.cache.bean.Department;
import org.apache.ibatis.annotations.Select;

public interface DepartmentMapper {

    @Select("select * from department where id=#{id}")
    Department getById(Integer id);
}
