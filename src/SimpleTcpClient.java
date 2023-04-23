import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleTcpClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 1234);
        Socket socket1 = new Socket("localhost", 1234);

        OutputStream os = socket.getOutputStream();
        os.write("1\n".getBytes());


        Thread.sleep(3000);

        OutputStream os2 = socket1.getOutputStream();

        os2.write("2\n".getBytes());
        System.out.println("Client's message sent.");
        Thread.sleep(5000);

        os.close();
    }

}
