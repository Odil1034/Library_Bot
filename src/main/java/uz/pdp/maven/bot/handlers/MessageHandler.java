package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.types.bookTypes.Genre;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.AddBookState;
import uz.pdp.maven.bot.states.child.RegistrationState;

import java.util.Objects;

import static uz.pdp.maven.backend.models.book.Book.*;
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
            if (curUser.getPhoneNumber() == null
                    && curUser.getBaseState().equals(BaseState.REGISTRATION_STATE.name())
                    && curUser.getState() == null
                    && !Objects.equals(text, "/start")) {
                handleNotStart(curUser);
            } else if (Objects.equals(text, "/start")) {
                handleStartCommand(from);
            } else {
                switch (baseState) {
                    case REGISTRATION_STATE -> enterPhoneNumber();
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
        }/*
        if (checkStrIsBlankNullAndEmpty(text)) {
            incorrectValueMes(text);
        }*/
    }

    private void handleNotStart(MyUser curUser) {
        SendMessage sendMessage = welcomeMessage(update.message().from());
        bot.execute(sendMessage);
        bot.execute(new SendMessage(curUser.getId(), "Botni boshlash uchun /start tugmasini bosing"));
        changeStates(BaseState.REGISTRATION_STATE, RegistrationState.NOT_REGISTERED.name());
    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (checkStrIsBlankNullAndEmpty(curUser.getPhoneNumber())) {
            enterPhoneNumber();
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.NOT_REGISTERED.name());
        } else {
            changeStates(BaseState.MAIN_MENU_STATE, null);
            handleMainMenu(curUser);
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {

    }

    private void handleAddBook(MyUser curUser) {

        String state = curUser.getState();
        AddBookState curState;


        String photoId1 = update.message().photo()[0].fileId();

        if (state != null) {
            curState = AddBookState.valueOf(state);
            String name = null;
            String author  = null;
            String description  = null;
            String photoId;
            String fileId;
            Genre genre = null;
            if(name == null) {
                name = getStrInfoOfBook();
            }
            if(author == null){
                author = getStrInfoOfBook();
            }if(description == null){
                description = getStrInfoOfBook();
            }
            switch (curState) {
                case AddBookState.ENTER_BOOK_NAME -> {

                    System.out.println("Name: " + name);
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                    SendMessage bookAuthorMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(bookAuthorMessage);
                    return;
                }
                case ENTER_BOOK_AUTHOR -> {
                    if (checkStrIsBlankNullAndEmpty(getText())) {
                        author = getText();
                    }
                    System.out.println("Author: " + author);
                    changeState(AddBookState.SELECT_BOOK_GENRE.name());
                    SendMessage sendMessage = messageMaker.enterSelectGenreMenu(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case SELECT_BOOK_GENRE -> {
                    genre = getGenre();
                    System.out.println("Genre: " + genre);
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_DESCRIPTION -> {
                    if (checkStrIsBlankNullAndEmpty(getText())) {
                        description = getText();
                    }
                    System.out.println("Description: " + description);
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_PHOTO_ID -> {
                    photoId = update.message().photo()[0].fileId();
                    System.out.println("Photo Id: " + photoId);
                    changeState(AddBookState.ENTER_BOOK_FILE_ID.name());
                    SendMessage sendMessage = messageMaker.enterBookFile(curUser);
                    bot.execute(sendMessage);
                    return;
                }
                case ENTER_BOOK_FILE_ID -> {
                    fileId = update.message().document().fileId();
                    System.out.println("File Id: " + fileId);
                    changeState(null);
                }
                default -> {
                    System.out.println("Xatolik");
                    return;
                }
            }

            Book newBook = builder()
                    .name(name)
                    .author(author)
                    .description(description)
                    .genre(genre)
                    .photoId(photoId1)
                    .userId(curUser.getId())
                    .fileId(fileId)
                    .isComplete(false)
                    .build();

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

    private String getStrInfoOfBook() {
        return !checkStrIsBlankNullAndEmpty(getText()) ? getText() : null;
    }


    private boolean checkBookIsValid(Book book) {
        return !(checkStrIsBlankNullAndEmpty(book.getName())
                && checkStrIsBlankNullAndEmpty(book.getAuthor())
                && checkStrIsBlankNullAndEmpty(book.getDescription())
                && Objects.isNull(book.getGenre())
                && checkStrIsBlankNullAndEmpty(book.getPhotoId())
                && checkStrIsBlankNullAndEmpty(book.getFileId()));
    }

    private Genre getGenre() {
        String genreStr = update.message().text();
        return Genre.valueOf(genreStr);
    }

    private String getText() {
        String text = update.message().text();
        return text;
    }

    private void handleContactMessage(Contact contact) {
        String phoneNumber = contact.phoneNumber();
        curUser.setPhoneNumber(phoneNumber);
        changeBaseState(BaseState.MAIN_MENU_STATE);
        handleMainMenu(curUser);
    }

    private void enterPhoneNumber() {
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }

    public void handleMainMenu(MyUser curUser) {
        if (curUser.getBaseState().equals(BaseState.MAIN_MENU_STATE.name())) {
            SendMessage sendMessage = messageMaker.mainMenu(curUser);
            bot.execute(sendMessage);
        } else if (curUser.getState() != null) {
            if (curUser.getState().equals(RegistrationState.NOT_REGISTERED.name())) {
                new SendMessage(curUser.getId(),
                        "Siz registratsiyadan o'tmagansiz " +
                                "\nRegistratsiyadan o'tish uchun telefon raqamingizni kiriting: ");
                enterPhoneNumber();
            }
        }
    }

    private void handleSearchBook(MyUser curUser) {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
    }
}
