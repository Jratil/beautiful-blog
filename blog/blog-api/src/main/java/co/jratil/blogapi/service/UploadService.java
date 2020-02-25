package co.jratil.blogapi.service;

import co.jratil.blogapi.response.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @create 2019-07-30 11:17
 */
public interface UploadService {

    List<String> uploadFile(byte[] fileByte);

    String uploadPicture(byte[] pictureByte, String suffix);

    void deleteFile(String key);
}
