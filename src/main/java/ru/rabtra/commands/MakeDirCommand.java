package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

@Component
public class MakeDirCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public MakeDirCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        return args.length==1 ? yandexApiService.makeDir(args[0]) : "Type a path";
    }

    @Override
    public String getName() {
        return "mkdir";
    }

    @Override
    public String getDescription() {
        return "make dir";
    }
}
