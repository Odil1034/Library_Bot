package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.*;

import java.util.Objects;

import static uz.pdp.maven.bot.states.child.MainMenuState.*;

public class CallBackQueryHandler extends BaseHandler {

    @Override
    public void handle(Update update) {

        CallbackQuery callbackQuery = update.callbackQuery();
        User from = callbackQuery.from();
        super.curUser = getUserOrCreate(from);
        super.update = update;

        String baseStateStr = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateStr);

        if (Objects.equals(baseState, BaseState.REGISTRATION_STATE)) {
            registrationState();
        } else if (Objects.equals(baseState, BaseState.MAIN_MENU_STATE)) {
            mainState();
        } else if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
            addBookState();
        } else if (Objects.equals(baseState, BaseState.SEARCH_BOOK_STATE)) {
            searchBookState();
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            myFavouriteBooksState();
        }
    }

    private void registrationState() {
        if (curUser.getState().equals(RegistrationState.SEND_PHONE_NUMBER.name())) {
            userService.save(curUser);
        } else if (curUser.getState().equals(RegistrationState.REGISTER.name())) {
            String data = update.callbackQuery().data();
            if (data.equals(MAIN_MENU.name())) {
                changeStates(BaseState.MAIN_MENU_STATE, null);
            }
        }
    }

    private void mainState() {

        if (Objects.equals(curUser.getState(), MAIN_MENU.name())) {
            mainMenuState();
        } else if (Objects.equals(curUser.getState(), ADD_BOOK.name())) {
            addBookState();
        } else if (Objects.equals(curUser.getState(), SEARCH_BOOK.name())) {
            addBookState();
        }  else if (Objects.equals(curUser.getState(), MY_FAVOURITE_BOOKS.name())) {
            addBookState();
        }

    }

    private void mainMenuState() {
        String stateStr = curUser.getState();
        MainMenuState state = valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state){

        }
    }

    private void searchBookState() {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
        changeStates(BaseState.SEARCH_BOOK_STATE, null);

        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();

        if (data.equals("BY_NAME")) {
            bot.execute(new SendMessage(curUser.getId(), "Enter name: "));
            changeState(SearchBookState.SEARCH_BY.name());
        } else if (data.equals("BY_AUTHOR")) {
            bot.execute(new SendMessage(curUser.getId(), "Enter name: "));
            changeState(SearchBookState.SEARCH_BY.name());
        } else if (data.equals("BY_GENRE")) {
            bot.execute(new SendMessage(curUser.getId(), "Enter name: "));
            changeState(SearchBookState.SEARCH_BY.name());

        } else if (data.equals("ALL_BOOKS")) {
            bot.execute(new SendMessage(curUser.getId(), "Enter name: "));
            changeState(SearchBookState.SEARCH_BY.name());

        } else if (data.equals("BACK_TO_MAIN_MENU")) {
            changeStates(BaseState.MAIN_MENU_STATE, null);
        }

    }

    private void addBookState() {
        changeStates(BaseState.ADD_BOOK_STATE, null);

        if (curUser.getState() == null) {
            changeState(AddBookState.ENTER_BOOK_NAME.name());
        }

        String stateStr = curUser.getState();
        AddBookState curState = AddBookState.valueOf(stateStr);
        switch (curState) {
            case ENTER_BOOK_NAME -> {
                SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                bot.execute(bookNameMessage);
                changeState(AddBookState.ENTER_BOOK_NAME.name());
            }
            case SELECT_BOOK_GENRE -> System.out.println("Select Book Genre Call Back");
            default -> anyThingIsWrongMessage();
        }
    }

    private void myFavouriteBooksState() {
        changeStates(BaseState.MY_FAVOURITE_BOOKS_STATE, null);
        System.out.println("myFavouriteBooksState is run");
    }

    public void anyThingIsWrongMessage() {
        bot.execute(new SendMessage(curUser.getId(), "Anything is wrong ❌❌❌"));
    }
}
