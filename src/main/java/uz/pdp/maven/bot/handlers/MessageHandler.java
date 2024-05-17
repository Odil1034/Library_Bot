package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.BaseState;
import uz.pdp.maven.bot.states.mainState.MainState;
import uz.pdp.maven.bot.states.registerState.RegisterState;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    private static MyUser curUser;

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = curUser;
        String text = message.text();

        if (text!=null && text.equals("/start")){
            SendMessage sendMessage = new SendMessage(from.id(),"Assalomu Alayku asfasdfas");
            bot.execute(sendMessage);
        }


    }
}
