package main.java.commands;

import main.java.models.StatusOfTask;
import main.java.models.Task;
import main.java.repositories.Repository;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class EditCommand extends Command {
    Repository repository;

    public EditCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Scanner scanner) {
        int id = 0;
        try{
            System.out.print("Введите ID: ");
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e){
            System.out.println("Значение должно быть целым числом и не может быть пустым");
        }

        Task task = repository.findTaskById(id);
        if(task == null){
            System.out.println("Задача не найдена");
            return;
        }
        System.out.println("Найдена задача: " + task);

        try {
            String newCaption = inputParameters("заголовок", scanner);
            if (newCaption.length() > 50) {
                throw new InvalidParameterException();
            }
            String newDescription = inputParameters("описание", scanner);
            String newPriorityInput = inputParameters("приоритет", scanner);
            int newPriority = newPriorityInput.isEmpty()
                    ? 0
                    : Integer.parseInt(newPriorityInput);
            if ((newPriority > 10) || (newPriority < 0)) {
                throw new NumberFormatException();
            }
            String newDeadlineInput = inputParameters("срок", scanner);
            LocalDate newDeadline = newDeadlineInput.isEmpty()
                    ? null
                    : LocalDate.parse(newDeadlineInput);
            String newStatusInput = inputParameters("статус", scanner);
            StatusOfTask newStatus = newStatusInput.isEmpty()
                    ? task.getStatusOfTask()
                    : StatusOfTask.valueOf(newStatusInput.toUpperCase());

            if(task.getCompletionDate() != null){
                String newCompletionDateInput = inputParameters("дата выполнения", scanner);
                LocalDate newCompletionDate = newCompletionDateInput.isEmpty()
                        ? null
                        : LocalDate.parse(newCompletionDateInput);
                task.setCompletionDate(newCompletionDate);
            }

            task.setCaption(newCaption);
            task.setDescription(newDescription);
            task.setPriority(newPriority);
            task.setDeadline(newDeadline);
            task.setStatusOfTask(newStatus);

            repository.edit(task);
            System.out.println("Задача успешно отредактирована");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ввод должен быть целым числом. Приоритет в диапазоне от 0 до 10");
        } catch (DateTimeParseException e) {
            System.out.println("Некорректный формат ввода даты! Пример: YYYY-MM-DD. Попробуйте еще раз");
        } catch (InvalidParameterException e) {
            System.out.println("Заголовок задачи не может быть пустым или содержать более 50 символов");
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ввода статуса! Возможные статусы: new, in_progress, done. Попробуйте еще раз");
        } catch (NullPointerException e) {
            System.out.println("Новое значение не может быть пустым");
        }
    }

}
