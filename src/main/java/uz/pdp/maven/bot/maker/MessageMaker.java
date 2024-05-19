package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.pdp.maven.backend.models.book.Book;
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
                {new InlineKeyboardButton("Add Book").callbackData("ADD_BOOK"),
                 new InlineKeyboardButton("Search Book").callbackData("SEARCH_BOOK")},
                {new InlineKeyboardButton("My Favourite Books").callbackData("MY_FAVOURITE_BOOKS")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage searchBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Search Book");
        InlineKeyboardButton[][] buttons = {
                {new InlineKeyboardButton("By Author").callbackData("BY_AUTHOR"),
                        new InlineKeyboardButton("By Name").callbackData("BY_NAME")},
                {new InlineKeyboardButton("By Genre").callbackData("BY_GENRE"),
                        new InlineKeyboardButton("All Books").callbackData("ALL_BOOKS")},
                {new InlineKeyboardButton("Back").callbackData("BACK"),
                        new InlineKeyboardButton("Back to Main Menu").callbackData("BACK_TO_MAIN_MENU")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage addBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Addition Book Info");
        InlineKeyboardButton[][] buttons = {
                {new InlineKeyboardButton("Enter Book Name").callbackData("ENTER_BOOK_NAME")},
                {new InlineKeyboardButton("Enter Book Author").callbackData("ENTER_BOOK_AUTHOR")},
                {new InlineKeyboardButton("Enter Description").callbackData("ENTER_DESCRIPTION")},
                {new InlineKeyboardButton("Select Genre").callbackData("SELECT_GENRE")},
                {new InlineKeyboardButton("Back").callbackData("BACK")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage myFavouriteBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Your favourite books: ");
        InlineKeyboardButton[][] buttons = {
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterBookNameMenu(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Enter Book name: ");
    }

    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return new DeleteMessage(chatId, messageId);
    }

    public SendMessage enterSelectGenreMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Select Genre: ");
        KeyboardButton[][] buttons = {
                {new KeyboardButton("BADIIY ADABIYOT")},
                {new KeyboardButton("SHE'RIYAT")},
                {new KeyboardButton("DASTURLASH")},
                {new KeyboardButton("ILMIY")},
                {new KeyboardButton("DINIY")},
                {new KeyboardButton("SARGUZASHT")},
                {new KeyboardButton("BOSHQALAR")},
                {new KeyboardButton("Back"), new KeyboardButton("Main Menu")}
        };
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttons)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterBookDescription(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Enter Book Description: ");
    }

    public SendMessage enterBookFile(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Please upload the Book File 😊😊😊");
    }

    public SendMessage enterBookAuthor(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Enter Book Author: ");
    }

    public SendMessage bookIsAddedMessage(MyUser curUser, Book newBuilderBook) {
        return new SendMessage(curUser.getId(), getBookInfo(newBuilderBook));
    }

    private String getBookInfo(Book book) {
        return " Added Book Info \n" +
                "\nName : " + book.getName() +
                "\nAuthor: " + book.getGenre() +
                "\nGenre: " + book.getGenre() +
                "\nDescription: " + book.getDescription()
                + "\n\nBook has been successfully added  ✅✅✅";
    }
}

