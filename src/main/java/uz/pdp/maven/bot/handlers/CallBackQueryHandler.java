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
        CallbackQuery callbackQuery = update.callbackQuery();
        SendMessage sendMessage = null;
        switch (curState) {
            case ADD_BOOK -> {
                String addBookStr = callbackQuery.data();
                AddBookState addBookState = AddBookState.valueOf(addBookStr);

                String state = null;
                if(addBookState.equals(AddBookState.BOOK_NAME)){
                    sendMessage = messageMaker.enterBookNameMenu(curUser);
                    state = AddBookState.BOOK_NAME.name();
                }else if(addBookState.equals(AddBookState.ENTER_PHOTO_OF_BOOK)){
                    sendMessage = messageMaker.enterPhotoOfBookMenu(curUser);
                    state = AddBookState.ENTER_PHOTO_OF_BOOK.name();
                }else if(addBookState.equals(AddBookState.ENTER_AUTHOR)){
                    sendMessage = messageMaker.enterAuthorMenu(curUser);
                    state = AddBookState.ENTER_AUTHOR.name();
                }else if(addBookState.equals(AddBookState.ENTER_DESCRIPTION)){
                    sendMessage = messageMaker.enterDescriptionMenu(curUser);
                    state = AddBookState.ENTER_DESCRIPTION.name();
                }else if(addBookState.equals(AddBookState.SELECT_GENRE)){
                    sendMessage = messageMaker.enterSelectGenreMenu(curUser);
                    state = AddBookState.SELECT_GENRE.name();
                }else if(addBookState.equals(AddBookState.UPLOAD_FILE)){
                    sendMessage = messageMaker.enterUploadFileMenu(curUser);
                    state = AddBookState.UPLOAD_FILE.name();
                }
                curUser.setBaseState(MainMenuState.ADD_BOOK.name());
                curUser.setState(state);
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
                sendMessage = new SendMessage(curUser.getId(), "Anything is wrong");
            }
        }

//        addBookState(addBookStr);
        bot.execute(sendMessage);
    }

    private void myFavouriteBooks(String data) {
    }

    private void searchBookMenu(String data) {

    }

    private void addBookState(String data) {

        SendMessage sendMessage = messageMaker.addBookMenu(curUser);
        CallbackQuery callbackQuery = update.callbackQuery();

        String dataStr = callbackQuery.data();
        curUser.setBaseState(BaseState.ADD_BOOK_STATE.name());
        curUser.setState(dataStr);
        userService.save(curUser);
        bot.execute(sendMessage);

    }

    private void myFavouriteBooksState(String data) {
        messageMaker.myFavouriteBookMenu(curUser);

    }

}
