package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

import java.util.Objects;

@Component
public class MoveCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public MoveCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }


    @Override
    public String execute(String[] args) throws Exception {
        String pathFrom = args[0];
        String pathTo = args[1];
        boolean overwrite = Objects.equals(args[args.length - 1], "-o");
        return yandexApiService.moveTo(pathFrom, pathTo, overwrite);
    }

    @Override
    public String getName() {
        return "mv";
    }

    @Override
    public String getDescription() {
        return "move";
    }
}
