package main.java.commands;

import main.java.models.StatusOfTask;
import main.java.models.Task;
import main.java.repositories.Repository;

import java.util.List;
import java.util.Scanner;

public class SelectCommand extends Command {
    Repository repository;

    public SelectCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("Введите аргумент (возможные аргументы: -s new, -s done, -s in_progress" +
                " или оставьте поле пустым чтобы вывести все задачи): ");
        String argument = scanner.nextLine();

        List<Task> taskList = repository.selectAll();

        if(argument.trim().isEmpty()){
            for(Task task : taskList){
                System.out.println(task);
            }
        } else if(argument.startsWith("-s")){
            StatusOfTask statusOfTask = StatusOfTask.valueOf(argument.substring(3).toUpperCase());
            List<Task> taskListByStatus = repository.selectByStatus(taskList, statusOfTask);

            if(!taskListByStatus.isEmpty()){
                for(Task task : taskListByStatus){
                    System.out.println(task);
                }
            } else {
                System.out.println("Таких задач сейчас нет");
            }

        } else {
            System.out.println("Аргумент не найден");
        }

    }
}
