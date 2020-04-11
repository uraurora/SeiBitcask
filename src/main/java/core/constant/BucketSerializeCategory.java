package core.constant;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import util.JsonUtil;

import java.nio.charset.StandardCharsets;

public enum BucketSerializeCategory {

    DEFAULT{
        @Override
        public byte[] serialize(String object) {
            return object == null ? new byte[0] : object.getBytes();
        }

        @Override
        public String deserialize(byte[] object) {
            return new String(object, StandardCharsets.UTF_8);
        }
    },

    JSON{
        @Override
        public byte[] serialize(String object) {
            return new byte[0];
        }

        @Override
        public String deserialize(byte[] object) {
            return null;
        }
    },

    ;

    public abstract byte[] serialize(@NotNull String object);

    public abstract String deserialize(@Nullable byte[] object);
}
