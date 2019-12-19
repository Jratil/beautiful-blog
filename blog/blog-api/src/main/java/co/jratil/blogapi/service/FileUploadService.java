package co.jratil.blogapi.service;

import co.jratil.blogapi.response.ResponseVO;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * @create 2019-07-30 11:17
 */
public interface FileUploadService {

    Map<String, Object> uploadFile(File file);

    Map<String, Object> uploadFile(InputStream inputStream);

    Map<String, Object> uploadImage(InputStream inputStream, String suffix);

    ResponseVO delete(String key);
}
