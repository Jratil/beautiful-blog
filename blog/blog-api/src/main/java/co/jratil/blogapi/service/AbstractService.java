package co.jratil.blogapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * @author jun
 * @version 1.0.0
 * @date 2019-12-11 16:33
 */
public abstract class AbstractService<T> {

    /**
     * 获取 Wrapper 的条件
     * 如果不是自己或者管理员，则 visible = true 表示只能看到开放的内容
     * 则需要将 visible = true 加入到 sql 语句的条件中
     * 否则不用加入此条件
     * @param fieldName
     * @param visible
     * @return
     */
//    protected QueryWrapper<T> getVisibleWrapper(String fieldName, boolean visible) {
//        // 从request中获取visible参数，在controller中设置的
//        QueryWrapper<T> wrapper = new QueryWrapper<>();
//
//        // 如果为 null 或者传入的值是 true，则只能看到为true的
//        // 否则不设置该查询条件
//        if (visible) {
//            wrapper.eq(fieldName, true);
//        }
//
//        return wrapper;
//    }
}
