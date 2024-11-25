import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ServerTest {

    @Test
    void processingMessage() {
        String expected = "Хорошо";
        PrintWriter writer = new PrintWriter(new ByteArrayOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(expected.getBytes())));
        List<Socket> list = new ArrayList<>();
        Socket socket = new Socket();
        String name = "Павел";

        Logger logger = new Logger("serverTest");
        try {
            Server.processingMessage(writer, reader, list, socket, name, logger);
            Assertions.assertTrue(new BufferedReader(new FileReader("serverTest.log")).readLine().contains(expected));
            new FileOutputStream("serverTest.log", false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}