package co.jratil.blogcontroller.controller;

import co.jratil.blogapi.exception.GlobalException;
import co.jratil.blogapi.enums.ResponseEnum;
import co.jratil.blogsecurity.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-10 14:24
 */
@Slf4j
public abstract class AbstractController<T> {

    protected Integer getPage() {
        HttpServletRequest request = getRequest();
        String page = request.getParameter("page");
        return StringUtils.isEmpty(page) ? 1 : Integer.parseInt(page);
    }

    protected Integer getCount() {
        HttpServletRequest request = getRequest();
        String count = request.getParameter("count");
        return StringUtils.isEmpty(count) ? 10 : Integer.parseInt(count);
    }

    /**
     * 检查参数是否为空
     *
     * @param obj       要检查的参数
     * @param paramName 检查的参数的名称
     */
    protected void checkParam(Object obj, String paramName, Class clazz) {
        if (StringUtils.isEmpty(obj)) {
            Logger logger = LoggerFactory.getLogger(clazz);
            logger.error("【Controller操作】参数不正确，{}={}", paramName, obj);
            throw new GlobalException(ResponseEnum.PARAM_ERROR);
        }
    }

    /**
     * 检查 Validate 校验的异常
     * @param result
     * @param obj
     */
    protected void checkBindindResult(BindingResult result, Object obj) {
        if (result.hasErrors()) {
            log.error("【文章操作】添加文章，文章参数不正确，obj={}", obj);
            throw new GlobalException(result.getFieldError().getDefaultMessage());
        }
    }

//    protected void checkParam(String paramName, Object obj, Class clazz) {
//        Logger logger = LoggerFactory.getLogger(clazz);
//        if (StringUtils.isEmpty(obj)) {
//            logger.error("【Controller查询用户】参数不正确，{}={}", paramName, obj);
//            throw new GlobalException(ResponseEnum.PARAM_ERROR);
//        }
//    }

    protected boolean checkMe(Integer authorId) {
        Integer saveId = SecurityUtils.getAuthorId();
        return authorId.equals(saveId);
    }

    /**
     * 分页查询中，通过authorId判断是否是用户自己的查询
     * 然后再设置visible条件
     *
     * @param authorId
     */
    protected boolean getVisible(Integer authorId) {
        // 如果不是登录用户查询，则设置只能查询全部可见的
        if (!authorId.equals(SecurityUtils.getAuthorId())) {
            return true;
        }
        return false;
    }

    /**
     * 单个查询中检查查询到的数据是否是全部可见
     * 并且和当前登录用户比较，判断是否有权查看
     *
     * @param authorId 用户id
     * @param visible  查询出的 visible
     */
    protected void checkMeAndVisible(Integer authorId, boolean visible, ResponseEnum repEnum) {
        if (!checkMe(authorId) && !visible) {
            throw new GlobalException(repEnum);
        }
    }

    private HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
}
