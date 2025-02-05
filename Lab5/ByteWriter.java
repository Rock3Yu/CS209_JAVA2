import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class ByteWriter {

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(Charset.defaultCharset());
        try (FileOutputStream fos = new FileOutputStream("Lab5/ByteWriter_output.txt")) {

            String s = "计算机系统";
            byte[] bytes = s.getBytes();

            fos.write(bytes);

        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }

    }

}
