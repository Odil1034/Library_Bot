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
        } else if (Objects.equals(baseState, BaseState.SEARCH_BY_STATE)) {
            searchByState();
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

        MainMenuState state;
        if (curUser.getState() == null) {
            String data = update.callbackQuery().data();
            state = MainMenuState.valueOf(data);
        } else {
            String stateStr = curUser.getState();
            state = MainMenuState.valueOf(stateStr);
        }
        CallbackQuery callbackQuery = update.callbackQuery();
        switch (state) {
            case MAIN_MENU -> {
                String data = callbackQuery.data();
                mainMenu(data);
            }
            case SEARCH_BOOK -> searchBookState();
            case MY_FAVOURITE_BOOKS -> myFavouriteBooksState();
            case ADD_BOOK -> addBookState();
        }
    }

    private void mainMenu(String data) {

        MainMenuState state = MainMenuState.valueOf(data);
        Message message = update.callbackQuery().message();
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
                SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                bot.execute(bookNameMessage);
                changeState(AddBookState.ENTER_BOOK_NAME.name());
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_AUTHOR -> {
                if (data.equals("BACK")) {
                    SendMessage bookNameMessage = messageMaker.enterBookNameMenu(curUser);
                    bot.execute(bookNameMessage);
                    changeState(AddBookState.ENTER_BOOK_NAME.name());
                    return;
                }
                SendMessage sendMessage = messageMaker.selectGenre(curUser);
                bot.execute(sendMessage);
                deleteMessage(message.messageId());
                deleteMessage(message.messageId());
            }
            case SELECT_BOOK_GENRE -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage sendMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                Genre genre = Genre.valueOf(data);
                curBook = bookService.getNewOrNonCompletedBookByUserId(curUser.getId());
                curBook.setGenre(genre);
                bookService.save(curBook);

                changeState(AddBookState.ENTER_BOOK_LANGUAGE.name());
                SendMessage sendMessage = messageMaker.enterBookLanguage(curUser);
                bot.execute(sendMessage);
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
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_DESCRIPTION -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_PHOTO_ID -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_PAGE.name());
                    SendMessage sendMessage = messageMaker.enterBookPage(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                deleteMessage(message.messageId());
            }
            case ENTER_BOOK_FILE_ID -> {
                if (data.equals("BACK")) {
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                }
            }
            default -> {

            }
        }
    }

    private void searchBookState() {

        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        if (data.equals(SEARCH_BOOK.name())) {
            bot.execute(messageMaker.searchBookMenu(curUser));
            changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SEARCH_BY.name());
            return;
        }

        Message message = callbackQuery.message();
        if (backToMainMenu(data, message)) return;
        String stateStr = curUser.getState();
        SearchBookState state = SearchBookState.valueOf(stateStr);

        switch (state) {
            case SEARCH_BY -> {
                changeStates(BaseState.SEARCH_BY_STATE, SearchByState.BY_NAME.name());
                searchByState();
            }
            case SELECT_FILE -> {
                String bookId = update.callbackQuery().data();
                Book book = bookService.get(bookId);
                if (book == null) {
                    bot.execute(messageMaker.bookNotFound(curUser));
                    changeStates(BaseState.SEARCH_BY_STATE, SearchByState.BY_NAME.name());
                    return;
                }
                bot.execute(messageMaker.sendBookPhoto(curUser, book));
                bot.execute(messageMaker.sendDocument(curUser, book));
            }
        }
    }

    private boolean backToMainMenu(String data, Message message) {
        if (data.equals("MAIN_MENU")) {
            changeStates(BaseState.MAIN_MENU_STATE, MAIN_MENU.name());
            mainState();
            return true;
        }
        return false;
    }

    private void searchByState() {

        CallbackQuery callbackQuery = update.callbackQuery();
        Message message = callbackQuery.message();
        String data = callbackQuery.data();

        if(backToMainMenu(data, message)) return;
        if(curUser.getState().equals(HANDLE_GENRE.name())){
            handleBookGenre();
        }
        SearchByState state = SearchByState.valueOf(data);
        changeBaseState(BaseState.SEARCH_BY_STATE);
        switch (state) {
            case BY_NAME -> {
                bot.execute(new SendMessage(curUser.getId(), "SEARCHING BOOK INFO \n\nEnter book name: "));
                changeState(SearchByState.BY_NAME.name());
            }
            case BY_AUTHOR -> {
                bot.execute(new SendMessage(curUser.getId(), "SEARCHING BOOK INFO \n\nEnter book author: "));
                changeState(SearchByState.BY_AUTHOR.name());
            }
            case BY_GENRE -> {
                bot.execute(messageMaker.selectGenre(curUser));
                changeStates(BaseState.SEARCH_BY_STATE, HANDLE_GENRE.name());
            }
            case ALL_BOOKS -> {
                List<Book> allBooks = bookService.getAllBooks();
                SendMessage sendMessage = messageMaker.showBookList(curUser, allBooks);
                InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(allBooks);
                sendMessage.replyMarkup(keyboardMarkup);
                bot.execute(sendMessage);
                changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SELECT_FILE.name());
            }
        }
    }

    private void handleBookGenre() {
        String genreStr = update.callbackQuery().data();
        Genre genre = Genre.valueOf(genreStr);
        Filter<Book> bookFilterByGenre = (book -> book.getGenre().equals(genre));
        List<Book> bookList = getBookListStrByFilter(bookFilterByGenre);
        SendMessage sendMessage = messageMaker.showBookList(curUser, bookList);
        InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(bookList);
        sendMessage.replyMarkup(keyboardMarkup);
        bot.execute(sendMessage);
        changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SELECT_FILE.name());
    }

    private void myFavouriteBooksState() {
        changeStates(BaseState.MY_FAVOURITE_BOOKS_STATE, null);
        Filter<Book> bookFilter = (book -> book.getUserId().equals(curUser.getId()));
        List<Book> books = bookService.getBooksByFilter(bookFilter);
        SendMessage sendMessage = messageMaker.showBookList(curUser, books);
        InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(books);
        sendMessage.replyMarkup(keyboardMarkup);
        bot.execute(sendMessage);
    }
}
