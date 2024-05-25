package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.service.bookService.filter.Filter;
import uz.pdp.maven.backend.types.bookTypes.Genre;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.*;

import java.util.List;
import java.util.Objects;

import static uz.pdp.maven.bot.states.child.MainMenuState.*;
import static uz.pdp.maven.bot.states.child.SearchByState.*;

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
        } else if (Objects.equals(baseState, BaseState.SEARCH_BOOK_STATE)) {
            searchBookState();
        } else if (Objects.equals(baseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
            myFavouriteBooksState();
        }
    }

    private void mainState() {

        String stateStr = update.callbackQuery().data();
        MainMenuState state = MainMenuState.valueOf(stateStr);
        if (state == MAIN_MENU) {
            mainMenu();
        } else if(state == SEARCH_BOOK){
            searchBookState();
        }else if(state == ADD_BOOK){
            addBookState();
        }else if(state == MY_FAVOURITE_BOOKS){
            myFavouriteBooksState();
        }else {

        }
    }

    private void mainMenu() {
        String data =  update.callbackQuery().data();
        MainMenuState state = MainMenuState.valueOf(data);
        Message message = update.callbackQuery().message();
        changeState(data);
        switch (state) {
            case ADD_BOOK -> addBookState();
            case SEARCH_BOOK -> searchBookState();
            case MY_FAVOURITE_BOOKS -> myFavouriteBooksState();
            default -> bot.execute(new SendMessage(curUser.getId(), "Anything is wrong"));
        }
        deleteMessage(message.messageId());
        userService.save(curUser);
    }

    private void addBookState() {
        if (curUser.getBaseState().equals(BaseState.MAIN_MENU_STATE.name()) &&
                curUser.getState().equals(MAIN_MENU.name()))
            changeStates(BaseState.ADD_BOOK_STATE, AddBookState.ENTER_BOOK_NAME.name());
        Message message = update.callbackQuery().message();
        deleteMessage(message.messageId());
        String stateStr = curUser.getState();
        AddBookState state = AddBookState.valueOf(stateStr);
        String data = update.callbackQuery().data();

        if (backToMainMenu(data, message)) return;

        switch (state) {
            case ENTER_BOOK_NAME -> {
                backToMainMenu(data, message);
                bot.execute(new SendMessage(curUser.getId(), "ADD BOOK INFO\n"));
                SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                bot.execute(bookNameMessage);
                changeState(AddBookState.ENTER_BOOK_NAME.name());
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_AUTHOR -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                    SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                    bot.execute(bookNameMessage);
                    deleteMessage(update.callbackQuery().message().messageId());
                    return;
                }
                SendMessage sendMessage = messageMaker.selectGenre(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
            case SELECT_BOOK_GENRE -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage sendMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                    return;
                }
                Genre genre = Genre.valueOf(data);
                curBook = bookService.getNewOrNonCompletedBookByUserId(Long.valueOf(curUser.getId()));
                curBook.setGenre(genre);
                bookService.save(curBook);

                changeState(AddBookState.ENTER_BOOK_LANGUAGE.name());
                SendMessage sendMessage = messageMaker.enterBookLanguage(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_LANGUAGE -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                    SendMessage sendMessage = messageMaker.selectGenre(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
            case ENTER_BOOK_PAGE -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_LANGUAGE.name());
                    SendMessage sendMessage = messageMaker.enterBookLanguage(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_DESCRIPTION -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_PHOTO_ID -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_PAGE.name());
                    SendMessage sendMessage = messageMaker.enterBookPage(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_FILE_ID -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
            default -> anyThingIsWrongMessage();
        }
    }

    private void searchBookState() {
        if (Objects.equals(curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())
                && Objects.equals(update.callbackQuery().data(), (SEARCH_BOOK.name()))) {
            changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SEARCH_BY.name());
            bot.execute(messageMaker.searchBookMenu(curUser));
        } else {
            changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SEARCH_BY.name());
            bot.execute(messageMaker.searchBookMenu(curUser));
            searchBookMenu();
        }
    }

    private void searchBookMenu() {

        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();

        deleteMessage(message.messageId());

        String stateStr = curUser.getState();
        SearchBookState state = SearchBookState.valueOf(stateStr);

        switch (state) {
            case SEARCH_BY -> searchByMenu();
            case BOOK_LIST -> bookList();
            case SELECT_FILE -> selectFile();
            case DOWNLOAD -> download();
            case ADD_MY_FAVOURITE_BOOKS -> addMyFavouriteBooks();
        }
    }

    private void searchByMenu() {

        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        SearchByState state = SearchByState.valueOf(data);
        Message message = callbackQuery.message();

        if (backToMainMenu(data, message)) return;

        switch (state) {
            case BY_NAME -> {
                if (data.equals("BACK")) {
                    changeState(MAIN_MENU.name());
                    SendMessage sendMessage = messageMaker.mainMenu(curUser);
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                    return;
                }
                bot.execute(messageMaker.searchBookMenu(curUser));
                bot.execute(new SendMessage(curUser.getId(), "SEARCHING BOOK INFO"));
                bot.execute(messageMaker.enterBookNameMenu(curUser));
            }
            case BY_AUTHOR -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage sendMessage1 = messageMaker.enterBookAuthor(curUser);
                    bot.execute(sendMessage1);
                    deleteMessage(message.messageId());
                    return;
                }
                bot.execute(messageMaker.enterBookAuthor(curUser));
            }
            case BY_GENRE -> {
                SendMessage sendMessage = messageMaker.selectGenre(curUser);
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage sendMessage1 = messageMaker.enterBookAuthor(curUser);
                    bot.execute(sendMessage1);
                    deleteMessage(message.messageId());
                } else {
                    Genre genre = Genre.valueOf(data);
                    Filter<Book> bookFilterByGenre = (b) -> Objects.equals(b.getGenre(), genre);
                    List<Book> books = getBookListStrByFilter(bookFilterByGenre);

                    String showBooksStr = messageMaker.showBookList(books);
                    bot.execute(new SendMessage(curUser.getId(), showBooksStr));
                    bot.execute(sendMessage);
                    deleteMessage(message.messageId());
                }
            }
            case ALL_BOOKS -> {
                bot.execute(new SendMessage(curUser.getId(), "ALL BOOKS\n"));
                List<Book> allBooks = bookService.getAllBooks();
                String messageStr = messageMaker.showBookList(allBooks);
                InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(allBooks);
                SendMessage sendMessage = new SendMessage(curUser.getId(), messageStr);
                sendMessage.replyMarkup(keyboardMarkup);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
            }
        }
    }


    private void bookList() {

    }

    private void selectFile() {

    }

    private void download() {

    }

    private void addMyFavouriteBooks() {

    }


    private void myFavouriteBooksState() {
        changeStates(BaseState.MY_FAVOURITE_BOOKS_STATE, null);
        System.out.println("myFavouriteBooksState is run");
    }

    public void anyThingIsWrongMessage() {
        bot.execute(new SendMessage(curUser.getId(), "Anything is wrong ❌❌❌"));
    }

    private boolean backToMainMenu(String data, Message message) {
        if (data.equals("MAIN_MENU")) {
            changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
            mainState();
            deleteMessage(message.messageId());
            return true;
        }
        return false;
    }
}
