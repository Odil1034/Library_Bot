package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.bookService.filter.Filter;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.*;

import java.util.*;

import static uz.pdp.maven.bot.maker.MessageMaker.welcomeMessage;
import static uz.pdp.maven.bot.states.child.SearchByState.*;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();

        String baseStateStr = curUser.getBaseState();
        BaseState baseState = BaseState.valueOf(baseStateStr);

        if (text != null) {
            if (Objects.equals(curUser.getBaseState(), null)) {
                handleNotStart(curUser);
            } else if (Objects.equals(text, "/start")) {
                handleStartCommand(from);
            } else {
                switch (baseState) {
                    case REGISTRATION_STATE -> handleRegistrationMenu();
                    case MAIN_MENU_STATE -> handleMainMenu(curUser);
                    case ADD_BOOK_STATE -> handleAddBook(curUser);
                    case SEARCH_BOOK_STATE -> handleSearchBook();
                    case SEARCH_BY_STATE -> handleSearchBy();
                    case MY_FAVOURITE_BOOKS_STATE -> handleMyFavouriteBook(curUser);
                    default -> bot.execute(new SendMessage(curUser.getId(), "Unexpected option"));
                }
            }
        } else if (message.photo() != null) {
            if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
                handleAddBook(curUser);
            }
        } else if (message.document() != null) {
            if (Objects.equals(baseState, BaseState.ADD_BOOK_STATE)) {
                handleAddBook(curUser);
            }
        } else if (message.contact() != null) {
            handleContactMessage(message.contact());
        }
    }

    private void handleSearchBook() {

        String stateStr = curUser.getState();
        SearchBookState state = SearchBookState.valueOf(stateStr);
        switch (state) {
            case SEARCH_BY -> handleSearchBy();
            case SELECT_FILE -> handleSelectFile();
            default -> {}
        }
    }

    private void handleSelectFile() {
        bot.execute(messageMaker.bookNotFound(curUser));
    }

    private void handleNotStart(MyUser curUser) {
        bot.execute(new SendMessage(curUser.getId(), "Botni boshlash uchun /start tugmasini bosing"));
        changeStates(BaseState.REGISTRATION_STATE, RegistrationState.NOT_REGISTERED.name());
    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (checkStrIsBlankNullAndEmpty(curUser.getPhoneNumber())) {
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.NOT_REGISTERED.name());
            handleRegistrationMenu();
            changeState(RegistrationState.SEND_PHONE_NUMBER.name());
        } else {
            changeStates(BaseState.MAIN_MENU_STATE, MainMenuState.MAIN_MENU.name());
            handleMainMenu(curUser);
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {
        // My favourite Book Is Not write
    }

    private void handleAddBook(MyUser curUser) {

        String state = curUser.getState();
        AddBookState curState;

        if (state != null) {
            curState = AddBookState.valueOf(state);
            curBook = bookService.getNewOrNonCompletedBookByUserId(curUser.getId());
            switch (curState) {
                case AddBookState.ENTER_BOOK_NAME -> {
                    String name = getText();
                    curBook.setName(name);
                    bookService.save(curBook);
                    SendMessage sendMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(sendMessage);
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    return;
                }
                case ENTER_BOOK_AUTHOR -> {
                    String author = getText();
                    curBook.setAuthor(author);
                    bookService.save(curBook);
                    SendMessage sendMessage = messageMaker.selectGenre(curUser);
                    bot.execute(sendMessage);
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                    return;
                }
                case SELECT_BOOK_GENRE -> bot.execute(new SendMessage(curUser.getId(), "Janrni tanlang"));
                case ENTER_BOOK_LANGUAGE -> {
                    String language = getText();
                    curBook.setLanguage(language);
                    bookService.save(curBook);
                    SendMessage sendMessage = messageMaker.enterBookPage(curUser);
                    bot.execute(sendMessage);
                    changeState(AddBookState.ENTER_BOOK_PAGE.name());
                    return;
                }
                case ENTER_BOOK_PAGE -> {
                    int countOfPage = Integer.parseInt(getText());
                    curBook.setCountOfPage(countOfPage);
                    bookService.save(curBook);
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                    return;
                }
                case ENTER_BOOK_DESCRIPTION -> {
                    String description = getText();
                    curBook.setDescription(description);
                    bookService.save(curBook);
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_PHOTO_ID -> {
                    String photoId = update.message().photo()[0].fileId();
                    curBook.setPhotoId(photoId);
                    bookService.save(curBook);
                    changeState(AddBookState.ENTER_BOOK_FILE_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookFile(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_FILE_ID -> {
                    String fileId = update.message().document().fileId();
                    curBook.setFileId(fileId);
                    bookService.save(curBook);
                    changeState(null);
                }
                default -> {
                    System.out.println("Xatolik");
                    return;
                }
            }

            if (checkBookIsValid(curBook)) {
                curBook.setComplete(true);
                bookService.save(curBook);
                changeStates(BaseState.MAIN_MENU_STATE, null);
            }

            if (Objects.equals(curBook.isComplete(), true)) {
                bookService.save(curBook);
                SendMessage bookIsAddedMessage = messageMaker.bookIsAddedMessage(curUser, curBook);
                bot.execute(bookIsAddedMessage);
            } else {
                handleAddBook(curUser);
            }
            changeStates(BaseState.MAIN_MENU_STATE, MainMenuState.MAIN_MENU.name());
            bot.execute(messageMaker.mainMenu(curUser));
        }
    }


    private boolean checkBookIsValid(Book book) {
        return !(checkStrIsBlankNullAndEmpty(book.getName())
                && checkStrIsBlankNullAndEmpty(book.getAuthor())
                && checkStrIsBlankNullAndEmpty(book.getDescription())
                && Objects.isNull(book.getGenre())
                && checkStrIsBlankNullAndEmpty(book.getPhotoId())
                && checkStrIsBlankNullAndEmpty(book.getFileId())
                && checkStrIsBlankNullAndEmpty(book.getId())
                && checkStrIsBlankNullAndEmpty(book.getLanguage())
                && book.getCountOfPage() == 0
                && !book.isComplete());
    }

    private String getText() {
        return update.message().text();
    }

    private void handleContactMessage(Contact contact) {
        String phoneNumber = contact.phoneNumber();
        curUser.setPhoneNumber(phoneNumber);
        userService.save(curUser);
        changeState(RegistrationState.REGISTER.name());

        SendMessage sendMessage = messageMaker.handRegistrationMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleRegistrationMenu() {
        if (curUser.getState() == null) {
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.NOT_REGISTERED.name());
        }
        if (curUser.getState().equals(RegistrationState.NOT_REGISTERED.name())) {
            SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
            bot.execute(sendMessage);
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.SEND_PHONE_NUMBER.name());
        } else if (curUser.getState().equals(RegistrationState.REGISTER.name())) {
            changeStates(BaseState.MAIN_MENU_STATE, MainMenuState.MAIN_MENU.name());
        }
    }

    public void handleMainMenu(MyUser curUser) {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleSearchBy() {

        String stateStr = curUser.getState();
        SearchByState state = valueOf(stateStr);

        switch (state) {
            case BY_NAME -> {
                String name = getText();
                Filter<Book> bookFilterByName = (book -> book.getName().contains(name));
                List<Book> bookList = getBookListStrByFilter(bookFilterByName);
                SendMessage sendMessage = messageMaker.showBookList(curUser, bookList);
                InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(bookList);
                sendMessage.replyMarkup(keyboardMarkup);
                bot.execute(sendMessage);
            }
            case BY_AUTHOR -> {
                String author = getText();
                Filter<Book> bookFilterByAuthor = (book) -> book.getAuthor().contains(author);
                List<Book> bookList = getBookListStrByFilter(bookFilterByAuthor);
                SendMessage sendMessage = messageMaker.showBookList(curUser, bookList);
                InlineKeyboardMarkup keyboardMarkup = messageMaker.makeInlineKeyboardButtons(bookList);
                sendMessage.replyMarkup(keyboardMarkup);
                bot.execute(sendMessage);
            }
            case ALL_BOOKS, BY_GENRE -> {
            }
        }
        changeStates(BaseState.SEARCH_BOOK_STATE, SearchBookState.SELECT_FILE.name());
    }
}
