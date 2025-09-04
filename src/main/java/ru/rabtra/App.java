package ru.rabtra;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.rabtra.commands.CommandDispatcher;
import ru.rabtra.config.AppConfig;
import ru.rabtra.dto.Item;
import ru.rabtra.dto.TreeResponse;
import ru.rabtra.service.YandexApiService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


public class App {
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        YandexApiService bean = context.getBean(YandexApiService.class);

        CommandDispatcher dispatcher = context.getBean(CommandDispatcher.class);

        Scanner in = new Scanner(System.in);

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("disk:/");
            System.out.print(sb + ">");
            String input = in.nextLine().trim();
            if (input.equals("quit()")) break;

            System.out.println(dispatcher.execute(input));
        }


//        String diskData = bean.uploadFile("disk:/test/test.txt", false, "C:\\Users\\rabtra\\Downloads\\test1.txt");
//        System.out.println(diskData);

//        String downloadLink = bean.downloadFile("disk:/test/test.txt");
//        System.out.println(downloadLink);

//        TreeResponse tree = bean.getTree(5, null, 0);
//        for (Item item : tree.getItems()) {
//            System.out.printf("%-20s %-10s %-30s %-10s%n",
//                    item.getName(),
//                    item.getType(),
//                    item.getPath(),
//                    item.getSize()
//            );
//        }

//        String s = bean.copyTo("disk:/test/test.txt", "disk:/test2/text1.txt", true);
//        System.out.print(s);

//        String s = bean.moveTo("disk:/test/test.jpg", "disk:/bin/test.jpg", false);
//        System.out.println(s);

//        String delete = bean.delete("disk:/test2/text1.txt");
//        System.out.println(delete);
//
//        String s = bean.makeDir("disk:/bin/test");
//        System.out.println(s);
    }


}
