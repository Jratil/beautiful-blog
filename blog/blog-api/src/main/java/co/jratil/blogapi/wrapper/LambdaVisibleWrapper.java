package co.jratil.blogapi.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现 lambdaQueryWrapper 类，用于实现查询时 visible 的判断
 * 使用此 wrapper 可以对 visible 是否加入判断条件进行判断
 * 并且使用 lambda 表达式，而不用写数据库的字段
 *
 * @author jun
 * @version 1.0.0
 * @date 2019-12-18 16:36
 */
public class LambdaVisibleWrapper<T> extends LambdaQueryWrapper<T> {

    private SharedString sqlSelect;

    public LambdaVisibleWrapper() {
    }

    public LambdaVisibleWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.entityClass = entityClass;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    public LambdaVisibleWrapper<T> eqVisible(SFunction<T, ?> sFunction, boolean visible) {
        if (visible) {
            this.eq(sFunction, visible);
        }

        return this;
    }

    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }
}
