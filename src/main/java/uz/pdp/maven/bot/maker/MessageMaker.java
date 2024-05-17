package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.BaseState;

import java.util.Objects;

public class MessageMaker {

    public SendMessage enterPhoneNumber(MyUser curUser){

        SendMessage sendMessage = new SendMessage(curUser.getId(), "Telefon raqamini jo'natish: ");

        KeyboardButton[][] buttons = {
                {
                    new KeyboardButton("Mening telefon raqamimni jo'natish ").requestContact(true)
                }
        };

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage mainMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Choose Menu");
        KeyboardButton[][] buttons = {
                {new KeyboardButton("Add Book"), new KeyboardButton("Search Book")}
        };

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }
}
