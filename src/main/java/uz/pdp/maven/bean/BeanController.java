package uz.pdp.maven.bean;

import uz.pdp.maven.backend.service.userService.UserService;

public interface BeanController {

    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);

}
