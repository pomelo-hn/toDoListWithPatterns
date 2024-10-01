package main.java.commands;

import main.java.repositories.Repository;

import java.util.Scanner;

public class CompleteCommand extends Command {
    Repository repository;

    public CompleteCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите ID: ");
        int id = 0;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Значение должно быть целым числом и не может быть пустым");
        }


        if (!repository.complete(id)) {
            System.out.println("Задача не найдена или уже выполнена");
        } else {
            System.out.println("Задача " + id + " успешно отмечена как выполненная!");
        }
    }
}
