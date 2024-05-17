package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.mainMenuState.MainMenuState;

import java.util.Objects;

import static uz.pdp.maven.bot.states.child.addBookState.AddBookState.*;

public class CallBackQueryHandler extends BaseHandler {

    @Override
    public void handle(Update update) {

        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;

        String baseStateStr = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateStr);

        String data;
        if (Objects.equals(baseState, BaseState.MAIN_MENU_STATE)) {
            data = callbackQuery.data();
            mainMenuState();   // go to Main Menu
        } else if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
            data = callbackQuery.data();
            addBookState(data);     //  go to Add Book
        } else if (Objects.equals(baseState, BaseState.SEARCH_BOOK_STATE)) {
            data = callbackQuery.data();
            searchBookState();  //  go to search Book
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            data = callbackQuery.data();
            myFavouriteBooksState();    //  go to My Favourite Book
        }

    }

    private void myFavouriteBooksState() {

    }

    private void searchBookState() {

    }

    private void addBookState(String data) {
        String stateStr = curUser.getState();
        MainMenuState curState = MainMenuState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();

        Message message = callbackQuery.message();

        String data2;
        switch (data) {
            case "BOOK_NAME" -> {
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            case "SELECT_GENRE" -> {
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            case "ENTER_AUTHOR" -> {
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            case "ENTER_PHOTO_OF_BOOK" -> {
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            case "ENTER_DESCRIPTION" -> {
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            case "UPLOAD_FILE" ->{
                data2 = callbackQuery.data();
                deleteMessage(message.messageId());
            }
            default -> throw new IllegalStateException("Unexpected value: " + curState);
        }
    }

    private void mainMenuState() {
        String stateStr = curUser.getState();
        MainMenuState curState = MainMenuState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();

        String data;
        switch (curState) {
            case SEARCH_BOOK -> {
                data = callbackQuery.data();
                searchBook(data);
            }
            case ADD_BOOK -> {
                data = callbackQuery.data();
                addBook(data);
            }
            default -> bot.execute(new SendMessage(curUser.getId(), "Nimadir xatoda"));
        }
    }

    private void addBook(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();

        String bookName;
        if (Objects.isNull(message.text()) || message.text().isBlank() || message.text().isEmpty()) {

        }

    }

    private void searchBook(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();

    }
}
