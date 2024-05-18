package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.pdp.maven.backend.models.myUser.MyUser;

public class MessageMaker {

    public static @NotNull SendMessage welcomeMessage(User from) {
        String welcomeMessage = "Assalomu Alaykum kutubxona botimizga xush kelibsiz 😊😊😊";
        return new SendMessage(from.id(), welcomeMessage);
    }

    public SendMessage enterPhoneNumber(@NotNull MyUser curUser) {

        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Phone Number");

        KeyboardButton[][] buttons = {
                {
                        new KeyboardButton("Send My Phone Number").requestContact(true)
                }
        };

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons).oneTimeKeyboard(true).resizeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }

    public SendMessage mainMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Choose Menu");

        InlineKeyboardButton[][] buttons = {
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("Add Book").callbackData("ADD_BOOK"),
                        new InlineKeyboardButton("Search Book").callbackData("SEARCH_BOOK")},
                new InlineKeyboardButton[]{
                        new InlineKeyboardButton("My Favourite Books").callbackData("MY_FAVOURITE_BOOKS")}
        };

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage searchBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Search Book");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("By Author").callbackData("BY_AUTHOR"),
                        new InlineKeyboardButton("By Name").callbackData("BY_NAME")
                },
                {
                        new InlineKeyboardButton("By Genre").callbackData("BY_GENRE"),
                        new InlineKeyboardButton("Skip").callbackData("SKIP")
                }
        };

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);

        return sendMessage;
    }

    public SendMessage addBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Add Book Menu");
        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("Enter Book Name: ").callbackData("ENTER_BOOK_NAME")
                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }
}