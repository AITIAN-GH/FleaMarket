package com.xu.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author AITIAN
 */
public class LocalDateConfig {
    /**
     * local-datetime 序列化成 13 位时间戳
     * 北京时间
     */
    public static class LocalDateSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());
        }
    }

    /**
     * 将 13 位时间戳转成 local-datetime
     * 北京时间
     */
    public static class LocalDateDeSerializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext text) throws IOException {
            long timestamp = p.getLongValue();
            return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
        }
    }
}
