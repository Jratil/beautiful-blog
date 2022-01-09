package co.jratil.blogcommon.util;

import cn.hutool.core.util.StrUtil;
import co.jratil.blogcommon.exception.GlobalException;
import co.jratil.blogcommon.func.IGetter;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author wenjunjun9
 * @created 2021/12/11 16:28
 */
public class FieldUtils {

    /**
     * 根据 lambda 表达式获取字段名
     *
     * @param fn 函数式接口
     * @param <T> any
     * @return
     */
    @NonNull
    public static <T> String fieldName(IGetter<T> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String methodName = serializedLambda.getImplMethodName();

            return method2Filed(methodName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }

    public static String method2Filed(String methodName) {
        String prefix = "";
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        } else {
            throw new GlobalException("请使用规范的 getter 方法");
        }

        return StrUtil.lowerFirst(methodName.replace(prefix, ""));
    }
}
