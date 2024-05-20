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
            if (Objects.equals(text, "/start")) {
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
        }
    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (Objects.isNull(curUser.getPhoneNumber()) ||
                curUser.getPhoneNumber().isEmpty() ||
                curUser.getPhoneNumber().isBlank()) {
            enterPhoneNumber();
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.REGISTERED.name());
        } else {
            handleMainMenu(curUser);
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.REGISTERED.name());
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {

    }

    private void handleAddBook(MyUser curUser) {

        String state = curUser.getState();
        AddBookState curState;

        if (state != null) {
            curState = AddBookState.valueOf(state);
            Book newBookBuilder = builder().build();

            switch (curState) {
                case AddBookState.ENTER_BOOK_NAME -> {
                    newBookBuilder.setName(getText());
                    System.out.println("Name: " + newBookBuilder.getName());
                    changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
                }
                case ENTER_BOOK_AUTHOR -> {
                    SendMessage bookAuthorMessage = messageMaker.enterBookAuthor(curUser);
                    bot.execute(bookAuthorMessage);
                    newBookBuilder.setAuthor(getText());
                    System.out.println("Author: " + newBookBuilder.getAuthor());
                    changeState(AddBookState.ENTER_BOOK_GENRE.name());
                }
                case ENTER_BOOK_GENRE -> {
                    SendMessage sendMessage = messageMaker.enterSelectGenreMenu(curUser);
                    bot.execute(sendMessage);
                    newBookBuilder.setGenre(getGenre());
                    System.out.println("Genre: " + newBookBuilder.getGenre());
                    changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                }
                case ENTER_BOOK_DESCRIPTION -> {
                    SendMessage sendMessage = messageMaker.enterBookDescription(curUser);
                    bot.execute(sendMessage);
                    newBookBuilder.setDescription(getText());
                    System.out.println("Description: "+ newBookBuilder.getDescription());
                    changeState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                }
                case ENTER_BOOK_PHOTO_ID -> {
                    SendMessage sendMessage = messageMaker.enterBookPhoto(curUser);
                    bot.execute(sendMessage);
                    PhotoSize[] photo = update.message().photo();
                    for (PhotoSize photoSize : photo) {
                        newBookBuilder.setPhotoId(photoSize.fileId());
                    }
                    System.out.println("Photo Id: " + newBookBuilder.getFileId());
                    changeState(AddBookState.ENTER_BOOK_FILE_ID.name());
                }
                case ENTER_BOOK_FILE_ID -> {
                    SendMessage sendMessage = messageMaker.enterBookFile(curUser);
                    bot.execute(sendMessage);
                    newBookBuilder.setFileId(update.message().document().fileId());
                    System.out.println("File Id: " + newBookBuilder.getFileId());
                    changeState(null);
                }
                default -> {
                    System.out.println("Xatolik");
                    return;
                }
            }

            BookBuilder newBook = Book.builder();
            if (!checkBookIsValid(newBookBuilder)) {
                newBook.name(newBookBuilder.getName())
                        .author(newBookBuilder.getAuthor())
                        .genre(newBookBuilder.getGenre())
                        .userId(curUser.getId())
                        .photoId(newBookBuilder.getPhotoId())
                        .description(newBookBuilder.getDescription())
                        .fileId(newBookBuilder.getFileId())
                        .isComplete(true)
//                        .Id()
                        .build();
                changeStates(BaseState.MAIN_MENU_STATE, null);
            } else {
                newBook.isComplete(false);
            }

            if (Objects.equals(newBook.build().isComplete(), true)) {
                SendMessage bookIsAddedMessage = messageMaker.bookIsAddedMessage(curUser, newBook.build());
                bookService.save(newBook.build());
                bot.execute(bookIsAddedMessage);
            } else {
                handleAddBook(curUser);
            }
            handleMainMenu(curUser);
        } else {

        }
    }


    private boolean checkBookIsValid(Book book) {
        return !(Objects.isNull(book.getGenre())
                && checkStrIsBlankNullAndEmpty(book.getFileId())
                && checkStrIsBlankNullAndEmpty(book.getAuthor())
                && checkStrIsBlankNullAndEmpty(book.getName())
                && checkStrIsBlankNullAndEmpty(book.getPhotoId())
                && checkStrIsBlankNullAndEmpty(book.getDescription()));
    }

    public boolean checkStrIsBlankNullAndEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    private Genre getGenre() {
        SendMessage sendMessage = messageMaker.enterSelectGenreMenu(curUser);
        bot.execute(sendMessage);
        String genreStr = update.message().text();
        return Genre.valueOf(genreStr);
    }

    private String getText() {
        return update.message().text();
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
        if(curUser.getState().equals(RegistrationState.REGISTERED.name())){
            SendMessage sendMessage = messageMaker.mainMenu(curUser);
            bot.execute(sendMessage);
        }else {
            new SendMessage(curUser.getId(),
                    "Siz registratsiyadan o'tmagansiz " +
                            "\nRegistratsiyadan o'tish uchun telefon raqamingizni kiriting: ");
            enterPhoneNumber();
        }
    }

    private void handleSearchBook(MyUser curUser) {
        SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
        bot.execute(sendMessage);
    }
}
