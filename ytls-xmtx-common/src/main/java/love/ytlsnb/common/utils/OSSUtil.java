package love.ytlsnb.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * OSS操作接口
 *
 * @author 金泓宇
 * @date 2024/2/17
 */
@FunctionalInterface
interface OSSOperation {
    void operate();
}

/**
 * OSS工具类
 *
 * @author 金泓宇
 * @date 2024/2/17
 */
@Slf4j
public class OSSUtil {
    public static OSS ossClient;

    static {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。
        String endpoint = "oss-rg-china-mainland.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请先配置环境变量。
        EnvironmentVariableCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
    }

    /**
     * OSS操作增强
     *
     * @param ossOperation 操作（函数式接口）
     */
    private static void doOperation(OSSOperation ossOperation) {
        try {
            ossOperation.operate();
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 使用输入流上传文件
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称，为相对路径，比如testdir/test.png
     * @param inputStream 输入流
     */
    public static void uploadFile(String bucketName, String objectName, InputStream inputStream) {
        doOperation(() -> {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("File uploaded successfully. ETag: " + result.getETag());
        });
    }

    /**
     * 删除对象文件
     *
     * @param bucketName 桶名称
     * @param objectName 对象名称
     */
    public static void removeFile(String bucketName, String objectName) {
        doOperation(() -> {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
            log.info("File removed successfully.");
        });
    }
}
