package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
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

        }else if(Objects.equals(baseState, BaseState.ADD_BOOK_STATE)){
            addBookState(data);

        }else if(Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)){
            myFavouriteBooksState(data);
        }
    }

    private void mainState(String data){
        MainMenuState curState = MainMenuState.valueOf(data);
        CallbackQuery callbackQuery = update.callbackQuery();

        switch (curState){
            case ADD_BOOK -> {
                String dataAddBook = callbackQuery.data();
                mainMenu(dataAddBook);
            }
            case SEARCH_BOOK -> {
                String dataSearchBook = callbackQuery.data();
                searchBookMenu(dataSearchBook);
            }
            case MY_FAVOURITE_BOOKS -> {
                String dataMyFavouriteBook = callbackQuery.data();
                myFavouriteBooks(dataMyFavouriteBook);
            }
            default -> {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Anything is wrong");
                bot.execute(sendMessage);
            }
        }
    }

    private void mainMenu(String data) {


    }

    private void myFavouriteBooks(String data) {
    }

    private void searchBookMenu(String data) {

    }

    private void addBookState(String data) {
    }

    private void myFavouriteBooksState(String data) {

    }

}
