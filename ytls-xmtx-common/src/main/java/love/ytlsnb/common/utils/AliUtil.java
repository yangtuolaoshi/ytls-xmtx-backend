package love.ytlsnb.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.facebody20191230.models.*;
import com.aliyun.ocr20191230.models.RecognizeIdentityCardAdvanceRequest;
import com.aliyun.ocr20191230.models.RecognizeIdentityCardResponse;
import com.aliyun.ocr20191230.models.RecognizeIdentityCardResponseBody;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.*;
import love.ytlsnb.model.user.dto.IdCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ula
 * @date 2024/2/13 16:16
 */
@Slf4j
@Component
public class AliUtil {
    @Autowired
    private AliProperties aliProperties;
    @Autowired
    private AliOssProperties aliOssProperties;
    @Autowired
    private AliSmsProperties aliSmsProperties;
    @Autowired
    private AliFaceProperties aliFaceProperties;
    @Autowired
    private AliOcrProperties aliOcrProperties;

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
                        aliProperties.getAccessKeyId(),
                        aliProperties.getAccessKeySecret());

        try {
            // 创建PutObject请求。
            ossClient.putObject(aliOssProperties.getBucketName(), objectName, inputStream);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw new BusinessException(ResultCodes.SERVER_ERROR, oe.getMessage());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
            throw new BusinessException(ResultCodes.SERVER_ERROR, ce.getMessage());
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

    /**
     * 调用api发送短信
     *
     * @param phoneNumber 发送验证码的对象（内部不校验）
     * @return 发送的验证码
     * @throws Exception 发送短信失败的异常
     */
    public String sendShortMessage(String phoneNumber) throws Exception {
        // 准备配置
        Config config = new Config();
        config.setAccessKeyId(aliProperties.getAccessKeyId())
                .setAccessKeySecret(aliProperties.getAccessKeySecret())
                .setEndpoint(aliSmsProperties.getEndpoint());
        Client client = new Client(config);
        // 准备验证码
        String code = RandomUtil.randomNumbers(aliSmsProperties.getCodeLength());
        // 准备发送请求
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(aliSmsProperties.getSignName())
                .setTemplateCode(aliSmsProperties.getTemplateCode())
                .setTemplateParam("{\"code\":\"" + code + "\"}")
                .setPhoneNumbers(phoneNumber);
        RuntimeOptions runtimeOptions = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtimeOptions);
            return code;
        } catch (TeaException teaException) {
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, teaException.getMessage());
        } catch (Exception exception) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, exception.getMessage());
        }
    }

    /**
     * 人脸比对接口，若为同一个人则正常执行，若不为同一个人或者原照片与目标照片不为同一个人或者两张照片中含不合法照片，则抛出异常
     * （注意，内部会调用faceDetect进行照片检测，会增加耗时，可优化逻辑减少此处时间损耗）
     *
     * @param source 进行人脸对比的源照片
     * @param target 进行人脸对比的目标照片
     * @throws Exception 比对失败的异常
     */
    public void faceCompare(String source, String target) throws Exception {
        faceDetect(source);
        faceDetect(target);
        Config config = new Config();
        config.setAccessKeyId(aliProperties.getAccessKeyId())
                .setAccessKeySecret(aliProperties.getAccessKeySecret())
                .setEndpoint(aliFaceProperties.getEndpoint());
        com.aliyun.facebody20191230.Client client = new com.aliyun.facebody20191230.Client(config);

        CompareFaceAdvanceRequest request = new CompareFaceAdvanceRequest();
        request.setImageURLAObject(new URL(source).openStream())
                .setImageURLBObject(new URL(target).openStream());
        try {
            CompareFaceResponse response = client.compareFaceAdvance(request, new RuntimeOptions());
            // 获取整体结果
            CompareFaceResponseBody.CompareFaceResponseBodyData data = response.getBody().getData();
            if (data.getConfidence().compareTo(aliFaceProperties.getConfidenceThreshold()) < 0) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "两张图像不是同一个人");
            }
        } catch (TeaException teaException) {
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, teaException.getMessage());
        }
    }

    /**
     * 检测传入参数所代表的图像是否是一张合格的人像图片，若传入参数不合法或图像质量较低会抛出异常
     *
     * @param photo 定位图像的URL字符串
     * @throws Exception 根据检测图像的质量可能抛出携带异常信息的Exception
     */
    public void faceDetect(String photo) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(aliProperties.getAccessKeyId())
                .setAccessKeySecret(aliProperties.getAccessKeySecret())
                .setEndpoint(aliFaceProperties.getEndpoint());
        com.aliyun.facebody20191230.Client client = new com.aliyun.facebody20191230.Client(config);

        // 根据图片创建URL
        URL url = new URL(photo);
        // 创建请求对象
        RecognizeFaceAdvanceRequest recognizeFaceAdvanceRequest = new RecognizeFaceAdvanceRequest();
        InputStream inputStream = url.openStream();
        recognizeFaceAdvanceRequest.setImageURLObject(inputStream);

        try {
            RecognizeFaceResponse response = client.recognizeFaceAdvance(recognizeFaceAdvanceRequest, new RuntimeOptions());
            // 获取整体结果
            RecognizeFaceResponseBody.RecognizeFaceResponseBodyData data = response.getBody().getData();
            Integer faceCount = data.getFaceCount();
            if (faceCount != 1) {
                // 待识别的图像没有人脸或者有多张人脸
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "请上传仅含有一张人脸的照片");
            }
            // 图片的一系列得分（一般来说得分越高越好）
            RecognizeFaceResponseBody.RecognizeFaceResponseBodyDataQualities qualities = data.getQualities();
            // 图片质量列表
            List<Float> scoreList = qualities.getScoreList();
            // 真人真实度列表
            List<Float> fnfList = qualities.getFnfList();
            // 图片模糊列表
            List<Float> blurList = qualities.getBlurList();
            // 图片面罩列表
            List<Float> maskList = qualities.getMaskList();
            // 逻辑拆分
            if (scoreList.get(0).compareTo(aliFaceProperties.getScoreThreshold()) < 0) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "上传图像质量过低");
            }
            if (blurList.get(0).compareTo(aliFaceProperties.getBlurThreshold()) < 0) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "上传图像过于模糊");
            }
            if (fnfList.get(0).compareTo(aliFaceProperties.getFnfThreshold()) < 0) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "未能识别到有效人脸");
            }
            if (maskList.get(0).compareTo(aliFaceProperties.getMaskThreshold()) < 0) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "请上传未佩戴口罩的照片");
            }
        } catch (TeaException teaException) {
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, teaException.getMessage());
        }
    }

    /**
     * 识别参数身份证的合法性
     *
     * @param photo 身份证背面图片的URL
     * @throws Exception 识别失败的异常
     */
    public void recognizeIdCardBack(String photo) throws Exception {
        // 准备相关配置
        Config config = new Config();
        config.setAccessKeyId(aliProperties.getAccessKeyId())
                .setAccessKeySecret(aliProperties.getAccessKeySecret())
                .setEndpoint(aliOcrProperties.getEndpoint());
        com.aliyun.ocr20191230.Client client = new com.aliyun.ocr20191230.Client(config);

        // 准备请求参数
        RecognizeIdentityCardAdvanceRequest request = new RecognizeIdentityCardAdvanceRequest();
        request.setImageURLObject(new URL(photo).openStream())
                .setSide("back");
        try {
            // 发送请求，获取响应
            RecognizeIdentityCardResponse response = client.recognizeIdentityCardAdvance(request, new RuntimeOptions());

            // 校验身份证是否已经过期
            RecognizeIdentityCardResponseBody.RecognizeIdentityCardResponseBodyDataBackResult backResult = response.getBody().getData().getBackResult();
            LocalDate localDate = LocalDate.parse(backResult.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (localDate.isBefore(LocalDate.now())) {
                throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "身份证件已过期");
            }
        } catch (com.aliyun.tea.TeaException teaException) {
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, teaException.getMessage());
        }
    }

    /**
     * 根据传入参数识别传入身份证的相关信息
     *
     * @param photo 身份证正面图片的URL
     * @return 识别出的身份证信息
     * @throws Exception 识别失败的异常
     */
    public IdCard recognizeIdCardFront(String photo) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(aliProperties.getAccessKeyId())
                .setAccessKeySecret(aliProperties.getAccessKeySecret())
                .setEndpoint(aliOcrProperties.getEndpoint());
        com.aliyun.ocr20191230.Client client = new com.aliyun.ocr20191230.Client(config);

        RecognizeIdentityCardAdvanceRequest request = new RecognizeIdentityCardAdvanceRequest();
        request.setImageURLObject(new URL(photo).openStream())
                .setSide("front");

        try {
            RecognizeIdentityCardResponse response = client.recognizeIdentityCardAdvance(request, new RuntimeOptions());

            RecognizeIdentityCardResponseBody.RecognizeIdentityCardResponseBodyDataFrontResult frontResult = response.getBody().getData().getFrontResult();
            IdCard idCard = BeanUtil.copyProperties(frontResult, IdCard.class);
            idCard.setIdNumber(frontResult.getIDNumber());
            return idCard;
        } catch (com.aliyun.tea.TeaException teaException) {
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, teaException.getMessage());
        }
    }
}
