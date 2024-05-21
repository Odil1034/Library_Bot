package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;
import uz.pdp.maven.bot.states.child.RegistrationState;
import uz.pdp.maven.bot.states.child.SearchBookState;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

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
            mainMenuState();
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
            if (data.equals("GO_MAIN_MENU")) {
                changeStates(BaseState.MAIN_MENU_STATE, null);
                mainMenuState();
            }
        }
    }

    private void mainMenuState() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);

        MainMenuState state;
        if(curUser.getState() == null){
            String stateStr2 = update.callbackQuery().data();
            changeState(stateStr2);
            state = MainMenuState.valueOf(stateStr2);
        }
        state = MainMenuState.valueOf(curUser.getState());
        switch (state){
            case ADD_BOOK -> addBookState();
            case SEARCH_BOOK -> searchBookState();
            case MY_FAVOURITE_BOOKS -> myFavouriteBooksState();
            case MAIN_MENU -> mainMenuState();
            default -> bot.execute(new SendMessage(curUser.getId(), "Mavjud bo'lmagan menu tanlandi"));
        }
    }

    private void searchBookState() {

        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
        changeStates(BaseState.SEARCH_BOOK_STATE, null);

        String data = "";
        if (curUser.getState() == null) {
            data = update.callbackQuery().data();
            SearchBookState state = SearchBookState.valueOf(data);
            changeState(state.name());
        }

        switch (data) {
            case "BY_NAME" -> bot.execute(new SendMessage(curUser.getId(), "Enter name: "));
            case "BY_AUTHOR" -> bot.execute(new SendMessage(curUser.getId(), "Enter author: "));
            case "BY_GENRE" -> {
                bot.execute(messageMaker.enterSelectGenreMenu(curUser));
            }
            case "ALL_BOOKS" -> {
                List<Book> allBooks = bookService.getAllBooks();
                StringJoiner allBookStr = showBookList(allBooks);
                bot.execute(new SendMessage(curUser.getId(), allBookStr.toString()));
            }
            case "BACK_TO_MAIN_MENU" -> changeStates(BaseState.MAIN_MENU_STATE, null);
        }
        changeState(SearchBookState.SEARCH_BY.name());
    }

    private void addBookState() {
        changeStates(BaseState.ADD_BOOK_STATE, AddBookState.ENTER_BOOK_NAME.name());

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
