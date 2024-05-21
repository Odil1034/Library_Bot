package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.service.bookService.BookService;
import uz.pdp.maven.backend.service.bookService.filter.Filter;
import uz.pdp.maven.bean.BeanController;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.backend.paths.PathConstants;
import uz.pdp.maven.bot.maker.MessageMaker;
import uz.pdp.maven.bot.states.base.BaseState;

import java.util.List;
import java.util.StringJoiner;

public abstract class BaseHandler implements PathConstants {

    protected TelegramBot bot;
    protected UserService userService;
    protected BookService bookService;
    protected MessageMaker messageMaker;
    protected Update update;
    protected MyUser curUser;
    protected Book curBook;

    public BaseHandler() {
        this.bot = new TelegramBot(BOT_TOKEN);
        this.userService = BeanController.userServiceByThreadLocal.get();
        this.bookService = BeanController.bookServiceByThreadLocal.get();
        this.messageMaker = BeanController.messageMakerByThreadLocal.get();
    }

    public abstract void handle(Update update);

    protected MyUser getUserOrCreate(User from) {
        MyUser myUser = userService.get(from.id());
        if (myUser == null) {
            MyUser newMyUser = MyUser.builder()
                    .Id(from.id())
                    .username(from.username())
                    .firstname(from.firstName())
                    .lastname(from.lastName())
                    .baseState(BaseState.REGISTRATION_STATE.name())
                    .build();
            userService.save(newMyUser);
            return newMyUser;
        } else {
            return myUser;
        }
    }

    protected void deleteMessage(int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }

    public void changeStates(BaseState changeBaseState, String childState) {
        curUser.setBaseState(changeBaseState.name());
        curUser.setState(childState);
        userService.save(curUser);
    }

    public void changeState(String childState) {
        curUser.setState(childState);
        userService.save(curUser);
    }

    public void changeBaseState(BaseState baseState) {
        curUser.setBaseState(baseState.name());
        userService.save(curUser);
    }

    public boolean checkStrIsBlankNullAndEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    public String incorrectValueMes(String value) {
        return "Enter incorrect value : " + value;
    }

    public @NotNull SendMessage getBookListStrByFilter(Filter<Book> bookFilter) {
        StringBuilder searchResultStr = new StringBuilder();
        List<Book> books = bookService.getBooksByFilter(bookFilter);
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            searchResultStr.append("SEARCH RESULT\n").append(i + 1).append(".   ").append(book.getName()).append("    ").append(book.getAuthor()).append("   ").append(book.getDescription());
        }
        return new SendMessage(curUser.getId(), searchResultStr.toString());
    }

    public StringJoiner showBookList(List<Book> bookList) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add("BOOK LIST\n");
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            stringJoiner.add(
                    i + 1 + ".  " + book.getName() + "\n    " + book.getAuthor() + book.getDescription());
        }

        return stringJoiner;
    }

    public StringJoiner showBook(Book book) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        String fileId = book.getFileId();

        bot.execute(new SendDocument(curUser.getId(), fileId));
        stringJoiner.add(". ⚙️" + book.getGenre()
                        + ".   \uD83D\uDCD3" + book.getName()
                        + "\n    ✍️" + book.getAuthor()
                        + "\n" + book.getDescription());

        return stringJoiner;
    }

}

