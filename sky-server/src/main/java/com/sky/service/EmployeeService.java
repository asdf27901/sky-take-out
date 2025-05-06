package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    boolean save(EmployeeDTO employeeDTO);

    PageResult<Employee> listEmployee(EmployeePageQueryDTO employeePageQueryDTO);

    void changeEmployeeStatus(Integer status, Long id);


    Employee getEmployeeById(Long id);

    void updateEmployee(EmployeeDTO employeeDTO);
}
