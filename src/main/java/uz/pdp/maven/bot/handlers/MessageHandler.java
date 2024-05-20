package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.types.bookTypes.Genre;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.MainMenuState;
import uz.pdp.maven.bot.states.child.RegistrationState;

import java.util.Objects;

import static uz.pdp.maven.bot.maker.MessageMaker.welcomeMessage;

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
            if (Objects.equals(curUser.getBaseState(), null)){
                handleNotStart(curUser);
            } else if (Objects.equals(text, "/start")) {
                handleStartCommand(from);
            } else {
                switch (baseState) {
                    case REGISTRATION_STATE -> handleRegistrationMenu();
                    case MAIN_MENU_STATE -> handleMainMenu(curUser);
                    case ADD_BOOK_STATE -> handleAddBook(curUser);
                    case SEARCH_BOOK_STATE -> handleSearchBook(curUser);
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
            Book newBook = bookService.getNewOrNonCompletedBookByUserId(curUser.getId());
            switch (curState) {
                case AddBookState.ENTER_BOOK_NAME -> {
                    String name = getText();
                    newBook.setName(name);
                    System.out.println("Name: " + newBook.getName());
                    bookService.save(newBook);
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage bookAuthorMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(bookAuthorMessage);
                    return;
                }
                case ENTER_BOOK_AUTHOR -> {
                    String author = getText();
                    System.out.println("Author: " + newBook.getAuthor());
                    newBook.setAuthor(author);
                    bookService.save(newBook);
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                    SendMessage sendMessage = messageMaker.enterSelectGenreMenu(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case SELECT_BOOK_GENRE -> {
                    Genre genre = getGenre();
                    System.out.println("Genre: " + newBook.getGenre().toString());
                    newBook.setGenre(genre);
                    bookService.save(newBook);
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_DESCRIPTION -> {
                    String description = getText();
                    System.out.println("Description: " + newBook.getDescription());
                    newBook.setDescription(description);
                    bookService.save(newBook);
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_PHOTO_ID -> {
                    String photoId = update.message().photo()[0].fileId();
                    System.out.println("Photo Id: " + newBook.getPhotoId());
                    newBook.setPhotoId(photoId);
                    bookService.save(newBook);
                    changeState(AddBookState.ENTER_BOOK_FILE_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookFile(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_FILE_ID -> {
                    String fileId = update.message().document().fileId();
                    System.out.println("File Id: " + newBook.getFileId());
                    newBook.setFileId(fileId);
                    bookService.save(newBook);
                    changeState(null);
                }
                default -> {
                    System.out.println("Xatolik");
                    return;
                }
            }

            if (checkBookIsValid(newBook)) {
                newBook.setComplete(true);
                changeStates(BaseState.MAIN_MENU_STATE, null);
            }

            if (Objects.equals(newBook.isComplete(), true)) {
                bookService.save(newBook);
                SendMessage bookIsAddedMessage = messageMaker.bookIsAddedMessage(curUser, newBook);
                bot.execute(bookIsAddedMessage);
            } else {
                handleAddBook(curUser);
            }
            handleMainMenu(curUser);
        }
    }


    private boolean checkBookIsValid(Book book) {
        return !(checkStrIsBlankNullAndEmpty(book.getName())
                && checkStrIsBlankNullAndEmpty(book.getAuthor())
                && checkStrIsBlankNullAndEmpty(book.getDescription())
                && Objects.isNull(book.getGenre())
                && checkStrIsBlankNullAndEmpty(book.getPhotoId())
                && checkStrIsBlankNullAndEmpty(book.getFileId())
                && checkStrIsBlankNullAndEmpty(book.getId()));
    }

    private Genre getGenre() {
        String genreStr = update.message().text();
        return Genre.valueOf(genreStr);
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

    private void handleSearchBook(MyUser curUser) {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
    }
}
