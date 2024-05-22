package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;
import uz.pdp.maven.bot.states.child.RegistrationState;
import uz.pdp.maven.bot.states.child.SearchBookState;

import java.util.Objects;

import static uz.pdp.maven.bot.states.child.MainMenuState.MAIN_MENU;

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

        Message message = update.callbackQuery().message();
        if (curUser.getState().equals(RegistrationState.SEND_PHONE_NUMBER.name())) {
            userService.save(curUser);
        } else if (curUser.getState().equals(RegistrationState.REGISTER.name())) {
            String data = update.callbackQuery().data();
            if (data.equals("MAIN_MENU")) {
                changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                bot.execute(messageMaker.mainMenu(curUser));
                deleteMessage(message.messageId());
            }
        }
    }

    private void mainState() {

        String stateStr = curUser.getState();
        MainMenuState state = MainMenuState.valueOf(stateStr);
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state) {
            case MAIN_MENU -> {
                String data = callbackQuery.data();
                mainMenu(data);
            }
            case SEARCH_BOOK -> {
                searchBookState();
            }
            case MY_FAVOURITE_BOOKS -> {
                myFavouriteBooksState();
            }
            case ADD_BOOK -> {
                addBookState();
            }
        }
    }

    private void mainMenu(String data) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (Objects.equals(data, "BACK")) {
            data = MAIN_MENU.name();
        }
        MainMenuState state = MainMenuState.valueOf(data);
        Message message = callbackQuery.message();
        SendMessage sendMessage;
        switch (state) {
            case MAIN_MENU -> {
                sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                return;
            }
            case ADD_BOOK -> addBookState();
            case SEARCH_BOOK -> searchBookState();
            case MY_FAVOURITE_BOOKS -> myFavouriteBooksState();
            default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
        }
        deleteMessage(message.messageId());
        userService.save(curUser);
    }

    private void addBookState() {
        Message message1 = update.callbackQuery().message();
        deleteMessage(message1.messageId());

        changeStates(BaseState.ADD_BOOK_STATE, null);

        if (curUser.getState() == null) {
            changeState(AddBookState.ENTER_BOOK_NAME.name());
        }

        String stateStr = curUser.getState();
        AddBookState curState = AddBookState.valueOf(stateStr);
        Message message = update.callbackQuery().message();
        switch (curState) {
            case ENTER_BOOK_NAME -> {

                String data = update.callbackQuery().data();
                if (data.equals("MAIN_MENU")) {
                    changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                    mainState();
                    deleteMessage(message.messageId());
                    return;
                }
                SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                bot.execute(bookNameMessage);
                changeState(AddBookState.ENTER_BOOK_NAME.name());
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_AUTHOR -> {
                String data = update.callbackQuery().data();
                if (data.equals("MAIN_MENU")) {
                    changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                    bot.execute(messageMaker.mainMenu(curUser));
                } else if (data.equals("ENTER_BOOK_NAME")) {
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                }
                deleteMessage(message.messageId());
            }
            case SELECT_BOOK_GENRE -> {
                String data = update.callbackQuery().data();
                if (data.equals("MAIN_MENU")) {
                    changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                    bot.execute(messageMaker.mainMenu(curUser));
                    mainState();
                } else if (data.equals("ENTER_BOOK_NAME")) {
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                } else if (data.equals("ENTER_BOOK_AUTHOR")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_PHOTO_ID -> {
                String data = update.callbackQuery().data();
                if (data.equals("MAIN_MENU")) {
                    changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                    bot.execute(messageMaker.mainMenu(curUser));
                    mainState();
                } else if (data.equals("ENTER_BOOK_NAME")) {
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                } else if (data.equals("ENTER_BOOK_AUTHOR")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                } else if (data.equals("SELECT_BOOK_GENRE")) {
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_DESCRIPTION -> {
                String data = update.callbackQuery().data();
                if (data.equals("MAIN_MENU")) {
                    changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
                    bot.execute(messageMaker.mainMenu(curUser));
                    mainState();
                } else if (data.equals("ENTER_BOOK_NAME")) {
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                } else if (data.equals("ENTER_BOOK_AUTHOR")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                } else if (data.equals("SELECT_BOOK_GENRE")) {
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                } else if (data.equals("")) {
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                }
                deleteMessage(message.messageId());
            }
            default -> anyThingIsWrongMessage();
        }
    }

    private void searchBookState() {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);

        CallbackQuery callbackQuery = update.callbackQuery();
        String stateStr = curUser.getState();
        SearchBookState state = SearchBookState.valueOf(stateStr);

        switch (state){
            case SEARCH_BY -> {
                String data = callbackQuery.data();
                searchBookMenu(data);
            }
            case BOOK_LIST -> {

            }
            case SELECT_FILE -> {

            }
            case DOWNLOAD -> {

            }
            case ADD_MY_FAVOURITE_BOOKS -> {

            }
        }
    }

    private void searchBookMenu(String data) {

        if (data.equals("BY_NAME")) {
            bot.execute(new SendMessage(curUser.getId(), "SEARCHING BOOK INFO"));
            bot.execute(new SendMessage(curUser.getId(), "Enter book name: "));
        } else if (data.equals("BY_AUTHOR")) {
            bot.execute(new SendMessage(curUser.getId(), "Enter book author: "));
        } else if (data.equals("BY_GENRE")) {
            bot.execute(messageMaker.selectGenreMenu(curUser));
        } else if (data.equals("ALL_BOOKS")) {
            bot.execute(new SendMessage(curUser.getId(), "All books"));
        } else if (data.equals("MAIN_MENU")) {
            changeStates(BaseState.MAIN_MENU_STATE, null);
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
