package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class NetworkScanner {
    public static void main(String[] args) {
        String baseIp = "192.168.1."; // Ваша подсеть
        int port = 12345;

        System.out.println("Сканируем сеть на наличие эхо-серверов...");

        for (int i = 1; i <= 254; i++) {
            String ip = baseIp + i;

            //Пропускаем свой IP
            try {
                if (ip.equals(getMyIp())) continue;
            } catch (SocketException e) {
                throw new RuntimeException(e);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), 500); // Таймаут 500ms
                System.out.println("✓ Найден эхо-сервер на " + ip);

                // Тестируем подключение
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("ping");
                String response = in.readLine();
                System.out.println("  Ответ: " + response);

            } catch (IOException e) {
                // Сервер не найден - это нормально
            }
        }
    }

    private static String getMyIp() throws SocketException, UnknownHostException {
        // Возвращает IP текущего компьютера
        return InetAddress.getLocalHost().getHostAddress();
    }
}
