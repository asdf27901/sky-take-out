package com.sky.controller.admin;

import com.sky.annotation.validation.File;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Validated
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传接口")
    public Result<String> uploadFile(
            @ApiParam("需要上传的文件")
            @File(maxSize = 5, allowFileTypes = "jpg")
            MultipartFile file
    ) {
        String path = aliOssUtil.upload(file);
        return Result.success(path);
    }

}
