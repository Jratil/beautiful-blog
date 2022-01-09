package co.jratil.blogcommon.func;

import java.io.Serializable;

/**
 * @author wenjunjun9
 * @created 2021/12/11 16:41
 */
@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object get(T source);
}
