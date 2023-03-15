import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * note: How to run?
 * in terminal: 2 steps
 *      javac FileTypeParser.java
 *      java FileTypeParser 1
 * note: How to determine
 * File Type    File Header (Hex)
 * png          89504e47
 * zip or jar   504b0304
 * class        cafebabe
 */

public class FileTypeParser {

    public static void main(String[] args) throws URISyntaxException {
        String path = args[0];
        String[] msgs = new String[]{"Filename: ", "File Header(Hex): ", "File Type: "};
        System.out.println(msgs[0] + path);
        URI uri = Objects.requireNonNull(
                ByteReader.class.getClassLoader().getResource(path)).toURI();
        path = Paths.get(uri).toString();

        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] buffer = new byte[4];
            int byteNum = fis.read(buffer);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < byteNum; i++) {
                stringBuilder.append(String.format("%02x", buffer[i]));
            }
            String str = stringBuilder.toString();
            System.out.println(msgs[1] + str);
            System.out.print(msgs[2]);
            switch (str) {
                case "89504e47" -> System.out.println("png");
                case "504b0304" -> System.out.println("zip or jar");
                case "cafebabe" -> System.out.println("class");
                default -> System.out.println("unknown type");
            }


        } catch (FileNotFoundException e) {
            System.out.println("The pathname does not exist.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Failed or interrupted when doing the I/O operations");
            e.printStackTrace();
        }
    }
}