//JS
package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 13000; // Çàäàííûé ïîðò

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP ñåðâåð çàïóùåí ñ ïîðòîì " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Ïðèíèìàåì âõîäÿùåå ñîåäèíåíèå
                System.out.println("Êëèåíò ïðèñîåäèíèëñÿ: " + clientSocket.getInetAddress().getHostAddress());

                // Ñîçäàåì è çàïóñêàåì íîâûé ïîòîê äëÿ êàæäîãî êëèåíòà
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Îøèáêà ïîäêëþ÷åíèÿ: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Ïîëó÷åíî îò êëèåíòà: " + inputLine);

                if ("hello".equalsIgnoreCase(inputLine)) {
                    out.println("Hello, Client!");
                } else if ("exit".equalsIgnoreCase(inputLine)) {
                    System.out.println("Êëèåíò îòñîåäèíèëñÿ: " + clientSocket.getInetAddress().getHostAddress());
                    break; // Çàêðûâàåì ñîåäèíåíèå ñ êëèåíòîì
                } else {
                    out.println("Íåèçâåñòíàÿ êîìàíäà");
                }
            }
        } catch (IOException e) {
            System.err.println("Îøèáêà îáðàáîòêè ïðè ïîäêëþ÷åíèè: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Çàêðûâàåì ñîêåò (åñëè íå áûëî çàêðûòî âíóòðè handle)
            } catch (IOException e) {
                System.err.println("Îøèáêà ïðè çàêðûòèè êëèåíòñêîãî ñîêåòà: " + e.getMessage());
            }
        }
    }
}
