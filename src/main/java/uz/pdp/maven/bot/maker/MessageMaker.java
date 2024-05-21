package uz.pdp.maven.bot.maker;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.types.bookTypes.Genre;

import static uz.pdp.maven.bot.states.child.MainMenuState.*;

public class MessageMaker {

    public static @NotNull SendMessage welcomeMessage(User from) {
        String welcomeMessage = "Assalomu Alaykum kutubxona botimizga xush kelibsiz 😊😊😊";
        return new SendMessage(from.id(), welcomeMessage);
    }

    public SendMessage enterPhoneNumber(@NotNull MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(),
                "REGISTRATION \n\nSend phone number to register ");
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
                {
                        new InlineKeyboardButton("Add Book").callbackData("ADD_BOOK"),
                        new InlineKeyboardButton("Search Book").callbackData("SEARCH_BOOK")
                },
                {
                        new InlineKeyboardButton("My Favourite Books").callbackData("MY_FAVOURITE_BOOKS")
                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage searchBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Search Book");
        InlineKeyboardButton[][] buttons = {
                {new InlineKeyboardButton("By Name").callbackData("BY_NAME"),
                        new InlineKeyboardButton("By Author").callbackData("BY_AUTHOR")},
                {new InlineKeyboardButton("By Genre").callbackData("BY_GENRE"),
                        new InlineKeyboardButton("All Books").callbackData("ALL_BOOKS")},
                {new InlineKeyboardButton("Back").callbackData("BACK"),
                        new InlineKeyboardButton("Back to Main Menu").callbackData("BACK_TO_MAIN_MENU")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage myFavouriteBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Your favourite books: ");
        InlineKeyboardButton[][] buttons = {
                {

                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterBookNameMenu(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Enter Book name: ");
    }

    public SendMessage enterSelectGenreMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Select Genre: ");
        KeyboardButton[][] buttons = {
                {new KeyboardButton(Genre.BADIIY_ADABIYOTLAR.name())},
                {new KeyboardButton(Genre.SHERIYAT.name())},
                {new KeyboardButton(Genre.DASTURLASH.name())},
                {new KeyboardButton(Genre.ILMIY.name())},
                {new KeyboardButton(Genre.DINIY.name())},
                {new KeyboardButton(Genre.SARGUZASHT.name())},
                {new KeyboardButton(Genre.BOSHQALAR.name())},
                {
                        new KeyboardButton("BACK"), new KeyboardButton("MAIN_MENU")
                }
        };
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttons);
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
                "\nAuthor: " + book.getAuthor() +
                "\nGenre: " + book.getGenre() +
                "\nDescription: " + book.getDescription()
                + "\n\nBook has been successfully added  ✅✅✅";
    }

    public SendMessage enterBookPhoto(MyUser curUser) {
        return new SendMessage(curUser.getId(), "Enter Book Photo: ");
    }

    public SendMessage handRegistrationMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(),
                "Congratulations, you are successfully registration ✅✅✅");

        InlineKeyboardButton[][] buttons = {
                {
                        new InlineKeyboardButton("MAIN MENU").callbackData(MAIN_MENU.name())
                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);

        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }
}

