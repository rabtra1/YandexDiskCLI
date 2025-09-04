package ru.rabtra.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rabtra.dto.Item;
import ru.rabtra.dto.TreeResponse;
import ru.rabtra.service.YandexApiService;

import java.util.HashMap;
import java.util.Map;

@Component
public class ListCommand implements Command{

    private final YandexApiService yandexApiService;

    @Autowired
    public ListCommand(YandexApiService yandexApiService) {
        this.yandexApiService = yandexApiService;
    }

    @Override
    public String execute(String[] args) throws Exception {
        Map<String, String> arg = parseArgs(args);
        Integer limit = arg.containsKey("l") ? Integer.parseInt(arg.get("l")) : 20;
        String mt = arg.get("mt");
        Integer offset = arg.containsKey("o") ? Integer.parseInt(arg.get("o")) : 0;
        TreeResponse tree = yandexApiService.getTree(limit, mt, offset);
        return formatTreeResponse(tree);
    }

    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public String getDescription() {
        return "See tree";
    }

    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                String key = args[i].replaceFirst("-+", "");
                String value = (i + 1 < args.length && !args[i + 1].startsWith("-")) ?
                        args[++i] : "true";
                result.put(key, value);
            }
        }
        return result;
    }

    private String formatTreeResponse(TreeResponse tree) {
        StringBuilder sb = new StringBuilder();
        for (Item item : tree.getItems()) {
            sb.append(
                    String.format("%-20s %-10s %-30s %-10s%n",
                            item.getName(),
                            item.getType(),
                            item.getPath(),
                            item.getSize())
            );
        }
        return sb.toString();
    }

}
