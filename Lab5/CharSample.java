import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CharSample {

    public static void main(String[] args) {
        char c = '计';
        System.out.printf("%c\n", (int) c);
        System.out.printf("Unicode for 计: %X\n", (int) c);

        String str = "计算机"; // UTF-16
        try {
            System.out.print("Java platform default: ");
            byte[] bytes0 = str.getBytes();
            for (byte b : bytes0) {
                System.out.printf("%2X ", b);
            }

            System.out.println();

            System.out.print("GBK: ");
            byte[] bytes1 = str.getBytes("GBK");
            for (byte b : bytes1) {
                System.out.printf("%2X ", b);
            }
            System.out.println();

            System.out.print("UTF_16: ");
            byte[] bytes2 = str.getBytes(StandardCharsets.UTF_16);
            for (byte b : bytes2) {
                System.out.printf("%2X ", b);
            }
            System.out.println();

            System.out.print("UTF_16BE: ");
            byte[] bytes3 = str.getBytes(StandardCharsets.UTF_16BE);
            for (byte b : bytes3) {
                System.out.printf("%2X ", b);
            }
            System.out.println();

            System.out.print("UTF_16LE: ");
            byte[] bytes4 = str.getBytes(StandardCharsets.UTF_16LE);
            for (byte b : bytes4) {
                System.out.printf("%02X ", b);
            }
            System.out.println();
        } catch (UnsupportedEncodingException e) {
            System.out.println("The character encoding is not supported.");
            e.printStackTrace();
        }
    }
}
