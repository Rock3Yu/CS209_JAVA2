import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleTcpServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        serverSocket.setReuseAddress(true);
//        Scanner in = new Scanner(System.in);

        System.out.println("Waiting for client.....");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected.");

        InputStream is = socket.getInputStream();
        byte[] buf = new byte[1024];
        int readLen = 0;
        while (true) {
            Thread.sleep(1000);
            System.out.println("-" + socket.isClosed());
            if ((readLen = is.read(buf)) != -1) {
                System.out.println(new String(buf, 0, readLen));
            }
        }

//        is.close();

//        serverSocket.close();
//        System.out.println("over");
    }
}
