package uz.pdp.maven.bean;

import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.backend.service.bookService.BookService;

public interface BeanController {

    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
    ThreadLocal<BookService> bookServiceByThreadLocal = ThreadLocal.withInitial(BookService::new);
}
