package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

@Component
public class DownloadCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public DownloadCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        return args.length==1 ? yandexApiService.downloadFile(args[0]) : "The argument takes only one path";
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "download file";
    }
}
