package com.coremedia.iso;

import java.io.IOException;
import java.nio.ByteBuffer;

public final class IsoTypeWriter {

    public static void writeUInt64(ByteBuffer bb, long u) {

        writeUInt32(bb, ((u >> 32) & 0xFFFFFFFFl));
        writeUInt32(bb, u & 0xFFFFFFFFl);

    }

    public static void writeUInt32(ByteBuffer bb, long u) {
        assert u >= 0 && u <= 1L << 32: "The given long is not in the range of uint32 (" + u  + ")";
        writeUInt16(bb, (int) ((u >> 16) & 0xFFFF));
        writeUInt16(bb, (int) u & 0xFFFF);

    }


    public static void writeUInt24(ByteBuffer bb, int i) {
        i = i & 0xFFFFFF;
        writeUInt16(bb, i >> 8);
        writeUInt8(bb, i);

    }


    public static void writeUInt16(ByteBuffer bb, int i) {
        i = i & 0xFFFF;
        writeUInt8(bb, i >> 8);
        writeUInt8(bb, i & 0xFF);
    }

    public static void writeUInt8(ByteBuffer bb, int i) {
        bb.put(int2byte(i));
    }


    public static void writeFixedPont1616(ByteBuffer bb, double v) throws IOException {
        int result = (int) (v * 65536);
        bb.put((byte) ((result & 0xFF000000) >> 24));
        bb.put((byte) ((result & 0x00FF0000) >> 16));
        bb.put((byte) ((result & 0x0000FF00) >> 8));
        bb.put((byte) ((result & 0x000000FF)));
    }

    public static void writeFixedPont88(ByteBuffer bb, double v) throws IOException {
        short result = (short) (v * 256);
        bb.put((byte) ((result & 0xFF00) >> 8));
        bb.put((byte) ((result & 0x00FF)));
    }

    public static byte int2byte(int i) {
        i = i & 0xFF;
        return (byte) (i > 127 ? i - 256 : i);
    }

    public static void writeIso639(ByteBuffer bb, String language) {
        int bits = 0;
        for (int i = 0; i < 3; i++) {
            bits += (language.getBytes()[i] - 0x60) << (2 - i) * 5;
        }
        writeUInt16(bb, bits);
    }
}
