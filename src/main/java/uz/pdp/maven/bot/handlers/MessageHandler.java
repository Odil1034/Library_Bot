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

        if (Objects.isNull(curUser.getPhoneNumber()) || curUser.getPhoneNumber().isEmpty() || curUser.getPhoneNumber().isBlank()) {
            enterPhoneNumber();
            changeStates(BaseState.REGISTRATION_STATE, RegistrationState.REGISTER.name());
        } else {
            handleMainMenu(curUser);

            changeStates(BaseState.MAIN_MENU_STATE, null);
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {

    }

    private void handleAddBook(MyUser curUser) {

        changeState(AddBookState.ENTER_BOOK_NAME.name());
        String stateStr = curUser.getState();
        AddBookState curState = AddBookState.valueOf(stateStr);

        Book addedBookInfo = builder().build();
        SendMessage sendMessage;
        if (curState.equals(AddBookState.ENTER_BOOK_NAME)) {
            addedBookInfo.setName(getText());
            bot.execute(new SendMessage(curUser.getId(), "Kitobning nomi muvaffaqiyatli qabul qilindi ✅✅✅"));
            changeState(AddBookState.ENTER_BOOK_AUTHOR.name());
            sendMessage = messageMaker.enterBookAuthor(curUser);
            bot.execute(sendMessage);
        }
        if (Objects.equals(curUser.getState(),AddBookState.ENTER_BOOK_AUTHOR.name())) {
            addedBookInfo.setAuthor(getText());
            bot.execute(new SendMessage(curUser.getId(), "Kitobni muallifi muvaffaqiyatli qabul qilindi ✅✅✅"));
            changeState(AddBookState.SELECT_GENRE.name());
            sendMessage = messageMaker.enterSelectGenreMenu(curUser);
            bot.execute(sendMessage);
        }
        if (Objects.equals(curUser.getState(),AddBookState.SELECT_GENRE.name())) {
            addedBookInfo.setGenre(getGenre());
            bot.execute(new SendMessage(curUser.getId(), "Kitobning janri tanlandi ✅✅✅"));
            changeState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
            sendMessage = messageMaker.enterBookDescription(curUser);
            bot.execute(sendMessage);
        }
        if (Objects.equals(curUser.getState(), AddBookState.ENTER_BOOK_DESCRIPTION.name())) {
            sendMessage = messageMaker.enterBookPhoto(curUser);
            bot.execute(sendMessage);
            addedBookInfo.setDescription(getText());
            bot.execute(new SendMessage(curUser.getId(), "Kitobga description qabul qilindi ✅✅✅"));
            changeState(AddBookState.SELECT_GENRE.name());
        }
        if (Objects.equals(curUser.getState(), AddBookState.SELECT_GENRE.name())) {
            PhotoSize[] photo = update.message().photo();
            addedBookInfo.setPhotoId(photo[0].fileId());
            bot.execute(new SendMessage(curUser.getId(), "Kitobning rasmi qabul qilindi ✅✅✅"));
            sendMessage = messageMaker.enterBookFile(curUser);
            bot.execute(sendMessage);
            changeState(AddBookState.ENTER_BOOK_FILE.name());
        }
        if (Objects.equals(curUser.getState(), AddBookState.ENTER_BOOK_FILE.name())) {
            addedBookInfo.setFileId(update.message().document().fileId());
            bot.execute(new SendMessage(curUser.getId(), "Kitobning fayli qabul qilindi ✅✅✅"));
            changeState(null);
        }

        Book newBook;
        if (!checkBookIsValid(addedBookInfo)) {
            newBook = builder()
                    .name(addedBookInfo.getName())
                    .author(addedBookInfo.getAuthor())
                    .genre(addedBookInfo.getGenre())
                    .description(addedBookInfo.getDescription())
                    .photoId(addedBookInfo.getPhotoId())
                    .fileId(addedBookInfo.getFileId())
                    .userId(curUser.getId())
//                        .Id(update.message())
                    .isComplete(true)
                    .build();
            changeStates(BaseState.MAIN_MENU_STATE, null);

            assert newBook != null;
            if (newBook.isComplete()) {
                SendMessage bookIsAddedMessage = messageMaker.bookIsAddedMessage(curUser, newBook);
                bookService.save(newBook);
                bot.execute(bookIsAddedMessage);
            } else {
                handleAddBook(curUser);
            }
            handleMainMenu(curUser);
        }

    }


    private boolean checkBookIsValid(Book book) {
        return Objects.isNull(book.getGenre())
                && checkStrIsBlankNullAndEmpty(book.getFileId())
                && checkStrIsBlankNullAndEmpty(book.getAuthor())
                && checkStrIsBlankNullAndEmpty(book.getName())
                && checkStrIsBlankNullAndEmpty(book.getPhotoId())
                && checkStrIsBlankNullAndEmpty(book.getDescription());
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
        Message message = update.message();
        return message.text();
    }

    private void handleContactMessage(Contact contact) {
        String phoneNumber = contact.phoneNumber();
        curUser.setPhoneNumber(phoneNumber);
        curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
        userService.save(curUser);
        handleMainMenu(curUser);
    }

    private void enterPhoneNumber() {
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }

    public void handleMainMenu(MyUser curUser) {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleSearchBook(MyUser curUser) {
        if (Objects.equals(this.curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())) {
            SendMessage sendMessage = messageMaker.searchBookMenu(this.curUser);
            bot.execute(sendMessage);
        }
    }
}
