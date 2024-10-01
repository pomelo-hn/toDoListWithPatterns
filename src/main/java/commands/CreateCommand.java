package main.java.commands;

import main.java.models.Task;
import main.java.repositories.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class CreateCommand extends Command {
    Repository repository;

    public CreateCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Scanner scanner) {

        try {
            String newCaption = inputParameters("заголовок", scanner, false);
            if (newCaption.length() > 50) {
                throw new IllegalArgumentException();
            }
            String newDescription = inputParameters("описание", scanner, false);
            int newPriority = Integer.parseInt(inputParameters("приоритет", scanner, false));
            if ((newPriority > 10) || (newPriority < 0)) {
                throw new NumberFormatException();
            }
            LocalDate newDeadline = LocalDate.parse(inputParameters("срок", scanner, false));

            Task newTask = new Task(newCaption, newDescription, newPriority, newDeadline);

            Task task = repository.create(newTask);
            System.out.println("Создана задача. " + task.toString());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ввод должен быть целым числом. Приоритет в диапазоне от 0 до 10");
        } catch (DateTimeParseException e) {
            System.out.println("Некорректный формат ввода даты! Пример: YYYY-MM-DD. Попробуйте еще раз");
        } catch (IllegalArgumentException e) {
            System.out.println("Заголовок задачи не может быть пустым или содержать более 50 символов");
        } catch (NullPointerException e) {
            System.out.println("Новое значение не может быть пустым");
        }
    }

}
