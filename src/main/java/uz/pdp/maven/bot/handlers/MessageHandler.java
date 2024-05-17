package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.BaseState;
import uz.pdp.maven.bot.states.mainState.MainState;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();

        MyUser curUser = getUserOrCreate(from);

        String baseStateStr = curUser.getBaseState();
        if(baseStateStr == null){

        }else {
            BaseState baseState = BaseState.valueOf(baseStateStr);
            if (Objects.equals(baseState, MainState.MAIN_STATE)) {
            }
        }
    }
}
