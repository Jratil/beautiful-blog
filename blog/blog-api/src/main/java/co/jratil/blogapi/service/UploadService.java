package co.jratil.blogapi.service;

import java.util.List;

/**
 * @create 2019-07-30 11:17
 */
public interface UploadService {

    List<String> uploadFile(byte[] fileByte);

    String uploadPicture(byte[] pictureByte, String suffix);

    void deleteFile(String key);
}
