package core.constant;

import com.sun.istack.internal.NotNull;

public enum BucketSerializeCategory {

    BYTES{
        @Override
        public <E> byte[] serialize(E object) {
            return new byte[0];
        }

        @Override
        public <E> E deserialize(byte[] object) {
            return null;
        }
    },

    JSON{
        @Override
        public <E> byte[] serialize(E object) {
            return new byte[0];
        }

        @Override
        public <E> E deserialize(byte[] object) {
            return null;
        }
    },

    ;

    public abstract <E> byte[] serialize(@NotNull E object);

    public abstract <E> E deserialize(@NotNull byte[] object);
}
