package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result<?> save(@RequestBody @Valid EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        return employeeService.save(employeeDTO) ? Result.success(null) : Result.error("添加失败");
    }

    @GetMapping("/page")
    @ApiOperation("获取员工列表")
    public Result<PageResult<Employee>> listEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult<Employee> employeePageResult = employeeService.listEmployee(employeePageQueryDTO);
        return Result.success(employeePageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改员工状态")
    public Result<?> changeEmployeeStatus(
            @ApiParam(value = "员工状态", required = true)
            @PathVariable
            @Range(max = 1L, message = "status不合法")
            @NotNull(message = "status不能为空")
            Integer status,

            @RequestParam
            @NotNull(message = "员工ID不能为空")
            @ApiParam(value = "员工ID", required = true)
            Long id
    ){
        log.info("员工修改状态，id：{}，状态值：{}", id, status);
        employeeService.changeEmployeeStatus(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工信息")
    public Result<Employee> getEmployeeById(
            @PathVariable
            @ApiParam(value = "员工ID", required = true)
            @NotNull(message = "员工ID不能为空")
            Long id
    ){
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }
}
