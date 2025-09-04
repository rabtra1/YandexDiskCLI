package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

@Component
public class EraseTrashCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public EraseTrashCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        return args.length==1 ? yandexApiService.eraseTrash(args[0]) : yandexApiService.eraseTrash(null);
    }

    @Override
    public String getName() {
        return "trash";
    }

    @Override
    public String getDescription() {
        return "empty trash";
    }
}
