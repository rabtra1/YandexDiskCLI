package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

@Component
public class DeleteCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public DeleteCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {

        return args.length==1 ? yandexApiService.delete(args[0]) : "Type a path";
    }

    @Override
    public String getName() {
        return "rm";
    }

    @Override
    public String getDescription() {
        return "remove";
    }
}
