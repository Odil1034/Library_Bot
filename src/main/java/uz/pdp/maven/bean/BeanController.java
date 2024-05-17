package uz.pdp.maven.bean;

import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.bookService.BookService;
import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.bot.maker.MessageMaker;

public interface BeanController {

    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<BookService> bookServiceByThreadLocal = ThreadLocal.withInitial(BookService::new);
    ThreadLocal<MessageMaker> messageMakerByThreadLocal = ThreadLocal.withInitial(MessageMaker::new);
}
