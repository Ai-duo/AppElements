package com.kd.appelements.tools;


import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class StrUtil {
    public static int hexCharToInt(char c) {
        if ((c >= '0') && (c <= '9'))
            return (c - '0');
        if ((c >= 'A') && (c <= 'F'))
            return (c - 'A' + 10);
        if ((c >= 'a') && (c <= 'f'))
            return (c - 'a' + 10);

        throw new RuntimeException("invalid hex char '" + c + "'");
    }
    public static String bytesToAscii(ArrayList<Byte> bytes) {
        if(bytes==null||bytes.size()==0){
            return null;
        }
        byte[] bytes1 = new byte[bytes.size()];
        for(int i=0;i<bytes.size();i++){
            bytes1[i] = bytes.get(i);
        }
        return bytesToAscii(bytes1);
    }
    public static String bytesToAscii(byte[] bytes, int offset, int dateLen) {
        if ((bytes == null) || (bytes.length == 0) || (offset < 0) || (dateLen <= 0)) {
            return null;
        }
        if ((offset >= bytes.length) || (bytes.length - offset < dateLen)) {
            return null;
        }

        String asciiStr = null;
        byte[] data = new byte[dateLen];
        System.arraycopy(bytes, offset, data, 0, dateLen);
        try {//ISO8859-1
            asciiStr = new String(data, "ascii");
        } catch (UnsupportedEncodingException e) {
        }
        return asciiStr;
    }

    public static String bytesToAscii(byte[] bytes, int dateLen) {
        return bytesToAscii(bytes, 0, dateLen);
    }

    public static String bytesToAscii(byte[] bytes) {
        return bytesToAscii(bytes, 0, bytes.length);
    }

    public static String bytesToHexString(byte[] bytes, int offset, int len) {
        if (bytes == null)
            return "null!";

        StringBuilder ret = new StringBuilder(2 * len);

        for (int i = 0; i < len; ++i) {
            int b = 0xF & bytes[(offset + i)] >> 4;
            ret.append("0123456789abcdef".charAt(b));
            b = 0xF & bytes[(offset + i)];
            ret.append("0123456789abcdef".charAt(b));
        }

        return ret.toString();
    }

    public static String bytesToHexString(byte[] bytes, int len) {
        return ((bytes == null) ? "null!" : bytesToHexString(bytes, 0, len));
    }

    public static String bytesToHexString(byte[] bytes) {
        return ((bytes == null) ? "null!" : bytesToHexString(bytes, bytes.length));
    }

    public static byte[] hexStringToBytes(String s) {
        if (s == null)
            return null;

        int sz = s.length();
        try {
            byte[] ret = new byte[sz / 2];
            for (int i = 0; i < sz; i += 2) {
                ret[(i / 2)] = (byte) (hexCharToInt(s.charAt(i)) << 4 | hexCharToInt(s.charAt(i + 1)));
            }

            return ret;
        } catch (RuntimeException re) {
        }
        return null;
    }

    public static byte[] shortToBytesLe(short shortValue) {
        byte[] bytes = new byte[2];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (shortValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    public static byte[] shortToBytesBe(short shortValue) {
        byte[] bytes = new byte[2];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[(bytes.length - i - 1)] = (byte) (shortValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    public static byte[] intToBytesLe(int intValue) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (intValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    public static byte[] intToBytesBe(int intValue) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[(bytes.length - i - 1)] = (byte) (intValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    public static int bytesToIntLe(byte[] bytes) {
        if ((bytes == null) || (bytes.length > 4)) {
            throw new RuntimeException("invalid arg");
        }

        int ret = 0;

        for (int i = 0; i < bytes.length; ++i) {
            ret += ((bytes[i] & 0xFF) << i * 8);
        }

        return ret;
    }

    public static int bytesToIntLe(byte[] data, int start, int end) {
        return bytesToIntLe(Arrays.copyOfRange(data, start, end));
    }

    public static int bytesToIntBe(byte[] bytes) {
        if ((bytes == null) || (bytes.length > 4)) {
            throw new RuntimeException("invalid arg");
        }

        int ret = 0;

        for (int i = 0; i < bytes.length; ++i) {
            ret += ((bytes[i] & 0xFF) << (bytes.length - i - 1) * 8);
        }

        return ret;
    }

    public static int bytesToIntBe(byte[] data, int start, int end) {
        return bytesToIntBe(Arrays.copyOfRange(data, start, end));
    }

    public static int bytesToIntLe(byte b0, byte b1, byte b2, byte b3) {
        int ret = 0;

        ret = b0 & 0xFF;
        ret += ((b1 & 0xFF) << 8);
        ret += ((b2 & 0xFF) << 16);
        ret += ((b3 & 0xFF) << 24);

        return ret;
    }

    public static int bytesToIntBe(byte b0, byte b1, byte b2, byte b3) {
        int ret = 0;

        ret = (b0 & 0xFF) << 24;
        ret += ((b1 & 0xFF) << 16);
        ret += ((b2 & 0xFF) << 8);
        ret += (b3 & 0xFF);

        return ret;
    }

    public static void byteArraySetByte(byte[] bytesArray, byte setValue, int index) {
        bytesArray[index] = setValue;
    }

    public static void byteArraySetByte(byte[] bytesArray, int setValue, int index) {
        bytesArray[index] = (byte) (setValue & 0xFF);
    }

    public static void byteArraySetBytes(byte[] bytesArray, byte[] setValues, int index) {
        System.arraycopy(setValues, 0, bytesArray, index, setValues.length);
    }

    public static void byteArraySetWord(byte[] bytesArray, int setValue, int index) {
        bytesArray[index] = (byte) (setValue & 0xFF);
        bytesArray[(index + 1)] = (byte) (setValue >> 8 & 0xFF);
    }

    public static void byteArraySetWordBe(byte[] bytesArray, int setValue, int index) {
        bytesArray[index] = (byte) (setValue >> 8 & 0xFF);
        bytesArray[(index + 1)] = (byte) (setValue & 0xFF);
    }

    public static void byteArraySetInt(byte[] bytesArray, int setValue, int index) {
        bytesArray[index] = (byte) (setValue & 0xFF);
        bytesArray[(index + 1)] = (byte) (setValue >> 8 & 0xFF);
        bytesArray[(index + 2)] = (byte) (setValue >> 16 & 0xFF);
        bytesArray[(index + 3)] = (byte) (setValue >> 24 & 0xFF);
    }

    public static void byteArraySetIntBe(byte[] bytesArray, int setValue, int index) {
        bytesArray[index] = (byte) (setValue >> 24 & 0xFF);
        bytesArray[(index + 1)] = (byte) (setValue >> 16 & 0xFF);
        bytesArray[(index + 2)] = (byte) (setValue >> 8 & 0xFF);
        bytesArray[(index + 3)] = (byte) (setValue & 0xFF);
    }

    public static void delayms(int ms) {
        if (ms <= 0)
            return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public static Bitmap bitmapRotate(Bitmap b, int degrees) {
        if ((degrees != 0) && (b != null)) {
            Matrix m = new Matrix();
            m.setRotate(degrees, b.getWidth() / 2.0F, b.getHeight() / 2.0F);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);

                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
            }
        }
        return b;
    }

    public static boolean isAscii(char ch) {
        return (ch <= '');
    }

    public static boolean isAscii(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (!(isAscii(text.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAsciiChar(String text) {
        for (int i = 0; i < text.length(); ++i) {
            if (isAscii(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
