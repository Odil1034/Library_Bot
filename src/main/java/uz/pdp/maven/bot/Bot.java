package uz.pdp.maven.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import uz.pdp.maven.bot.manager.UpdateManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uz.pdp.maven.backend.paths.PathConstants.BOT_TOKEN;

public class Bot {

    static final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    static ThreadLocal<UpdateManager> updateHandlerThreadLocal = ThreadLocal.withInitial(UpdateManager::new);


    public static void main(String[] args) {

        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        bot.setUpdatesListener((updates) -> {
            for (Update update : updates) {
                CompletableFuture.runAsync(() -> {
                            try {
                                updateHandlerThreadLocal.get().handle(update);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        pool);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }
}
