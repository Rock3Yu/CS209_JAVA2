import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleTcpClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        OutputStream os = socket.getOutputStream();
        // use byte stream
        byte[] msg = "Hello server!".getBytes();
        os.write(msg);
        System.out.println("Client's message sent.");
        // closing the OutputStream will close the associated socket
        os.close();
    }

}
