package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 13000; // �������� ����

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("TCP ������ ������� � ������ " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // ��������� �������� ����������
                System.out.println("������ �������������: " + clientSocket.getInetAddress().getHostAddress());

                // ������� � ��������� ����� ����� ��� ������� �������
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("������ �����������: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("�������� �� �������: " + inputLine);

                if ("hello".equalsIgnoreCase(inputLine)) {
                    out.println("Hello, Client!");
                } else if ("exit".equalsIgnoreCase(inputLine)) {
                    System.out.println("������ ������������: " + clientSocket.getInetAddress().getHostAddress());
                    break; // ��������� ���������� � ��������
                } else {
                    out.println("����������� �������");
                }
            }
        } catch (IOException e) {
            System.err.println("������ ��������� ��� �����������: " + e.getMessage());
        } finally {
            try {
                clientSocket.close(); // ��������� ����� (���� �� ���� ������� ������ handle)
            } catch (IOException e) {
                System.err.println("������ ��� �������� ����������� ������: " + e.getMessage());
            }
        }
    }
}