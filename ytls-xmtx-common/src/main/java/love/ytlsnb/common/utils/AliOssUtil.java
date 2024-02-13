package love.ytlsnb.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.properties.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author ula
 * @date 2024/2/13 10:24
 */
@Slf4j
@Component
public class AliOssUtil {
    @Autowired
    private AliOssProperties aliOssProperties;

    /**
     * 文件上传
     *
     * @param bytes      待上传文件的字节数据
     * @param objectName 待上传文件的文件名称
     * @return 阿里云OSS的存储地址
     */
    public String upload(byte[] bytes, String objectName) {
        return upload(new ByteArrayInputStream(bytes), objectName);
    }

    /**
     * 文件上传
     *
     * @param inputStream 待上传文件的数据输入流
     * @param objectName  待上传文件的文件名称
     * @return 阿里云OSS的存储地址
     */
    public String upload(InputStream inputStream, String objectName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(aliOssProperties.getEndpoint(),
                        aliOssProperties.getAccessKeyId(),
                        aliOssProperties.getAccessKeySecret());

        try {
            // 创建PutObject请求。
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径规则 https://bucketName.endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(aliOssProperties.getBucketName())
                .append(".")
                .append(aliOssProperties.getEndpoint())
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder);

        return stringBuilder.toString();
    }
}
