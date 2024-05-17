package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.handlers.CallBackQueryHandler;
import uz.pdp.maven.bot.states.child.addBookState.AddBookState;

public class MessageMaker {

    public SendMessage enterPhoneNumber(MyUser curUser) {

        SendMessage sendMessage = new SendMessage(curUser.getId(), "Telefon raqamini jo'natish");

        KeyboardButton[][] buttons = {
                {
                        new KeyboardButton("Mening telefon raqamimni jo'natish").requestContact(true)
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

    public SendMessage searchBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Search Book");
        KeyboardButton[][] buttons = {
                {new KeyboardButton("By Author"), new KeyboardButton("By Name")},
                {new KeyboardButton("By Genre"), new KeyboardButton("Skip")}
        };

        ReplyKeyboardMarkup replyKeyboardMarkup =
                new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);

        sendMessage.replyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage addBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Add Book Menu");
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[0][0];
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);

        InlineKeyboardButton bookNameButton =
                buttons[0][0].callbackData(AddBookState.BOOK_NAME.name());

        sendMessage.replyMarkup(keyboardMarkup);

        return sendMessage;
    }
}