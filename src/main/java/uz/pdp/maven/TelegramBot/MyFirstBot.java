package uz.pdp.maven.TelegramBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendSticker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyFirstBot {

    public static void main(String[] args) {

        TelegramBot bot = new TelegramBot("7011213809:AAEvbTcxvJ2alL7b5-_1EhbAGzq3lthbMZg");
        int countOfProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(countOfProcessors);

        bot.setUpdatesListener(list -> {
            for (Update update : list) {

                CompletableFuture.runAsync(()->{
                    Message message = update.message();
                    User from = message.from();
                    String text = message.text();
                    /*SendMessage sendMessage = new SendMessage(from.id(), text);
                    bot.execute(sendMessage);*/

                    System.out.println("username: " + from.username() + "    text: " + text + "    Name: " + from.firstName());

                    SendSticker sendSticker = new SendSticker(from.id(), "😢");

                    bot.execute(sendSticker);
                }, executor);
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
