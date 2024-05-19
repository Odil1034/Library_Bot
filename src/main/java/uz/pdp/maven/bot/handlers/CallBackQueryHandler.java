package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
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
        String state = curUser.getState();
        MainMenuState curState = MainMenuState.valueOf(state);
        switch (curState) {
            case MAIN_MENU:
                curUser.setState(MainMenuState.MAIN_MENU.name());
                SendMessage sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                break;
            case ADD_BOOK:
                curUser.setState(MainMenuState.ADD_BOOK.name());
                SendMessage sendMessage1 = messageMaker.addBookMenu(curUser);
                bot.execute(sendMessage1);
                break;
            case SEARCH_BOOK:
                curUser.setState(MainMenuState.SEARCH_BOOK.name());
                searchBookState(data);
                break;
            case MY_FAVOURITE_BOOKS:
                curUser.setState(MainMenuState.MY_FAVOURITE_BOOKS.name());
                myFavouriteBooksState(data);
                break;
            default:
                bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
                break;
        }
        userService.save(curUser);
    }

    private void searchBookState(String data) {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
    }

    private void addBookState(String data) {
        String stateStr = curUser.getState();
        AddBookState curState = AddBookState.valueOf(stateStr);
        switch (curState){
            case ENTER_BOOK_AUTHOR -> {

            }
            case ENTER_BOOK_GENRE -> {

            }
            case ENTER_BOOK_NAME -> {

            }
            case ENTER_BOOK_FILE_ID -> {

            }
            case ENTER_BOOK_PHOTO_ID -> {

            }
            case ENTER_BOOK_DESCRIPTION -> {

            }
            default -> {
                incorrectValue("Option");
            }
        }

        curUser.setState(AddBookState.ENTER_BOOK_NAME.name());
        SendMessage sendMessage = messageMaker.enterBookNameMenu(curUser);
        bot.execute(sendMessage);
    }

    public void incorrectValue(String data) {
        bot.execute(new SendMessage(curUser.getId(), "You entered incorrect " + data));
    }

    private void myFavouriteBooksState(String data) {
        SendMessage sendMessage = messageMaker.myFavouriteBookMenu(curUser);
        bot.execute(sendMessage);
    }
}
