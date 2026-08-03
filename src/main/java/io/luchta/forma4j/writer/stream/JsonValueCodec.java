package io.luchta.forma4j.writer.stream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class JsonValueCodec {
    private JsonValueCodec() {
    }

    static Object read(JsonParser parser, JsonToken token) throws IOException {
        if (token == JsonToken.START_OBJECT) {
            Map<String, Object> value = new LinkedHashMap<>();
            while (true) {
                JsonToken field = parser.nextToken();
                if (field == JsonToken.END_OBJECT) {
                    return value;
                }
                if (field == null) {
                    throw new IOException("JSON オブジェクトが途中で終了しました。");
                }
                if (field != JsonToken.FIELD_NAME) {
                    throw new IOException("JSON オブジェクトのフィールド名が不正です。");
                }
                String name = parser.currentName();
                JsonToken child = parser.nextToken();
                if (child == null) {
                    throw new IOException("JSON オブジェクトが途中で終了しました。");
                }
                value.put(name, read(parser, child));
            }
        }
        if (token == JsonToken.START_ARRAY) {
            List<Object> value = new ArrayList<>();
            while (true) {
                JsonToken child = parser.nextToken();
                if (child == JsonToken.END_ARRAY) {
                    return value;
                }
                if (child == null) {
                    throw new IOException("JSON 配列が途中で終了しました。");
                }
                value.add(read(parser, child));
            }
        }
        if (token == JsonToken.VALUE_STRING) {
            return parser.getText();
        }
        if (token == JsonToken.VALUE_TRUE || token == JsonToken.VALUE_FALSE) {
            return parser.getBooleanValue();
        }
        if (token == JsonToken.VALUE_NUMBER_INT) {
            if (parser.getNumberType() == JsonParser.NumberType.BIG_INTEGER) {
                return parser.getBigIntegerValue();
            }
            if (parser.getNumberType() == JsonParser.NumberType.LONG) {
                return parser.getLongValue();
            }
            return parser.getIntValue();
        }
        if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            if (parser.getNumberType() == JsonParser.NumberType.BIG_DECIMAL) {
                return parser.getDecimalValue();
            }
            return parser.getDoubleValue();
        }
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        throw new IOException("サポートされていない JSON トークンです: " + token);
    }

    static void write(JsonGenerator generator, Object value) throws IOException {
        if (value == null) {
            generator.writeNull();
            return;
        }
        if (value instanceof String || value instanceof Character) {
            generator.writeString(value.toString());
            return;
        }
        if (value instanceof Boolean) {
            generator.writeBoolean((Boolean) value);
            return;
        }
        if (value instanceof BigInteger) {
            generator.writeNumber((BigInteger) value);
            return;
        }
        if (value instanceof BigDecimal) {
            generator.writeNumber((BigDecimal) value);
            return;
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
            generator.writeNumber(((Number) value).intValue());
            return;
        }
        if (value instanceof Long) {
            generator.writeNumber((Long) value);
            return;
        }
        if (value instanceof Float) {
            generator.writeNumber((Float) value);
            return;
        }
        if (value instanceof Double) {
            generator.writeNumber((Double) value);
            return;
        }
        if (value instanceof Number) {
            generator.writeNumber(value.toString());
            return;
        }
        if (value instanceof Map<?, ?>) {
            generator.writeStartObject();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                if (!(entry.getKey() instanceof String)) {
                    throw new IOException("SPOOLED sequence の Map キーは String である必要があります。");
                }
                generator.writeFieldName((String) entry.getKey());
                write(generator, entry.getValue());
            }
            generator.writeEndObject();
            return;
        }
        if (value instanceof List<?>) {
            generator.writeStartArray();
            for (Object item : (List<?>) value) {
                write(generator, item);
            }
            generator.writeEndArray();
            return;
        }
        throw new IOException(
                "SPOOLED sequence ではサポートされていない値型です: "
                        + value.getClass().getName()
        );
    }
}
