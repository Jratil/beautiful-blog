package co.jratil.blogapi.wrapper;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 *
 * 和 QueryWrapper 一样的继承，修改了 lambda 方法
 * 实现 visible 以及自定义的一些查询条件
 * 默认 visible 的条件判断可以直接使用 LambdaVisibleWrapper 来进行
 *
 * @author jun
 * @version 1.0.0
 * @date 2019-12-18 15:59
 */
public class VisibleWrapper<T>  extends AbstractWrapper<T, String, VisibleWrapper<T>> implements Query<VisibleWrapper, T, String> {

    private SharedString sqlSelect;

    private String fieldName;

    private boolean visible;

    public VisibleWrapper() {
        this(null);
    }

    public VisibleWrapper(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    public VisibleWrapper(T entity, String... columns) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    private VisibleWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        this.entityClass = entityClass;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
    }

    public VisibleWrapper(String fieldName, boolean visible) {
        this.fieldName = fieldName;
        this.visible = visible;
    }

    /**
     * 配合有参构造方法使用
     * @return
     */
    public VisibleWrapper<T> visible() {
        return this.setVisible();
    }

    /**
     * 配合无参构造方法使用
     * @param fieldName
     * @param visible
     * @return
     */
    public VisibleWrapper<T> visible(String fieldName, boolean visible) {

        // 如果为 null 或者传入的值是 true，则只能看到为true的
        // 否则不设置该查询条件
        if (visible) {
            this.eq(fieldName, true);
        }

        return this;
    }

    /**
     * 获取自定义的 visible lambda 查询，其中可以使用 lambda 表达式来获取 visible 字段
     * @return 自定义的 visible lambda 查询
     */
    public LambdaVisibleWrapper<T> lambda() {
        return new LambdaVisibleWrapper<>(this.entity, this.entityClass, this.sqlSelect, this.paramNameSeq, this.paramNameValuePairs, this.expression, this.lastSql, this.sqlComment);
    }

    @Override
    public VisibleWrapper select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(",", columns));
        }

        return this.typedThis;
    }

    @Override
    public VisibleWrapper select(Predicate<TableFieldInfo> predicate) {
        return this.select(this.entityClass, predicate);
    }

    @Override
    public VisibleWrapper select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(this.getCheckEntityClass()).chooseSelect(predicate));
        return this.typedThis;
    }

    @Override
    protected VisibleWrapper<T> instance() {
        return new VisibleWrapper(this.entity, this.entityClass, this.paramNameSeq, this.paramNameValuePairs, new MergeSegments(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    private VisibleWrapper<T> setVisible() {

        // 如果为 null 或者传入的值是 true，则只能看到为true的
        // 否则不设置该查询条件
        if (this.visible) {
            this.eq(fieldName, true);
        }

        return this;
    }
}
