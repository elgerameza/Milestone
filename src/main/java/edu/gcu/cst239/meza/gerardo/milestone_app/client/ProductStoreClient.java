package edu.gcu.cst239.meza.gerardo.milestone_app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Sends console commands to the Product Store server and displays its responses.
 */
public class ProductStoreClient {

    /**
     * Connects to the local Product Store server on port 5050.
     *
     * @param args command-line arguments, currently unused
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5050);
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the Product Store on localhost:5050");
            System.out.println("Commands: ALL, GET id, FIND text, DELETE id, SORT NAME, SORT PRICE");
            System.out.println("Enter . to disconnect.");

            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String command = scanner.nextLine().strip();
                if (command.equals(".")) {
                    break;
                }

                output.println(command);
                String response = input.readLine();

                if (response == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }

                System.out.println(response);
            }
        } catch (IOException ex) {
            System.err.println("Product Store client error: " + ex.getMessage());
        }
    }
}