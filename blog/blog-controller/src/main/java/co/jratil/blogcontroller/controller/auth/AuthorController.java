package co.jratil.blogcontroller.controller.auth;

import co.jratil.blogapi.entity.PageParam;
import co.jratil.blogcontroller.controller.AbstractController;
import co.jratil.blogapi.entity.dto.AuthorDTO;
import co.jratil.blogapi.entity.dto.AuthorForm;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogapi.response.ResponseVO;
import co.jratil.blogapi.response.ResponseUtils;
import co.jratil.blogapi.service.AuthorService;
import co.jratil.blogapi.exception.GlobalException;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "AuthorController", tags = "1-用户相关的接口")
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthorController extends AbstractController {

    @Reference
    private AuthorService authorService;

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorForm", value = "注册所需的json", dataTypeClass = AuthorForm.class, paramType = "body")
    })
    @PostMapping("/register")
    public ResponseVO register(@Validated(AuthorForm.Register.class) @RequestBody AuthorForm authorForm,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("【用户注册】参数不正确，authorForm={}", authorForm);
            return ResponseUtils.error(ResponseEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        authorService.register(authorForm);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorForm", value = "登录所需的json", dataTypeClass = AuthorForm.class, example = "{\"email\":\"123\",\n\"password\":\"123\"}", paramType = "body")
    })
    @PostMapping("/login")
    public ResponseVO login(@RequestBody AuthorForm authorForm,
                            BindingResult bindingResult,
                            HttpServletRequest request, HttpServletResponse response) {
//        if (bindingResult.hasErrors()) {
//            log.error("【用户登录】参数不正确，authorForm={}", authorForm);
//            return ResponseUtils.error(ResponseEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
//        }
//
//        AuthorDTO authorDTO = authorService.login(authorForm.getAuthorAccount(), authorForm.getAuthorPassword(), DruidWebUtils.getRemoteAddr(request));
//        // 客户端不存在cookie或者cookie和session中的不相等，则登录
//        String token = UUID.randomUUID().toString().replace("-", "");
//
//        return ResponseUtils.success(authorDTO);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "用户登出", notes = "用户登出-现在实现：前端直接从浏览器删除 token 就好，后续可能增加续签等", httpMethod = "DELETE")
    @DeleteMapping("/logout")
    public ResponseVO logout(HttpServletRequest request, HttpServletResponse response) {

        // 前端将token从浏览器删除，就是登出了。否则要在数据库创建一个token黑名单库表，将登出的token加入该表
        return ResponseUtils.success();
    }

    @ApiOperation(value = "查找用户", notes = "按用户id查找用户信息", httpMethod = "GET")
    @GetMapping("/id/{authorId:\\d+}")
    public ResponseVO queryAuthorById(@PathVariable("authorId") Integer authorId) {
        this.checkParam(authorId, "authorId", this.getClass());

        AuthorDTO authorDTO = authorService.getById(authorId);
        return ResponseUtils.success(authorDTO);
    }

    @ApiOperation(value = "查找用户", notes = "按用户account查找用户信息", httpMethod = "GET")
    @GetMapping("/account/{account}")
    public ResponseVO queryAuthorByAccount(@PathVariable("account") String authorAccount) {
        this.checkParam(authorAccount, "authorAccount", this.getClass());

        AuthorDTO authorDTO = authorService.getByAccount(authorAccount);
        return ResponseUtils.success(authorDTO);
    }

    @ApiOperation(value = "模糊名称分页查找", notes = "按模糊名称分页查找用户", httpMethod = "POST")
    @GetMapping("/page/{blurName}")
    public ResponseVO pageQueryByBlurName(@PathVariable("blurName") String blurName, HttpServletRequest request) {
        this.checkParam(blurName, "blurName", this.getClass());

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<AuthorDTO> result = authorService.listByBlueName(pageParam, blurName);

        return ResponseUtils.success(result);
    }

    @ApiOperation(value = "按注册时间查找用户，", notes = "按注册时间分页查找用户", httpMethod = "POST")
    @GetMapping("/page")
    public ResponseVO pageQuery(HttpServletRequest request) {

        PageParam pageParam = new PageParam(getPage(), getCount());
        PageInfo<AuthorDTO> result = authorService.list(pageParam);
        return ResponseUtils.success(result);
    }

    @ApiOperation(value = "修改资料", notes = "用户修改资料", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorDTO", value = "修改信息所需要穿的json", dataTypeClass = AuthorDTO.class, example = "{\"name\":\"1\",\n\"email\":\"123\",\n\"password\":\"123\",\n\"verify-code\":\"1234\"}", paramType = "body")
    })
    @PutMapping()
    public ResponseVO updateAuthor(@RequestBody AuthorDTO authorDTO) {
        if (authorDTO == null || authorDTO.getAuthorId()==null || authorDTO.getAuthorAccount() == null) {
            log.error("【用户修改信息】参数错误，authorDTO = {}", authorDTO);
            throw new GlobalException(ResponseEnum.PARAM_ERROR);
        }

        authorService.updateAuthor(authorDTO);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "用户修改密码", notes = "用户修改密码", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorForm", value = "修改密码所需的json", dataTypeClass = AuthorForm.class, example = "{\"email\":\"123\",\n\"password\":\"123\", \n\"verify-code\":\"1234\"}", paramType = "body")
    })
    @PutMapping("/password")
    public ResponseVO forgetPassword(@Validated(AuthorForm.ForgetPassword.class) @RequestBody AuthorForm authorForm,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("【用户注册】参数不正确，authorForm={}", authorForm);
            throw new GlobalException(ResponseEnum.PARAM_ERROR);
        }

        authorService.updatePassword(authorForm);

        return ResponseUtils.success();
    }

    @ApiOperation(value = "删除用户", notes = "根据用户id删除用户-需要管理员权限", httpMethod = "DELETE")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{authorId:\\d+}")
    public ResponseVO deleteAuthor(@PathVariable("authorId") Integer authorId) {
        this.checkParam(authorId, "authorId", this.getClass());

        authorService.deleteAuthor(authorId);
        return ResponseUtils.success();
    }
}
