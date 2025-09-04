package ru.rabtra;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.rabtra.commands.CommandDispatcher;
import ru.rabtra.config.AppConfig;
import java.util.*;


public class App {
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
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
    }


}
