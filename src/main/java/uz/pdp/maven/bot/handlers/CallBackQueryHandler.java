package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;
import uz.pdp.maven.bot.states.child.RegistrationState;

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
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            myFavouriteBooksState();
        }
    }

    private void registrationState() {
        if (curUser.getState().equals(RegistrationState.SEND_PHONE_NUMBER.name())) {
            userService.save(curUser);
        } else if (curUser.getState().equals(RegistrationState.REGISTER.name())) {
            String data = update.callbackQuery().data();
            if (data.equals("GO_MAIN_MENU")) {
                changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
            }
        }
    }

    private void mainState() {
        if (curUser.getState() == null) {
            String curState = update.callbackQuery().data();
            if(curState!=null) changeState(curState);
        } else {
            String stateStr = curUser.getState();
            MainMenuState state = MainMenuState.valueOf(stateStr);
            Message message = update.callbackQuery().message();
            SendMessage sendMessage;
            switch (state) {
                case MAIN_MENU -> {
                    changeStates(BaseState.MAIN_MENU_STATE, null);
                    sendMessage = messageMaker.mainMenu(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
                case ADD_BOOK -> addBookState();

                case SEARCH_BOOK -> {
                    sendMessage = messageMaker.searchBookMenu(curUser);
                    bot.execute(sendMessage);
                    searchBookState();
                }
                case MY_FAVOURITE_BOOKS -> {
                    sendMessage = messageMaker.myFavouriteBookMenu(curUser);
                    bot.execute(sendMessage);
                    myFavouriteBooksState();
                }
                default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
            }
            userService.save(curUser);
        }
    }

    private void searchBookState() {
        changeStates(BaseState.SEARCH_BOOK_STATE, null);
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
