package uz.pdp.maven.backend.bean;

import uz.pdp.maven.backend.service.UserService;

public interface BeanController {

    ThreadLocal<UserService> userServiceByThreadLocal = ThreadLocal.withInitial(UserService::new);
}
