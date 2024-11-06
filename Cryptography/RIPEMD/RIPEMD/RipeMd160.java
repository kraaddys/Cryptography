import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.MessageDigest;
import java.security.Security;


public class RipeMd160 {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        String input = "RIPEMD160 Test Message";

        System.out.println("Исходное сообщение: " + input);

        try {
            MessageDigest md = MessageDigest.getInstance("RIPEMD160", "BC");

            byte[] hash = md.digest(input.getBytes());

            System.out.print("Зашифрованное сообщение (хеш): ");
            for (byte b : hash) {
                System.out.printf("%02x", b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

