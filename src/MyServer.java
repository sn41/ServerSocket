// использован https://java-course.ru/begin/networking/
// Cервер ЖДЕТ запросы.
// Все пакеты приходящие на порт, деляться по клиенту.
// Это выглядит так, что каждый объект типа Socket работает сам по себе.
// Для записи будем использовать не OutputStream, а PrintWriter,
// объект этого класса сам преобразовывает строку в байты.

/*
Клиент: “Тестовая строка для передачи”
Сервер: "Server returns: Тестовая строка для передачи"
Клиент: “bye”
Сервер: “bye”
* */
public class Main {
    public static void main(String[] args) {

    }
}