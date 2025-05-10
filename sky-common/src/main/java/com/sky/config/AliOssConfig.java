package com.sky.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.CreateBucketRequest;
import com.sky.properties.AliOssProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliOssProperties.class)
public class AliOssConfig {

    @Bean
    public OSS createOssClient(AliOssProperties aliOssProperties) {
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        DefaultCredentialProvider credentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());

        OSS ossClient = OSSClientBuilder.create()
                .endpoint(aliOssProperties.getEndpoint())
                .region(aliOssProperties.getRegion())
                .credentialsProvider(credentialProvider)
                .build();

        // 判断存储空间是否存在
        String bucketName = aliOssProperties.getBucketName();
        boolean exist = ossClient.doesBucketExist(bucketName);
        if (!exist) {
            // 如果不存在则创建存储空间
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            ossClient.createBucket(createBucketRequest);
        }

        return ossClient;
    }
}
