package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;

public class MessageMaker {

    public SendMessage enterPhoneNumber(MyUser curUser){

        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Phone Number: ");

        KeyboardButton[][] buttons = { {new KeyboardButton("Send My Phone Number")} };

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        sendMessage.replyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }
}
