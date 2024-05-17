package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.processor.message.MainMessage;

public class CallBackQueryHandler extends BaseHandler {

    private MainMessage mainMessage = new MainMessage();

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        MyUser curUser = getUserOrCreate(from);

    }
}
