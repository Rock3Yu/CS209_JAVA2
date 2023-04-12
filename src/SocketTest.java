import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTest {

    public static void main(String[] args) throws Exception {
        test1();
    }

    private static void test1() throws IOException {
        ServerSocket server = new ServerSocket(3027);
        Socket c1 = new Socket("10.15.239.26", 3027);
        try (server; c1) {
            Socket c2 = server.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
