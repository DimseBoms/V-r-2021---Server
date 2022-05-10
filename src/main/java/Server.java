import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // Setter i gang behandlingen av en ny klient og initialiserer en ny tråd med KlientBehandling
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("En ny klient har koblet til");
                KlientBehandling håndterKlient = new KlientBehandling(socket);
                Thread klientTråd = new Thread(håndterKlient);
                klientTråd.start();
            }
        } catch (IOException e) {
            lukkServerSocket();
        }
    }

    // Forsøker å lukke serveren på ryddig vis
    public void lukkServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Starter tjeneren
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
