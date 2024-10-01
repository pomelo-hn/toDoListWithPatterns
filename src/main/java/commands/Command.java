package main.java.commands;

import java.util.Scanner;

public abstract class Command {
    protected String inputParameters(String type, Scanner scanner){
        System.out.println("Введите новое значение " + type + " или нажмите Enter, чтобы оставить без изменений: ");
        return scanner.nextLine();
    }

    protected String inputParameters(String type, Scanner scanner, boolean isTypeEmpty){
        if(!isTypeEmpty){
            System.out.println("Введите новое значение " + type);
            try {
                String input = scanner.nextLine();
                if(input.trim().isEmpty()){
                    throw new NullPointerException();
                } else {
                    return input;
                }
            } catch (NullPointerException _) {}
        } else {
            System.out.println("Введите новое значение " + type + " или нажмите Enter, чтобы оставить без изменений: ");
            return scanner.nextLine();
        }

        return null;
    }

    public abstract void execute(Scanner scanner);
}
