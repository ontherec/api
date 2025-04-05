package kr.ontherec.api.infra.util;

import org.tinylog.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    public static Map<String, Class<?>> getFieldTypeMap(Class<?> clazz) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            fieldMap.put(field.getName(), field.getType());
        }
        Logger.info(clazz.getSimpleName() + ": " + fieldMap);
        return fieldMap;
    }
}
