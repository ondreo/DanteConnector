package pl.ondreo.util;

import java.util.HexFormat;

public abstract class ConverterUtil {

    private ConverterUtil() {

    }

    /**
     * Method converting an ASCII string to a hexadecimal string, e.g.,
     * the string "main-pc-name" will be converted to "6d61696e2d70632d6e616d65".
     *
     * @param asciiString ASCII string
     * @return hexadecimal string
     */
    public static String convertAsciiStringtoHexString(String asciiString) {
        return HexFormat.of().formatHex(asciiString.getBytes());
    }

    /**
     * Method converting a hexadecimal string to a byte array.
     *
     * @param hexString hexadecimal string
     * @return byte array
     */
    public static byte[] convertHexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}
