import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class SocketTest {

    public static void main(String[] args) throws Exception {
//        test1();

        String host1 = "www.baidu.com";
//        fetchWeb(host1);

        String host2 = "https://www.sustech.edu.cn/";
        String host3 = "https://newshub.sustech.edu.cn/ztjy/";
//        readByURLConnection(host3);
        readByHttpClient(host3);
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

    final static int HTTP_PORT = 80;

    private static void fetchWeb(String host) {
        try (Socket s = new Socket(host, HTTP_PORT)) {
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            Scanner in = new Scanner(is);
            PrintWriter out = new PrintWriter(os);

            String resource = "/";
            String command = "GET " + resource + " HTTP/1.1\n" +
                    "Host: " + host + "\n\n";
            out.println(command);
            out.flush();

            while (in.hasNextLine()) {
                String input = in.nextLine();
                System.out.println(input);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readByURLConnection(String url) throws IOException {
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) conn;

        int code = httpConn.getResponseCode();
        String msg = httpConn.getResponseMessage();
        System.out.println(code + " " + msg);
        if (code != HttpURLConnection.HTTP_OK) {
            return;
        }

        InputStream inputStream = httpConn.getInputStream();
        Scanner in = new Scanner(inputStream);
        while (in.hasNextLine()) {
            System.out.println(in.nextLine());
        }
    }

    private static void readByHttpClient(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
