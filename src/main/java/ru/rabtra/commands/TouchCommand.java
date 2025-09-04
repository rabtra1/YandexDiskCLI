package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.service.YandexApiService;

import java.util.Objects;

@Component
public class TouchCommand  implements Command{
    private final YandexApiService yandexApiService;

    @Autowired
    public TouchCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        String pathToUpload = args[0];
        String pathToFile = args[1];
        boolean overwrite = Objects.equals(args[args.length - 1], "-o");
        return yandexApiService.uploadFile(pathToUpload, pathToFile, overwrite);
    }

    @Override
    public String getName() {
        return "touch";
    }

    @Override
    public String getDescription() {
        return "upload file to the cloud";
    }

}
