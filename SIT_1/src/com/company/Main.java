package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 13000; // Заданный порт

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP сервер запущен с портом " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Принимаем входящее соединение
                System.out.println("Клиент присоединился: " + clientSocket.getInetAddress().getHostAddress());

                // Создаем и запускаем новый поток для каждого клиента
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + inputLine);

                if ("hello".equalsIgnoreCase(inputLine)) {
                    out.println("Hello, Client!");
                } else if ("exit".equalsIgnoreCase(inputLine)) {
                    System.out.println("Клиент отсоединился: " + clientSocket.getInetAddress().getHostAddress());
                    break; // Закрываем соединение с клиентом
                } else {
                    out.println("Неизвестная команда");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка обработки при подключении: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // Закрываем сокет (если не было закрыто внутри handle)
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии клиентского сокета: " + e.getMessage());
            }
        }
    }
}