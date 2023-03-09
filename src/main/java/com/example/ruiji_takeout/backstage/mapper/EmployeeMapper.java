package com.example.ruiji_takeout.backstage.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ruiji_takeout.backstage.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Jmd
 * @create 2023-03-2023/3/2-22:36
 * @Description：
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
