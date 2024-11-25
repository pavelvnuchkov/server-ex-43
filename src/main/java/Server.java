import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(new BufferedReader(new FileReader("src/main/resources/settings.txt")).readLine()));
        ) {
            System.out.println("Сервер стартовал!");
            List<Socket> listClientSocket = new ArrayList<>();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                listClientSocket.add(clientSocket);

                Thread thread = new Thread(() -> {
                    try (Logger logger = new Logger("server")) {
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        writer.println("Введите своё имя!");
                        String name = reader.readLine();
                        logger.addMessage("Подключился к чату " + name);
                        writer.println("Вы подключились к чату под именем " + name);
                        while (true) {
                            String messageClient = processingMessage(writer, reader, listClientSocket, clientSocket, name, logger);
                            if (messageClient.equals("/exit")) {
                                break;
                            }
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String processingMessage(PrintWriter writer, BufferedReader reader, List<Socket> listClientSocket, Socket clientSocket, String name, Logger logger) throws IOException {

        String messageClient = reader.readLine();
        if (messageClient.equals("/exit")) {
            writer.println(messageClient);
            listClientSocket.remove(clientSocket);
            logger.addMessage("Пользователь " + name + " вышел из чата");
            return "/exit";
        } else {
            String message = name + " "
                    + LocalDateTime.now() + " "
                    + messageClient;
            logger.addMessage(message);
            System.out.println(message);
            for (Socket socket : listClientSocket) {
                if (!clientSocket.equals(socket)) {
                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(message);
                }
            }
            return messageClient;
        }
    }
}
