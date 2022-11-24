package mscw.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

/**
 * @apiNote 基于JackSon封装的Json解析类。究其原因还是因为FastJson漏洞着实过多。
 * @author wu nan
 * @since  2022/11/8
 **/
public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static final ObjectMapper getMapper() {
        return MAPPER;
    }

    public static <T> T fromJson(byte[] srcBytes, Class<T> valueType)
            throws Exception {
        return MAPPER.readValue(srcBytes, valueType);
    }

    public static <T> T fromJson(String src, Class<T> valueType) throws Exception {
        return MAPPER.readValue(src, valueType);
    }

    public static <T> T fromJson(InputStream src, Class<T> valueType) throws Exception {
        return MAPPER.readValue(src, valueType);
    }

    public static String toJson(Object obj) throws Exception {
        return MAPPER.writeValueAsString(obj);
    }

    public static <T> List<T> jsonToList(String src, Class<T> t) throws Exception {
        return MAPPER.readValue(src, MAPPER.getTypeFactory().constructCollectionType(List.class, t));
    }

    public static JsonNode toJsonnode(String jsonStr) throws Exception {
        return MAPPER.readTree(jsonStr);
    }
}
