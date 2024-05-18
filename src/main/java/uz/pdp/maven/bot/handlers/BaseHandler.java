package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.service.bookService.BookService;
import uz.pdp.maven.bean.BeanController;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.backend.paths.PathConstants;
import uz.pdp.maven.bot.maker.MessageMaker;
import uz.pdp.maven.bot.states.base.BaseState;

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

    protected void mainMenu() {
        curUser.setState(BaseState.MAIN_MENU_STATE.name());
        userService.save(curUser);
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    protected void deleteMessage(int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(curUser.getId(), messageId);
        bot.execute(deleteMessage);
    }



}
