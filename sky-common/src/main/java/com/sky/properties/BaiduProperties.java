package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.baidu")
@Data
public class BaiduProperties {
    private String ak;
    private String locationUrl;
    private String distanceUrl;
}
