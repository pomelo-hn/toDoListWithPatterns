package main.java.editor;

import main.java.commands.*;
import main.java.repositories.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ToDoList {
    private Map<String, Command> commands;
    private Repository repository;
    private Scanner scanner;

    public ToDoList(Repository repository, Scanner scanner) {
        this.repository = repository;
        this.scanner = scanner;
        this.commands = new HashMap<>();
        initializeCommands();
    }

    private void initializeCommands() {
        addCommand("create", new CreateCommand(repository));

        addCommand("edit", new EditCommand(repository));

        addCommand("delete", new DeleteCommand(repository));

        addCommand("selectAll", new SelectCommand(repository));

        addCommand("complete", new CompleteCommand(repository));
    }

    private void addCommand(String name, Command command) {
        commands.put(name, command);
    }

    public void executeCommand(String name) {
        Command command = commands.get(name);
        if (command != null) {
            command.execute(scanner);
        } else {
            System.out.println("Команда не найдена: " + name);
        }
    }
}
