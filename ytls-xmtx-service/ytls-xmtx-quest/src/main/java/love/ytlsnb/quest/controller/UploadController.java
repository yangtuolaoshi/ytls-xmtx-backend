package love.ytlsnb.quest.controller;

import love.ytlsnb.common.utils.OSSUtil;
import love.ytlsnb.model.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @PostMapping
    public Result<String> upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String newFilename = "quest/location/" + filename + UUID.randomUUID();
        OSSUtil.uploadFile(OSSUtil.QUEST_BUCKET, newFilename, file.getInputStream());
        String accessUrl = "https://"
                + OSSUtil.QUEST_BUCKET + "."
                + OSSUtil.END_POINT + "/"
                + newFilename;
        return Result.ok(accessUrl);
    }
}
