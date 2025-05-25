package com.sky.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.BaiduProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Slf4j
public class DistanceUtil {

    private final String ak;
    private final String distanceUrl;
    private final String locationUrl;
    @Value("${sky.shop.location.lng}")
    private String originLng;
    @Value("${sky.shop.location.lat}")
    private String originLat;

    private DistanceUtil(BaiduProperties baiduProperties) {
        this.ak = baiduProperties.getAk();
        this.distanceUrl = baiduProperties.getDistanceUrl();
        this.locationUrl = baiduProperties.getLocationUrl();
    }

    public String getAddressLocation(String address) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("ak", ak);
        queryParams.put("address", address);
        queryParams.put("output", "json");
        String res = HttpClientUtil.doGet(locationUrl, queryParams);
        JSONObject jsonObject = JSON.parseObject(res);
        String lat = jsonObject.getJSONObject("result")
                .getJSONObject("location")
                .getString("lat");
        String lng = jsonObject.getJSONObject("result")
                .getJSONObject("location")
                .getString("lng");
        return lat + "," + lng;
    }

    public Double getDistance(String destination) {
        String destinationCoordinate = getAddressLocation(destination);
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("ak", ak);
        queryParams.put("origin", originLat + "," + originLng);
        queryParams.put("destination", destinationCoordinate);
        queryParams.put("steps_info", "0");
        queryParams.put("riding_type", "1");
        String res = HttpClientUtil.doGet(distanceUrl, queryParams);
        JSONObject jsonObject = JSON.parseObject(res);
        log.info("距离信息：{}", jsonObject);
        int status = jsonObject.getIntValue("status");
        if ( status == 2002) {
            throw new RuntimeException("距离超出范围");
        }
        if (status == 1000) {
            // 就是原地
            return 0d;
        }
        return jsonObject.getJSONObject("result")
                .getJSONArray("routes")
                .getJSONObject(0)
                .getDouble("distance");
    }
}
