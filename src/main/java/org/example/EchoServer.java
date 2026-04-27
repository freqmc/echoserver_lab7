package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class EchoServer {
    private static final int PORT = 12345; // Порт для прослушивания
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("Сервер запущен на порту " + PORT);

        // try-with-resources для гарантированного закрытия ServerSocket

        try  (ServerSocket serverSocket = new ServerSocket(PORT)) {


            // Бесконечный цикл для приема клиентов (пока сервер работает)
            while (true) {
                // accept() блокирует, пока клиент не подключится
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключился клиент: " + clientSocket.getInetAddress());

                // ПОКА Обрабатываем клиента в этом же потоке
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Ошибка в работе сервера: " + e.getMessage());
        }
    }

    // метод обработки сообщений
    private static void handleClient(Socket clientSocket) {


        try (clientSocket;
             //поток чтения и буферизауии сообщения от клиента
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             //поток для отправки сообщения
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            // Читаем строку от клиента. readLine() блокируется, пока не получит '\n' или EOF.
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + inputLine);
                // Простейший эхо-ответ: отправляем строку обратно
                String time = "[" + LocalTime.now().format(TIME_FORMATTER) + "]";
                String response = time + inputLine; // Базовая логика
                out.println(response); // Отправляем ответ клиенту
            }

            System.out.println("Клиент отключился: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        }

    }
}