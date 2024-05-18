package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.bookService.BookService;
import uz.pdp.maven.backend.types.bookTypes.Genre;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.addBookState.AddBookState;

import java.util.Objects;

import static uz.pdp.maven.bot.maker.MessageMaker.welcomeMessage;

public class MessageHandler extends BaseHandler {

    private final BookService bookService;

    public MessageHandler(BookService bookService) {
        this.bookService = bookService;
    }

    public MessageHandler() {
        this.bookService = new BookService();
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();

        if (text != null) {
            if (Objects.equals(text, "/start")) {
                handleStartCommand(from);
            } else {
                String baseState = curUser.getBaseState();
                BaseState curBaseState = BaseState.valueOf(baseState);

                switch (curBaseState) {
                    case MAIN_MENU_STATE:
                        handleMainMenu(curUser);
                        break;
                    case ADD_BOOK_STATE:
                        handleAddBook(curUser, text);
                        break;
                    case SEARCH_BOOK_STATE:
                        handleSearchBook(curUser);
                        break;
                    case MY_FAVOURITE_BOOKS_STATE:
                        handleMyFavouriteBook(curUser);
                        break;
                    default:
                        bot.execute(new SendMessage(curUser.getId(), "Unexpected option"));
                        break;
                }
            }
        } else if (message.contact() != null) {
            handleContactMessage(message.contact());
        }
    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (Objects.isNull(curUser.getPhoneNumber())
                || curUser.getPhoneNumber().isEmpty()
                || curUser.getPhoneNumber().isBlank()) {
            enterPhoneNumber();

            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        } else {
            handleMainMenu(curUser);

            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {
    }

    private void handleAddBook(MyUser curUser, String text) {
        AddBookState addBookState = AddBookState.valueOf(curUser.getState());
        switch (addBookState) {
            case ENTER_BOOK_NAME:
                curUser.setState(AddBookState.ENTER_BOOK_AUTHOR.name());
                curUser.setTempBookName(text);
                bot.execute(new SendMessage(curUser.getId(), "Please enter the author's name:"));
                break;
            case ENTER_BOOK_AUTHOR:
                curUser.setState(AddBookState.ENTER_BOOK_GENRE.name());
                curUser.setTempAuthor(text);
                bot.execute(new SendMessage(curUser.getId(), "Please enter the genre:"));
                break;
            case ENTER_BOOK_GENRE:
                curUser.setState(AddBookState.ENTER_BOOK_DESCRIPTION.name());
                curUser.setTempGenre(text);
                bot.execute(new SendMessage(curUser.getId(), "Please enter the description:"));
                break;
            case ENTER_BOOK_DESCRIPTION:
                curUser.setState(AddBookState.ENTER_BOOK_FILE_ID.name());
                curUser.setTempDescription(text);
                bot.execute(new SendMessage(curUser.getId(), "Please upload the file or send the file ID:"));
                break;
            case ENTER_BOOK_FILE_ID:
                curUser.setState(AddBookState.ENTER_BOOK_PHOTO_ID.name());
                curUser.setTempFileId(text);
                bot.execute(new SendMessage(curUser.getId(), "Please upload the photo or send the photo ID:"));
                break;
            case ENTER_BOOK_PHOTO_ID:
                curUser.setTempPhotoId(text);
                saveBook(curUser);
                curUser.setState(null);
                curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
                bot.execute(new SendMessage(curUser.getId(), "Book has been successfully added!"));
                handleMainMenu(curUser);
                break;
            default:
                curUser.setState(AddBookState.ENTER_BOOK_NAME.name());
                bot.execute(new SendMessage(curUser.getId(), "Please enter the book name:"));
                break;
        }
        userService.save(curUser);
    }

    private void saveBook(MyUser curUser) {
        Book book = Book.builder()
                .name(curUser.getTempBookName())
                .author(curUser.getTempAuthor())
                .genre(Genre.valueOf(curUser.getTempGenre()))
                .description(curUser.getTempDescription())
                .fileId(curUser.getTempFileId())
                .photoId(curUser.getTempPhotoId())
                .userId(curUser.getId())
                .isComplete(true)
                .build();
        bookService.save(book);
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
