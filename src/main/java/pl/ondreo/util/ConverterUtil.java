package pl.ondreo.util;

import java.util.HexFormat;

public abstract class ConverterUtil {

    private ConverterUtil() {

    }

    /**
     * Metoda konwertująca ciąg znaków ASCII do ciągu znaków w postaci szesnastkowej, np.
     * ciąg znaków "main-pc-name" zamieni na "6d61696e2d70632d6e616d65".
     *
     * @param asciiString ciąg znaków ASCII
     * @return ciąg znaków w postaci szesnastkowej
     */
    public static String convertAsciiStringtoHexString(String asciiString) {
        return HexFormat.of().formatHex(asciiString.getBytes());
    }

    /**
     * Metoda konwertująca szesnastkowy ciąg znaków na tablicę bajtów.
     *
     * @param hexString ciąg znaków postaci szesnastkowej
     * @return tablica bajtów
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
