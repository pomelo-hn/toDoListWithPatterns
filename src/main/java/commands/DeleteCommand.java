package main.java.commands;

import main.java.repositories.Repository;

import java.util.Scanner;

public class DeleteCommand extends Command {
    Repository repository;

    public DeleteCommand(Repository repository) {
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


        if (!repository.delete(id)) {
            System.out.println("Задача не найдена");
        } else {
            System.out.println("Задача " + id + " успешно удалена!");
        }
    }
}
