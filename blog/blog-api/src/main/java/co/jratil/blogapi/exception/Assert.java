package co.jratil.blogapi.exception;

import java.util.Collection;

/**
 * 简化异常处理，先搁置
 */
public interface Assert {

    GlobalException newException(Object... args);

    GlobalException newException(Throwable t, Object... args);

    default void assertNotNull(Object object) {
        if (object == null) {
            throw newException();
        }
    }

    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    default void assertNotEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            throw newException();
        }
    }

    default void assertNotEmpty(String str, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newException(args);
        }
    }

    default void assertNotEmpty(Object[] arr) {
        if (arr == null || arr.length == 0) {
            throw newException();
        }
    }

    default void assertNotEmpty(Object[] arr, Object... args) {
        if (arr == null || arr.length == 0) {
            throw newException(args);
        }
    }

    default void assertNotEmpty(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            throw newException();
        }
    }
    default void assertNotEmpty(Collection<?> c, Object... args) {
        if (c == null || c.isEmpty()) {
            throw newException(args);
        }
    }
    default void assertIsFalse(boolean expression) {
        if (expression) {
            throw newException();
        }
    }

    /**
     * 断言布尔值expression为false。如果布尔值expression为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalse(boolean expression, Object... args) {
        if (expression) {
            throw newException(args);
        }
    }

    /**
     * 断言布尔值expression为true。如果布尔值expression为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     */
    default void assertIsTrue(boolean expression) {
        if (!expression) {
            throw newException();
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrue(boolean expression, Object... args) {
        if (!expression) {
            throw newException(args);
        }
    }
}
