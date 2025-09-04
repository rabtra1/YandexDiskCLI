package ru.rabtra.commands;

import lombok.Getter;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class CommandDispatcher {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandDispatcher(List<Command> commandList) {
        for (Command c : commandList) {
            commands.put(c.getName(), c);
        }
    }

    public String execute(String input) {
        String[] split = input.split("\\s+");
        String commandName = split[0];
        String[] args = Arrays.copyOfRange(split, 1, split.length);

        Command command = commands.get(commandName);
        if (command == null) {
            return "No command";
        }

        try {
            return command.execute(args);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
