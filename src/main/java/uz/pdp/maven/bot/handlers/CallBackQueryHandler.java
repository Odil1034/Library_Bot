package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;

import java.util.Objects;

import static uz.pdp.maven.bot.states.child.MainMenuState.SEARCH_BOOK;

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

        if (curUser.getState() == null) {
            curUser.setState(update.callbackQuery().data());
            userService.save(curUser);
        }

        String stateStr = curUser.getState();
        MainMenuState state = MainMenuState.valueOf(stateStr);
        Message message = update.callbackQuery().message();
        SendMessage sendMessage;
        switch (state) {
            case MAIN_MENU -> {
                curUser.setState(MainMenuState.MAIN_MENU.name());
                sendMessage = messageMaker.mainMenu(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
            case ADD_BOOK -> {
                curUser.setBaseState(BaseState.ADD_BOOK_STATE.name());
                sendMessage = messageMaker.addBookMenu(curUser);
                bot.execute(sendMessage);
                addBookState();
            }
            case SEARCH_BOOK -> {
                curUser.setBaseState(BaseState.SEARCH_BOOK_STATE.name());
                sendMessage = messageMaker.searchBookMenu(curUser);
                bot.execute(sendMessage);
                searchBookState();
            }
            case MY_FAVOURITE_BOOKS -> {
                curUser.setBaseState(BaseState.MY_FAVOURITE_BOOKS_STATE.name());
                sendMessage = messageMaker.myFavouriteBookMenu(curUser);
                bot.execute(sendMessage);
                myFavouriteBooksState();
            }
            default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
        }
        userService.save(curUser);
    }

    private void searchBookState() {

    }

    private void addBookState() {

        if(curUser.getState() == null){
            CallbackQuery callbackQuery = update.callbackQuery();
            String data = callbackQuery.data();
            curUser.setState(data);
            userService.save(curUser);
        }

        String stateStr = curUser.getState();
        AddBookState curState = AddBookState.valueOf(stateStr);
        Message message = update.callbackQuery().message();
        SendMessage sendMessage;
        switch (curState) {
            case ENTER_BOOK_NAME -> {
                curUser.setState(AddBookState.ENTER_BOOK_NAME.name());
                sendMessage = messageMaker.enterBookNameMenu(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_AUTHOR -> {
                System.out.println("Enter Book Author");
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_GENRE -> {
                System.out.println("Enter Book Genre");
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_DESCRIPTION -> {
                System.out.println("Enter Book Description");
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_PHOTO_ID -> {
                System.out.println("Enter Book Photo");
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_FILE_ID -> {
                System.out.println("Enter Book File");
                deleteMessage(message.messageId());
            }
            default -> anyThingIsWrongMessage();
        }
    }

    private void myFavouriteBooksState() {
        SendMessage sendMessage = messageMaker.myFavouriteBookMenu(curUser);
        bot.execute(sendMessage);
    }

    public void anyThingIsWrongMessage() {
        bot.execute(new SendMessage(curUser.getId(), "Anything is wrong ❌❌❌"));
    }
}
