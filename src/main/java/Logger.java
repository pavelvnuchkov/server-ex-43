import java.io.FileWriter;
import java.io.IOException;

public class Logger implements AutoCloseable {
    private FileWriter writer;

    public Logger(String nameFile) {
        String path = nameFile.concat(".log");
        try {
            writer = new FileWriter(path, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }
}
