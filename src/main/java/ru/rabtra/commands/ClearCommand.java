package ru.rabtra.commands;

import org.springframework.stereotype.Component;

@Component
public class ClearCommand implements Command{
    @Override
    public String execute(String[] args) throws Exception {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        return "";
    }

    @Override
    public String getName() {
        return "cls";
    }

    @Override
    public String getDescription() {
        return "clear terminal";
    }
}
