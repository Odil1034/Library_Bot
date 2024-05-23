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

import static uz.pdp.maven.backend.types.bookTypes.Genre.*;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
                        new InlineKeyboardButton("Add Book").callbackData(ADD_BOOK.name()),
                        new InlineKeyboardButton("Search Book").callbackData(SEARCH_BOOK.name())},
                {
                        new InlineKeyboardButton("My Favourite Books").callbackData(MY_FAVOURITE_BOOKS.name())
                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage searchBookMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Search Book");

        InlineKeyboardButton[][] buttons = {
                {getNewInlineButton("By Name", "BY_NAME"),
                        getNewInlineButton("By Author", "BY_AUTHOR")},
                {getNewInlineButton("By Genre", "BY_GENRE"),
                        getNewInlineButton("All Books", "ALL_BOOKS")},
                {getNewInlineButton("Main Menu", "MAIN_MENU")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public static void myFavouriteBookMenu(List<Book> books) {

        Map<Genre, List<Book>> collect = books.stream().collect(Collectors.groupingBy(Book::getGenre));

        StringJoiner joiner = new StringJoiner("\n");

        collect.forEach((genre, books1) -> {
            int i;
            joiner.add("\n" + genre.name() + "\n");
            for (i = 0; i < books1.size(); i++) {
                joiner.add(i + 1 + ". "
                        + books1.get(i).getName() + "   "
                        + books1.get(i).getAuthor());
            }
            i = 0;
        });

        System.out.println(joiner);
    }

    public SendMessage enterBookNameMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "ADD BOOK MENU\n\nEnter Book name: ");
        InlineKeyboardButton[][] buttons = {
                new InlineKeyboardButton[]{
                        getNewInlineButton("Main Menu", "MAIN_MENU")
                }
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage enterBookPage(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter count of page: ");
        InlineKeyboardButton[][] backAndMainKeyboard = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(backAndMainKeyboard);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage enterBookLanguage(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter book language: ");
        InlineKeyboardButton[][] buttons = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage selectGenre(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Select Genre: ");
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[][]{
                {getNewInlineButton("BADIIY ADABIYOTLAR", BADIIY_ADABIYOTLAR.name())},
                {getNewInlineButton("SHE'RIYAT", SHERIYAT.name())},
                {getNewInlineButton("DASTURLASH", DASTURLASH.name())},
                {getNewInlineButton("ILMIY", ILMIY.name())},
                {getNewInlineButton("DINIY", DINIY.name())},
                {getNewInlineButton("SARGUZASHT", SARGUZASHT.name())},
                {getNewInlineButton("BOSHQALAR", BOSHQALAR.name())},
                new InlineKeyboardButton[]{getNewInlineButton("Main Menu", "MAIN_MENU"),
                        getNewInlineButton("Back", "BACK")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterBookDescription(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Book Description: ");
        InlineKeyboardButton[][] buttons = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    public SendMessage enterBookFile(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Please upload the Book File 😊😊😊");
        InlineKeyboardButton[][] backAndMainKeyboard = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(backAndMainKeyboard);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage enterBookAuthor(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Book Author: ");
        InlineKeyboardButton[][] buttons = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage bookIsAddedMessage(MyUser curUser, Book newBook) {
        return new SendMessage(curUser.getId(), getBookInfo(newBook));
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
        SendMessage sendMessage = new SendMessage(curUser.getId(), "Enter Book Photo: ");
        InlineKeyboardButton[][] backAndMainKeyboard = getBackAndMainKeyboard();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(backAndMainKeyboard);
        return sendMessage.replyMarkup(keyboardMarkup);
    }

    public SendMessage handRegistrationMenu(MyUser curUser) {
        SendMessage sendMessage = new SendMessage(curUser.getId(),
                "Congratulations, you are successfully registration ✅✅✅");

        InlineKeyboardButton[][] buttons = {
                new InlineKeyboardButton[]{getNewInlineButton("Main Menu", "MAIN_MENU")}
        };
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);

        sendMessage.replyMarkup(keyboardMarkup);
        return sendMessage;
    }

    private InlineKeyboardButton[][] getBackAndMainKeyboard() {
        return new InlineKeyboardButton[][]{
                new InlineKeyboardButton[]{
                        getNewInlineButton("Main Menu", "MAIN_MENU"),
                        getNewInlineButton("Back", "BACK")}
        };
    }

    private InlineKeyboardButton getNewInlineButton(String buttonName, String callBackData) {
        return new InlineKeyboardButton(buttonName).callbackData(callBackData);
    }
}

