package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

@Component
public class DiskInfoCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public DiskInfoCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        return yandexApiService.getDiskData();
    }

    @Override
    public String getName() {
        return "diskInfo";
    }

    @Override
    public String getDescription() {
        return "Show information";
    }
}
