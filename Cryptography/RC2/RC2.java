import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RC2 {
    public static void main(String[] args) {
        String text = "DOMASHKA";
        String key = "SC";
        System.out.println("Given text: " + text);
        System.out.println("Given key: " + key);

        String binaryText = new BigInteger(text.getBytes(StandardCharsets.UTF_8)).toString(2);
        binaryText = String.format("%64s", binaryText).replaceAll(" ", "0");
        String binaryKey = new BigInteger(key.getBytes(StandardCharsets.UTF_8)).toString(2);
        binaryKey = String.format("%16s", binaryKey).replaceAll(" ", "0");
        System.out.println("Given word in binary: " + prettyBinary(binaryText, 8, " "));
        System.out.println("Given key in binary: " + prettyBinary(binaryKey, 8, " "));

        StringBuilder a1 = new StringBuilder();
        for (int i = 0; i < 16; ++i) {
            a1.append(binaryText.charAt(i));
        }
        String a = a1.toString();

        StringBuilder b1 = new StringBuilder();
        for (int i = 16; i < 32; ++i) {
            b1.append(binaryText.charAt(i));
        }
        String b = b1.toString();

        StringBuilder c1 = new StringBuilder();
        for (int i = 32; i < 48; ++i) {
            c1.append(binaryText.charAt(i));
        }
        String c = c1.toString();

        StringBuilder d1 = new StringBuilder();
        for (int i = 48; i < 64; ++i) {
            d1.append(binaryText.charAt(i));
        }
        String d = d1.toString();

        System.out.println("a = " + prettyBinary(a, 8, " "));
        System.out.println("b = " + prettyBinary(b, 8, " "));
        System.out.println("c = " + prettyBinary(c, 8, " "));
        System.out.println("d = " + prettyBinary(d, 8, " "));

        // Step 1
        int part1Int = (Integer.parseInt(a, 2) + Integer.parseInt(binaryKey, 2));
        String part1 = Integer.toBinaryString(part1Int);
        part1 = String.format("%32s", part1).replaceAll(" ", "0");
        System.out.println("\nStep 1: a + key = " + part1Int + " = " + part1);

        // Step 2
        int part2Int = Integer.parseInt(c, 2) & Integer.parseInt(d, 2);
        String part2 = Integer.toBinaryString(part2Int);
        part2 = String.format("%32s", part2).replaceAll(" ", "0");
        System.out.println("\nStep 2: c ^ d = " + part2);

        //Step 3
        int part3Int = ~Integer.parseInt(d, 2) & Integer.parseInt(b, 2);
        String part3 = Integer.toBinaryString(part3Int);
        part3 = String.format("%32s", part3).replaceAll(" ", "0");
        System.out.println("\nStep 3: (not d) ^ b = " + part3);

        //Step 4
        int part4Int = part2Int + part3Int;
        String part4 = Integer.toBinaryString(part4Int);
        part4 = String.format("%32s", part4).replaceAll(" ", "0");
        System.out.println("\nStep 4: (c ^ d) + ((not d) ^ b) = " + part4);

        // Step 5
        int part5Int = part1Int + part4Int;
        String part5 = Integer.toBinaryString(part5Int);
        part5 = String.format("%32s", part5).replaceAll(" ", "0");
        System.out.println("\nStep 5: a + key + (c ^ d) + ((not d) ^ b) = " + part5);

        // Step 6
        int part6Int = part5Int << 4;
        String part6 = Integer.toBinaryString(part6Int);
        part6 = String.format("%32s", part6).replaceAll(" ", "0");
        System.out.println("\nStep 6: part5 <<< 4 = " + part6);

        // Step 7
        a = part6;
        System.out.println("b => " + b + "\n" + "c => " + c + "\n" + "d => " + d + "\n" + "a => " + a);

        // Step 8
        String bcd = b.concat(c).concat(d);
        String encryption = bcd.concat(a);
        System.out.println("Concatenating b, c, d, a => " + prettyBinary(encryption, 8, " "));

        // Result
        String result = "";
        int aDecimal = Integer.parseInt(a, 2);
        String aHex = Integer.toString(aDecimal, 16);
        for (int i = 0; i < bcd.length()/8; i++) {
            int az = Integer.parseInt(bcd.substring(8*i,(i+1)*8),2);
            result += (char)(az);
        }
        System.out.println("Result = " + "\"" + result + "\"" + " + \"" + aHex + "\"");

        // Decryption
        System.out.println("\n===================DECRYPTION===================");

        //Step 1
        part5Int = part6Int >>> 4;
        part5 = Integer.toBinaryString(part5Int);
        part5 = String.format("%32s", part5).replaceAll(" ", "0");
        System.out.println("Step 1: part6 >>> 4 = " + part5);

        // Step 2
        System.out.println("\nStep 2: c ^ d = " + part2);

        //Step 3
        System.out.println("\nStep 3: (not d) ^ b = " + part3);

        //Step 4
        System.out.println("\nStep 4: (c ^ d) + ((not d) ^ b) = " + part4);

        // Step 5
        int part5IntDecrypt = part5Int -  part4Int;
        String part5Decrypt = Integer.toBinaryString(part5IntDecrypt);
        part5Decrypt = String.format("%32s", part5Decrypt).replaceAll(" ", "0");
        System.out.println("\nStep 5: (a - (b ^ (not d) + (c ^ d) )) => " + part5IntDecrypt + " = " + part5Decrypt);

        // Step 6
        part6Int = part5IntDecrypt - Integer.parseInt(binaryKey, 2);
        part6 = Integer.toBinaryString(part6Int);
        part6 = String.format("%16s", part6).replaceAll(" ", "0");
        System.out.println("\nStep 6: part5 - CV => " + part6Int + " = " + part6);

        // Step 7
        result = "";
        String abcd = part6.concat(b).concat(c).concat(d);
        for (int i = 0; i < abcd.length()/8; i++) {
            int az = Integer.parseInt(abcd.substring(8*i,(i+1)*8),2);
            result += (char)(az);
        }
        System.out.println("Result = " + "\"" + result + "\"");
    }

    public static String prettyBinary(String binary, int blockSize, String separator) {
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }
        return String.join(separator, result);
    }
}
