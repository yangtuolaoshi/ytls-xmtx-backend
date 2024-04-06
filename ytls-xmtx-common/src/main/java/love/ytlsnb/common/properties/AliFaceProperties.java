package love.ytlsnb.common.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/14 9:30
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("xmtx.ali.face")
public class AliFaceProperties {
    /**
     * 阿里人脸识别的域名
     */
    private String endpoint;
    /**
     * 人脸识别图像的质量阈值（取值越高则图像质量越高）
     */
    private Float scoreThreshold;
    /**
     * 人脸识别图像的模糊阈值（取值越高则图像越清晰）
     */
    private Float blurThreshold;
    /**
     * 人脸识别图像的真人阈值（取值越高则越能保证上传的是真人图像）
     */
    private Float fnfThreshold;
    /**
     * 人脸识别图像的面罩阈值（取值越高则越能保证上传的人未戴口罩）
     */
    private Float maskThreshold;
    /**
     * 人脸图像对比的置信度阈值
     */
    private Float confidenceThreshold;
}
