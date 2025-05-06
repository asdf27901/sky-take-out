package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.BusinessException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = DigestUtils.md5DigestAsHex(employeeLoginDTO.getPassword().getBytes());

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public boolean save(EmployeeDTO employeeDTO) {

        // 寻找当前的要添加的用户名是否已被占用
        Employee em = employeeMapper.getByUsername(employeeDTO.getUsername());
        if (em != null) {
            throw new BusinessException(employeeDTO.getUsername() + "用户名已被注册");
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        LocalDateTime now = LocalDateTime.now();

        employee.setCreateTime(now);
        employee.setUpdateTime(now);

        // 设置默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 数据库status默认状态为1

        // threadLocal获取当前登录用户
        Long empId = BaseContext.getCurrentId();
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        return employeeMapper.insertEmployee(employee) > 0;
    }

    @Override
    public PageResult<Employee> listEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        Page<Employee> employeePage = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())
                .doSelectPage(() -> employeeMapper.listEmployee(employeePageQueryDTO));
        return new PageResult<>(employeePage.getTotal(), employeePage.getResult(), employeePage.getPageSize(), employeePage.getPageNum());
    }

    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(s);
    }

}
