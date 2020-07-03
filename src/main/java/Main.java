import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final byte ENQ = 0x5;
    public static final byte ACK = 0x6;
    public static final byte NAK = 0x15;

    public static void main(String[] args) {

        int port = 6868;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                System.out.println("Address = " + socket.getLocalAddress());
                System.out.println("Port = " + socket.getPort());

                InputStream input = socket.getInputStream();
                DataInputStream reader = new DataInputStream(input);

                OutputStream output = socket.getOutputStream();
                DataOutputStream writer = new DataOutputStream(output);

                byte[] answerPositive = new byte[1];
                answerPositive[0] = ACK;

                String convert = "";

                int start = reader.readInt();

                if(start == ENQ)
                writer.write(answerPositive, 0, 1);

                int len = reader.readInt();
                System.out.println("Message length = " + len);
                byte[] data = new byte[len];
                if (len > 0) {
                    reader.readFully(data);
                }
                List<Byte> list = new ArrayList<Byte>();
                for (byte fieldByte : data) {
                    list.add(fieldByte);
                }

                convertStringAndAdd(list, convert);
                System.out.println(list);

                if(data[0] == 0x5)
                    System.out.println("server ready");

                socket.close();
                System.out.println("Connection close!");
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void convertStringAndAdd(List<Byte> result, String field) {
        byte[] fieldBytes = field.getBytes(StandardCharsets.US_ASCII);

        for (byte fieldByte : fieldBytes) {
            result.add(fieldByte);
        }
    }
}