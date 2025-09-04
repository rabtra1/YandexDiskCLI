package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

import java.util.Objects;

@Component
public class CopyCommand implements Command{
    private final YandexApiService yandexApiService;

    @Autowired
    public CopyCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        String pathFrom = args[0];
        String pathTo = args[1];
        boolean overwrite = Objects.equals(args[args.length - 1], "-o");
        return yandexApiService.copyTo(pathFrom, pathTo, overwrite);
    }

    @Override
    public String getName() {
        return "cp";
    }

    @Override
    public String getDescription() {
        return "copy files";
    }
}
