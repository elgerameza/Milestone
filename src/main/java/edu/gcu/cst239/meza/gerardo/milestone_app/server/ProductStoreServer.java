package edu.gcu.cst239.meza.gerardo.milestone_app.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import edu.gcu.cst239.meza.gerardo.milestone_app.services.ProductCommandService;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;

/**
 * Serves Product Store commands over TCP, one client at a time.
 */
public class ProductStoreServer {
    private final ProductCommandService commandService;

    /**
     * Creates a server that delegates commands to the supplied service.
     *
     * @param commandService the service that processes inventory commands
     */
    public ProductStoreServer(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    /**
     * Listens for client connections and handles each session in order.
     *
     * @param port the TCP port to listen on
     * @throws IOException if the listener cannot be opened or accept a connection
     */
    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Product Store server listening on port " + port);

            while (true) {
                handleClient(serverSocket.accept());
            }
        }
    }

    private void handleClient(Socket socket) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());

        try (socket;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            String command;
            while ((command = input.readLine()) != null) {
                output.println(commandService.processMessage(command));
            }
        } catch (IOException ex) {
            System.err.println("Client connection error: " + ex.getMessage());
        } finally {
            System.out.println("Client disconnected");
        }
    }

    /**
     * Loads the store inventory and starts the server on port 5050.
     *
     * @param args command-line arguments, currently unused
     */
    public static void main(String[] args) {
        try {
            StoreFront storeFront = new StoreFront();
            ProductCommandService commandService =
                    new ProductCommandService(storeFront.getInventoryManager());

            new ProductStoreServer(commandService).start(5050);
        } catch (IOException ex) {
            System.err.println("Product Store server error: " + ex.getMessage());
        }
    }
}