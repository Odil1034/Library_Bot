package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.maven.backend.models.myUser.MyUser;

public class CallBackQueryHandler extends BaseHandler {
    @Override

    public void handle(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        MyUser curUser = getUserOrCreate(from);

    }
}
