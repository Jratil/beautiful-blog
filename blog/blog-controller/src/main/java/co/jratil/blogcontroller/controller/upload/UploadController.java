package co.jratil.blogcontroller.controller.upload;

import co.jratil.blogcommon.enums.ResponseEnum;
import co.jratil.blogcommon.exception.GlobalException;
import co.jratil.blogcommon.response.ResponseUtils;
import co.jratil.blogcommon.response.ResponseVO;
import co.jratil.blogapi.service.UploadService;
import co.jratil.blogcontroller.constant.UploadConstant;
import co.jratil.blogcontroller.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-30 17:45
 */
@Api(value = "UploadController", tags = "9-上传相关的接口")
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController extends AbstractController {

    @DubboReference
    private UploadService uploadService;

    @ApiOperation(value = "上传单张图片", notes = "上传单张图片，最大不超过2M，返回的data中为图片的链接", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "picture", value = "上传的图片file", paramType = "file")
    })
    @PostMapping("/pic")
    public ResponseVO uploadPicture(@RequestParam("picture") MultipartFile picture) throws IOException {

        checkPicture(picture);
        String picLink = uploadService.uploadPicture(picture.getBytes(), getSuffix(picture));

        return ResponseUtils.success(picLink);
    }

    @ApiOperation(value = "上传多张图片", notes = "上传多张图片，一共不超过20M，返回的data中为图片链接数组", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pictures", value = "上传的图片数组", paramType = "file")
    })
    @PostMapping("/pics")
    public ResponseVO uploadPictures(@RequestParam("pictures") MultipartFile[] pictures) {

        checkPicture(pictures);
        List<String> picLinks  = Arrays.stream(pictures)
                .map(item -> {
                    String link = "";
                    try {
                        link = uploadService.uploadPicture(item.getBytes(), getSuffix(item));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return link;
                })
                .collect(Collectors.toList());

        return ResponseUtils.success(picLinks);
    }

    private void checkPicture(MultipartFile picture) {
        try {
            // 判断是否为空
            if (picture.isEmpty()) {
                log.error("【上传Controller】上传为文件列表空");
                throw new GlobalException(ResponseEnum.FILE_EMPTY);
            }
            // 判断大小
            if (picture.getSize() > UploadConstant.PICTURE_MAX_SIZE) {
                log.error("【上传Controller】上传为文件列表为不能超过2M");
                throw new GlobalException(ResponseEnum.PICTURE_TOO_BIG);
            }
            // 判断是否为图片
            BufferedImage bi = ImageIO.read(picture.getInputStream());
            if (null == bi) {
                log.error("【上传Controller】上传的文件必须是图片");
                throw new GlobalException(ResponseEnum.NOT_PICTURE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkPicture(MultipartFile[] pictures) {
        Arrays.asList(pictures).forEach(this::checkPicture);
    }

    private String getSuffix(MultipartFile file) {
        // 获取文件的后缀
        String photoName = file.getOriginalFilename();
        String suffix = photoName.substring(photoName.lastIndexOf("."));
        return suffix;
    }
}
