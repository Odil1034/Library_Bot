package uz.pdp.maven.backend.service.userService;

import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.paths.PathConstants;
import uz.pdp.maven.backend.service.BaseService;
import uz.pdp.maven.backend.utils.fileWriterAndLoader.FileWriterAndLoader;

import java.util.List;
import java.util.Objects;

public class UserService implements BaseService, PathConstants {

    FileWriterAndLoader<MyUser> writerAndReader;

    public UserService() {
        this.writerAndReader = new FileWriterAndLoader<>(USERS_TXT);
    }

    public void save(MyUser myUser){
        List<MyUser> users = writerAndReader.load();

        for (int i = 0; i < users.size(); i++) {
            MyUser curUser = users.get(i);
            if(Objects.equals(curUser.getId(), myUser.getId())){
                users.set(i, myUser);
                writerAndReader.write(users);
                return;
            }
        }

        users.add(myUser);
        writerAndReader.write(users);
        return;
    }

    public MyUser get(Long Id){
        List<MyUser> users = writerAndReader.load();

        for (int i = 0; i < users.size(); i++) {
            MyUser curUser = users.get(i);
            if(Objects.equals(curUser.getId(), Id)){
                return curUser;
            }
        }

        return null;
    }
}
