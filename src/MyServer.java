// Использован https://java-course.ru/begin/networking/
// Cервер ЖДЕТ запросы.
// Все пакеты приходящие на один порт, группируются для каждого клиента.
// Это выглядит так, что каждый объект типа Socket изолирован от других.
// Для записи используем PrintWrite, он сам умеет преобразовывать строки в байты.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
Запустить сервер.
Убедитесь, что он вывел надпись “Waiting for a connection on 1777”
Прервать работу сервера можно принудительно.

Waiting for a connection on 1777
Server returns: Было получено сообщение: Тестовая строка для передачи
Server returns: Было получено сообщение: bye
Waiting for a connection on 1777
...

Запустить клиент.
Программа должна послать/принять сообщения и закончить свою работу.

Client is started
Server returns: Тестовая строка для передачи


 */
public class MyServer {
    public static void main(String[] args) {
        // Определяем номер порта, который будет "слушать" сервер
        int port = 1777;

        try {
            // Открыть серверный сокет (ServerSocket)
            ServerSocket servSocket = new ServerSocket(port);

            // Входим в бесконечный цикл - ожидаем соединения
            while (true) {
                System.out.println("Waiting for a connection on " + port);

                // Получив соединение начинаем работать с сокетом
                Socket fromClientSocket = servSocket.accept();

                // Работаем с потоками ввода-вывода,
                // используем блок try(){} - catch автоматически завершающиий потоки ввода-вывода
                // при выходе из блока.
                // Потоки ввода-вывода должны наследовать интерфейс AutoCloseable
                try (
                        Socket localSocket = fromClientSocket;
                        PrintWriter printWriter = new PrintWriter(localSocket.getOutputStream(), true);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()))
                ) {

                    // Читаем сообщения от клиента
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        // Печатаем сообщение
                        System.out.println("The message: " + str);

                        // Ожидаем сообщение от клиента с содержанием "bye" для прекращения цикла обмена.
                        if (!str.equals("bye")) {
                            // Посылаем клиенту ответ
                            str = "Server returns: Было получено сообщение:" + str;
                            printWriter.println(str);

                        } else {
                            // Завершаем цикл обмена.
                            // Отправляем клиенту сообщение окончания сеанса "bye".
                            printWriter.println("bye");
                            // Завершаем цикл
                            break;
                        }
                    }
                } catch (IOException ex) {
                    //Вывод трассировки ошибки в поток вывода консоли System.out.
                    ex.printStackTrace(System.out);
                }
            }

        } catch (IOException ex) {
            //Вывод трассировки ошибки в поток вывода консоли System.out.
            ex.printStackTrace(System.out);
        }
    }
}


class MyClient {
    public static void main(String args[]) throws Exception {
        // Определяем номер порта, на котором нас ожидает сервер для ответа
        int portNumber = 1777;
        // Подготавливаем строку для запроса - просто строка
        String str = "Тестовая строка для передачи";

        // Пишем, что стартовали клиент
        System.out.println("Client is started");

        // Открыть сокет (Socket) для обращения к локальному компьютеру
        // Сервер мы будем запускать на этом же компьютере
        // Это специальный класс для сетевого взаимодействия c клиентской стороны
        Socket socket = new Socket("127.0.0.1", portNumber);

        // Создать поток для чтения символов из сокета
        // Для этого надо открыть поток сокета - socket.getInputStream()
        // Потом преобразовать его в поток символов - new InputStreamReader
        // И уже потом сделать его читателем строк - BufferedReader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Создать поток для записи символов в сокет
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        // Отправляем тестовую строку в сокет
        printWriter.println(str);

        // Входим в цикл чтения, что нам ответил сервер
        while ((str = bufferedReader.readLine()) != null) {
            // Если пришел ответ “bye”, то заканчиваем цикл
            if (str.equals("bye")) {
                break;
            }
            // Печатаем ответ от сервера на консоль для проверки
            System.out.println(str);
            // Посылаем ему "bye" для окончания "разговора"
            printWriter.println("bye");
        }

        //закрываем
        bufferedReader.close();
        printWriter.close();
        socket.close();

        System.out.println("Client is finished");
    }
}