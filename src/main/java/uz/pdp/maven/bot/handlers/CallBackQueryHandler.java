package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.maker.MessageMaker;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.addBookState.AddBookState;
import uz.pdp.maven.bot.states.child.mainMenuState.MainMenuState;

import java.util.Objects;

public class CallBackQueryHandler extends BaseHandler {

    @Override
    public void handle(Update update) {

        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;

        String baseStateStr = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateStr);

        String data = callbackQuery.data();

        if (Objects.equals(baseState, BaseState.MAIN_MENU_STATE)) {
            mainState(data);
        } else if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
            addBookState(data);
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            myFavouriteBooksState(data);
        }

    }

    private void mainState(String data) {
        MainMenuState curState = MainMenuState.valueOf(data); // search or add book
        switch (curState) {
            case ADD_BOOK -> {
                addBookState(data);
                curUser.setState(MainMenuState.ADD_BOOK.name());
            }
            case SEARCH_BOOK -> {
                searchBookState(data);
                curUser.setState(MainMenuState.SEARCH_BOOK.name());
            }
            case MY_FAVOURITE_BOOKS -> {
                myFavouriteBooksState(data);
                curUser.setState(MainMenuState.MY_FAVOURITE_BOOKS.name());
            }
            default -> {
                bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
            }
        }
    }

    private void searchBookState(String data) {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
    }

    private void addBookState(String data) {
        SendMessage sendMessage = messageMaker.addBookMenu(curUser);
        bot.execute(sendMessage);
    }

    private void myFavouriteBooksState(String data) {
        SendMessage sendMessage = messageMaker.myFavouriteBookMenu(curUser);
        bot.execute(sendMessage);
    }

}
