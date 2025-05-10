package com.sky.validator;

import com.sky.annotation.validation.File;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {

    private long maxSize;
    private String[] allowFileTypes;

    @Override
    public void initialize(File constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.allowFileTypes = constraintAnnotation.allowFileTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {

        constraintValidatorContext.disableDefaultConstraintViolation();

        // 判断文件大小
        if (file.getSize() > maxSize * 1024 * 1024) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("文件超出大小限制, 最大为: " + maxSize + "MB")
                    .addConstraintViolation();
            return false;
        }

        // 判断是否是空文件
        if (file.isEmpty()) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("不允许上传空文件")
                    .addConstraintViolation();
            return false;
        }

        // 判断文件类型
        if (allowFileTypes.length != 0) {
            try {
                String fileType = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                boolean match = Arrays.stream(allowFileTypes).anyMatch(s -> s.equalsIgnoreCase(fileType));
                if (!match) {
                    throw new RuntimeException();
                }
            }catch (Exception e) {
                constraintValidatorContext.buildConstraintViolationWithTemplate("不支持的文件类型")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
