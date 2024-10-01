package main.java.application;

import main.java.editor.ToDoList;
import main.java.factories.RepositoryFactory;
import main.java.factories.XMLRepositoryFactory;
import main.java.repositories.Repository;

import java.io.File;
import java.util.Scanner;

public class ApplicationRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RepositoryFactory repositoryFactory = new XMLRepositoryFactory(new File("src/main/resources/toDoList.xml"));
        Repository repository = repositoryFactory.createRepository();

        ToDoList toDoList = new ToDoList(repository, scanner);

        boolean running = true;
        while (running) {
            System.out.print("Введите команду (введите 'help' для получения списка команд): ");
            String command = scanner.nextLine().trim();

            // Проверка на выход из цикла
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Выход из программы...");
                running = false;
            }

            if (command.startsWith("help")) {
                System.out.printf("%-15s %s%n", "new", "Добавить новую задачу");
                System.out.printf("  Аргументы: %s%n", "заголовок, описание, важность и срок задачи через запятую\n");

                System.out.printf("%-15s %s%n", "complete", "Пометить задачу как выполненную");
                System.out.printf("  Аргументы: %s%n", "id\n");

                System.out.printf("%-15s %s%n", "edit", "Измененить задачу");
                System.out.printf("  Аргументы: %s%n", "id\n");

                System.out.printf("%-15s %s%n", "list", "Вывести задачи");
                System.out.printf("  Аргументы: %s%n", "\n -s new,\n -s done,\n -s in_progress\n");

                System.out.printf("%-15s %s%n", "remove", "Удалить задачу");
                System.out.printf("  Аргументы: %s%n", "id\n");
            } else if (command.startsWith("new")) {

                toDoList.executeCommand("create");

            } else if (command.startsWith("edit")) {

                toDoList.executeCommand("edit");

            } else if (command.startsWith("remove")) {

                toDoList.executeCommand("delete");

            } else if (command.startsWith("list")) {

                toDoList.executeCommand("selectAll");

            } else if (command.startsWith("complete")) {

                toDoList.executeCommand("complete");

            } else {
                System.out.println("Неизвестная команда. Пожалуйста, попробуйте снова.");
            }
        }
        scanner.close();
    }
}
