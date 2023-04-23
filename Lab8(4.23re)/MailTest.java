import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailTest {

    public static void main(String[] args) throws MessagingException, IOException {
        // read properties
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("./Lab8(4.23re)/mail.properties"))) {
            props.load(in);
        }

        // read message info
        List<String> lines = Files.readAllLines(Paths.get("./Lab8(4.23re)/message.txt"),
                StandardCharsets.UTF_8);

        String from = lines.get(0);
        String to = lines.get(1);
        String subject = lines.get(2);

        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < lines.size(); i++) {
            builder.append(lines.get(i));
            builder.append("\n");
        }

        // read password for your email account
        System.out.println("Password: ");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.next();

        Session mailSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(mailSession);
            // note 1: check the MimeMessage API to figure out how to set the sender, receiver, subject, and email body
            message.setSender(new InternetAddress(from));
            message.setRecipients(RecipientType.TO, to);
            message.setSubject(subject);
            message.setText(builder.toString());

            // TODO 2: check the Session API to figure out how to connect to the mail server and send the message
            
            Transport.send(message);
            System.out.println("Already sent.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}