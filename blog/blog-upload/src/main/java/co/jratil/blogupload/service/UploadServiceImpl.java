package co.jratil.blogupload.service;

import co.jratil.blogapi.service.UploadService;
import co.jratil.blogcommon.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-30 17:08
 */
@Slf4j
@Component
@Service(interfaceClass = UploadService.class)
public class UploadServiceImpl implements UploadService {

    @Value("${qiniu.bucket}")
    private String Bucket;

    @Value("${qiniu.link-pre}")
    private String LinkPre;

    @Autowired
    private Auth auth;

    @Autowired
    private UploadManager uploadManager;

    @Override
    public List<String> uploadFile(byte[] fileByte) {
        return null;
    }

    @Override
    public String uploadPicture(byte[] pictureByte, String suffix) {

        // 获取文件名
        String photoName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        // 获取令牌
        String upToken = auth.uploadToken(Bucket);

        // 七牛云的返回响应
        Response response;
        String picLink = "";

        try {
            response = uploadManager.put(pictureByte, photoName, upToken);
            DefaultPutRet defaultPutRet = new ObjectMapper().readValue(response.bodyString(), DefaultPutRet.class);
            picLink = LinkPre + defaultPutRet.key;
        } catch (QiniuException e) {
            response = e.response;
            log.error("【上传照片】上传照片出错！七牛云错误。{}", response.toString());
            throw new GlobalException(response.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return picLink;
    }

    @Override
    public void deleteFile(String key) {

    }
}
