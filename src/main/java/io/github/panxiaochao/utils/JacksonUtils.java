package io.github.panxiaochao.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Mr_LyPxc
 */
public class JacksonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 默认日期时间格式
     */
    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    private static final String DATE_TIME_FORMAT = "HH:mm:ss";

    static {
        // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
        // Since 2.9 use
        OBJECT_MAPPER.setDefaultPropertyInclusion(Include.ALWAYS);
        // 设置时区
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 设置Date类型的序列化及反序列化格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(LOCAL_DATE_TIME_FORMAT));
        // 忽略空Bean转json的错误
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        /** 序列化配置,针对java8 时间 **/
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));

        /** 反序列化配置,针对java8 时间 **/
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    public static ObjectMapper getMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * @param obj 对象
     * @return
     */
    public static String toString(Object obj) {
        if (obj != null) {
            if (obj.getClass() == String.class) {
                return (String) obj;
            }
            try {
                return OBJECT_MAPPER.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                LOGGER.error("json序列化出错：{},{}", obj, e);
                return null;
            }
        }
        return null;
    }

    /**
     * @param json   json
     * @param tClass class
     * @param <T>    T类型
     * @return T类型
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{},{}", json, e);
            return null;
        }
    }

    /**
     * @param inputStream 流
     * @param tClass      class
     * @param <T>         T类型
     * @return T类型
     */
    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(inputStream, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + inputStream.toString(), e);
            return null;
        }
    }

    /**
     * @param bytes  字节
     * @param tClass class
     * @param <T>    T类型
     * @return T类型
     */
    public static <T> T toBean(byte[] bytes, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(bytes, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + new String(bytes), e);
            return null;
        }
    }

    /**
     * @param json   json
     * @param eClass class
     * @param <E>    E
     * @return E
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @param json json
     * @param <T>  T类型
     * @return T类型
     */
    public static <T> T toMap(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<T>() {
            });
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @param json   json
     * @param kClass class
     * @param vClass class
     * @param <K>    K
     * @param <V>    V
     * @return <K, V>
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @param json json
     * @param type type
     * @param <T>  T类型
     * @return T类型
     */
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error("json解析出错：" + json, e);
            return null;
        }
    }
}
