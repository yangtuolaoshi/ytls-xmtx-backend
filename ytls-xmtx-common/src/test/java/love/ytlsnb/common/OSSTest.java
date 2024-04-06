package love.ytlsnb.common;

import com.aliyun.oss.common.auth.Credentials;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import love.ytlsnb.common.utils.OSSUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OSSTest {
    public static void main(String[] args) throws ClientException, FileNotFoundException {
//        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
//        Credentials credentials = credentialsProvider.getCredentials();
//        String accessKeyId = credentials.getAccessKeyId();
//        String secretAccessKey = credentials.getSecretAccessKey();
//        System.out.println("AccessKeyId: " + accessKeyId);
//        System.out.println("SecretAccessKey: " + secretAccessKey);
//        System.out.println(OSSUtil.ossClient);

        // 上传文件
        // 输入流
        String localFilepath = "D:\\ossfiles\\拜占庭.webp";
        InputStream inputStream = new FileInputStream(localFilepath);
        // 上传路径
        String objectName = "testdir/拜占庭.webp";
        // 桶名称
        String bucketName = "ytls-xmtx";
        // 上传
        OSSUtil.uploadFile(bucketName, objectName, inputStream);

        // 删除文件
//        OSSUtil.removeFile(bucketName, objectName);
    }
}
