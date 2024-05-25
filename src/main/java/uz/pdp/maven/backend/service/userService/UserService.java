package uz.pdp.maven.backend.service.userService;

import com.pengrad.telegrambot.model.User;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.paths.PathConstants;
import uz.pdp.maven.backend.service.BaseService;
import uz.pdp.maven.backend.utils.fileWriterAndLoader.FileWriterAndLoader;

import java.util.List;
import java.util.Objects;

public class UserService implements BaseService, PathConstants {

    private final FileWriterAndLoader<MyUser> writerAndReader;

    public UserService() {
        this.writerAndReader = new FileWriterAndLoader<>(USERS_JSON);
    }

    public void save(MyUser myUser) {
        List<MyUser> users = writerAndReader.load(MyUser.class);
        for (int i = 0; i < users.size(); i++) {
            MyUser curUser = users.get(i);
            if (Objects.equals(curUser.getId(), myUser.getId())) {
                users.set(i, myUser);
                writerAndReader.write(users);
                return;
            }
        }
        users.add(myUser);
        writerAndReader.write(users);
    }

    public MyUser get(Long id) {
        List<MyUser> users = writerAndReader.load(MyUser.class);
        for (MyUser user : users) {
            if (Objects.equals(user.getId(), id)) {
                return user;
            }
        }
        return null;
    }
}
