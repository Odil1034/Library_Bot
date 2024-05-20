package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;

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

        if (Objects.equals(baseState, BaseState.MAIN_MENU_STATE)) {
            mainState();
        } else if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
            addBookState();
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            myFavouriteBooksState();
        }
    }

    private void mainState() {

        changeStates(BaseState.MAIN_MENU_STATE,null);
        if (curUser.getState() == null) {
            curUser.setState(update.callbackQuery().data());
        }

        String stateStr = curUser.getState(); // add Book
        MainMenuState state = MainMenuState.valueOf(stateStr);
        Message message = update.callbackQuery().message();
        SendMessage sendMessage;
        switch (state) {
            case MAIN_MENU -> {
                sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                changeStates(BaseState.MAIN_MENU_STATE, null);
                deleteMessage(message.messageId());
            }
            case ADD_BOOK -> addBookState();

            case SEARCH_BOOK -> {
                sendMessage = messageMaker.searchBookMenu(curUser);
                bot.execute(sendMessage);
                changeStates(BaseState.SEARCH_BOOK_STATE, null);
                searchBookState();
            }
            case MY_FAVOURITE_BOOKS -> {
                sendMessage = messageMaker.myFavouriteBookMenu(curUser);
                bot.execute(sendMessage);
                changeStates(BaseState.MY_FAVOURITE_BOOKS_STATE, null);
                myFavouriteBooksState();
            }
            default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
        }
    }

    private void addBookState() {
        SendMessage sendMessage;
        sendMessage = messageMaker.enterBookNameMenu(curUser);
        bot.execute(sendMessage);
        changeStates(BaseState.ADD_BOOK_STATE, AddBookState.ENTER_BOOK_NAME.name());
    }

    private void searchBookState() {

    }

    private void myFavouriteBooksState() {
        SendMessage sendMessage = messageMaker.myFavouriteBookMenu(curUser);
        bot.execute(sendMessage);
    }

    public void anyThingIsWrongMessage() {
        bot.execute(new SendMessage(curUser.getId(), "Anything is wrong ❌❌❌"));
    }
}
