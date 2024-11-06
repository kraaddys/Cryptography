import java.util.Arrays;
public class TigerHashExample {
    // Векторы инициализации
    private static final byte[][] IV = {
            {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, 0x11, 0x22}, // IV1
            {0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA}, // IV2
            {(byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE, (byte) 0xFF, (byte) 0xAA, (byte) 0xBB, (byte) 0xCC} // IV3
    };
    // S-блоки для преобразования байтов
    private static final int[] S_BLOCK = {
            0xA2, 0xB1, 0xC3, 0xB1, 0xA8, 0xB1, 0xD4, 0xE5 // Замены для байтов "S C t e c h n o"
    };
    public static void main(String[] args) {
        String input = "SCtechno";
        // Шаг 1: Преобразование строки в байты
        byte[] bytes = input.getBytes();
        System.out.println("Исходные байты: " + Arrays.toString(bytes));
        // Шаг 2: Применение S-блоков
        byte[] sBoxResult = applySBlocks(bytes);
        System.out.println("После S-блоков: " + toHexString(sBoxResult));
        // Шаг 3: Применение перестановок
        byte[] permuted = applyPermutation(sBoxResult);
        System.out.println("После перестановок: " + toHexString(permuted));
        // Шаг 4: Применение побитовых сдвигов
        byte[] shifted = applyBitShifts(permuted);
        System.out.println("После сдвигов: " + toHexString(shifted));
        // Шаг 5: Операция XOR с IV побайтово
        byte[][] xorResult = applyByteWiseXOR(shifted);
        for (int i = 0; i < xorResult.length; i++) {
            System.out.println("После XOR с IV" + (i + 1) + ": " +
                    toHexString(xorResult[i]));
        }
    }
    // Применение S-блоков
    private static byte[] applySBlocks(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) S_BLOCK[i]; // Применяем заранее определенные замены (для демонстрации)
        }
        return result;
    }
    // Применение перестановок
    private static byte[] applyPermutation(byte[] input) {
        byte[] result = new byte[input.length];
        // Пример перестановки:
        result[0] = input[2]; // 3-й байт на 1-е место
        result[1] = input[4]; // 5-й байт на 2-е место
        result[2] = input[6]; // 7-й байт на 3-е место
        result[3] = input[0]; // 1-й байт на 4-е место
        result[4] = input[7]; // 8-й байт на 5-е место
        result[5] = input[5]; // 6-й байт на 6-е место
        result[6] = input[1]; // 2-й байт на 7-е место
        result[7] = input[3]; // 4-й байт на 8-е место
        return result;
    }
    // Применение побитовых сдвигов (сдвиг влево на 2 бита)
    private static byte[] applyBitShifts(byte[] input) {
        byte[] result = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            result[i] = (byte) ((input[i] & 0xFF) << 2);
        }
        return result;
    }
    // Применение побайтового XOR с векторами инициализации IV
    private static byte[][] applyByteWiseXOR(byte[] input) {
        byte[][] xorResult = new byte[3][8]; // 3 результата XOR для каждого IV
        // XOR с первым вектором IV1
        for (int i = 0; i < 8; i++) {
            xorResult[0][i] = (byte) (input[i] ^ IV[0][i]);
        }
        // XOR со вторым вектором IV2
        for (int i = 0; i < 8; i++) {
            xorResult[1][i] = (byte) (input[i] ^ IV[1][i]);
        }
        // XOR с третьим вектором IV3
        for (int i = 0; i < 8; i++) {
            xorResult[2][i] = (byte) (input[i] ^ IV[2][i]);
        }
        return xorResult;
    }
    // Вспомогательный метод для вывода байтов в шестнадцатеричном формате
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
