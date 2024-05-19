package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.addBookState.AddBookState;
import uz.pdp.maven.bot.states.child.mainMenuState.MainMenuState;
import uz.pdp.maven.bot.states.child.myFavouriteBooksState.MyFavouriteBooksState;
import uz.pdp.maven.bot.states.child.searchBookState.SearchBookState;

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
        } else {

        }

    }

    private void mainState(String data) {
        String state = curUser.getState();

        if(state!=null){
            if (state.equals(MainMenuState.ADD_BOOK.name())) {
                addBookState(data);
            } else if (state.equals(MainMenuState.SEARCH_BOOK.name())) {
                searchBookState(data);
            } else if (state.equals(MainMenuState.MY_FAVOURITE_BOOKS.name())) {
                myFavouriteBooksState(data);
            } else if (state.equals(MainMenuState.MAIN_MENU.name())) {
                mainState(data);
            } else {
                System.out.println("State is incorrect value");
            }
        }else {
            MainMenuState curState = MainMenuState.valueOf(data); // search or add book
            SendMessage sendMessage;
            switch (curState) {

                case ADD_BOOK -> {
                    sendMessage = messageMaker.addBookMenu(curUser);
                    bot.execute(sendMessage);

                    curUser.setState(data);
                    userService.save(curUser);
                }
                case SEARCH_BOOK -> {
                    sendMessage = messageMaker.searchBookMenu(curUser);
                    bot.execute(sendMessage);

                    curUser.setState(data);
                    userService.save(curUser);
                }
                case MY_FAVOURITE_BOOKS -> {
                    sendMessage = messageMaker.myFavouriteBookMenu(curUser);
                    bot.execute(sendMessage);

                    curUser.setState(data);
                    userService.save(curUser);
                }
                default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
            }
        }
    }

    private void searchBookState(String data) {
        // SEARCH_BY BOOK_LIST, SELECT_FILE DOWNLOAD, ADD_MY_FAVOURITE_BOOKS; - > data

        curUser.setBaseState(BaseState.SEARCH_BOOK_STATE.name());
        String stateStr = curUser.getState();
        SearchBookState curState = SearchBookState.valueOf(stateStr);

        switch (curState) {
            case SEARCH_BY -> {

            }
            case BOOK_LIST -> {

            }
            case SELECT_FILE -> {

            }
            case DOWNLOAD -> {

            }
            default -> {

            }
        }

    }

    private void addBookState(String data) {

        curUser.setBaseState(BaseState.ADD_BOOK_STATE.name());
        AddBookState curState = AddBookState.valueOf(data);

        switch (curState) {
            case ENTER_BOOK_NAME -> {
                curUser.setBaseState(BaseState.ADD_BOOK_STATE.name());
                curUser.setState(AddBookState.ENTER_BOOK_AUTHOR.name());
                userService.save(curUser);
            }
            case ENTER_BOOK_AUTHOR -> {

            }
            case ENTER_BOOK_GENRE -> {

            }
            case ENTER_BOOK_DESCRIPTION -> {

            }
            case ENTER_BOOK_PHOTO_ID -> {

            }
            case ENTER_BOOK_FILE_ID -> {

            }
            default -> {

            }
        }

    }

    private void myFavouriteBooksState(String data) {

        curUser.setBaseState(BaseState.MY_FAVOURITE_BOOKS_STATE.name());
        String stateStr = curUser.getState();
        MyFavouriteBooksState curState = MyFavouriteBooksState.valueOf(stateStr);

        switch (curState) {
            case BOOK_LIST -> {

            }
            case SELECT_FILE -> {

            }
            case DOWNLOAD -> {

            }
            default -> {

            }
        }
    }
}
