import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Practice6 {

    public static void main(String[] args) throws IOException, InterruptedException {
        // practice6.1
        for_src_zip();
        Thread.sleep(5_000);
        // practice6.2
        for_rt_jar();

    }

    static void for_src_zip() throws IOException {
        InputStream file = new FileInputStream(".\\Lab6\\src.zip");
        InputStream bfile = new BufferedInputStream(file);
        ZipInputStream zfile = new ZipInputStream(bfile);

        ZipEntry ze;
        ArrayList<String> names = new ArrayList<>();
        while ((ze = zfile.getNextEntry()) != null) {
            String tmp = ze.getName();
            if (!tmp.startsWith("java/io/") & !tmp.startsWith("java/nio")) {
                continue;
            }
            if (!tmp.endsWith(".java")) {
                continue;
            }
            names.add(tmp);
        }
        System.out.println("# of .java files in java.io/java.nio: " + names.size());
        names.forEach(System.out::println);
    }

    static void for_rt_jar() throws IOException {
        InputStream file = new FileInputStream(".\\Lab6\\rt.jar");
        InputStream bfile = new BufferedInputStream(file);
        JarInputStream jarfile = new JarInputStream(bfile);

        ZipEntry ze;
        ArrayList<String> names = new ArrayList<>();
        while ((ze = jarfile.getNextEntry()) != null) {
            String tmp = ze.getName();
            if (!tmp.startsWith("java/io/") & !tmp.startsWith("java/nio")) {
                continue;
            }
            if (!tmp.endsWith(".class")) {
                continue;
            }
            names.add(tmp);
        }
        System.out.println("# of .java files in java.io/java.nio: " + names.size());
        names.forEach(System.out::println);
    }

}
