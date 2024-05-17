package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.maven.bean.BeanController;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.backend.paths.PathConstants;
import uz.pdp.maven.bot.states.registerState.RegisterState;

public abstract class BaseHandler implements PathConstants {

    protected TelegramBot bot;
    protected UserService userService;

    public BaseHandler() {
        this.bot = new TelegramBot(BOT_TOKEN);
        this.userService = BeanController.userServiceByThreadLocal.get();
    }

    public abstract void handle(Update update);

    protected MyUser getUserOrCreate(User from) {
        MyUser myUser = userService.get(from.id());
        if (myUser == null) {
            myUser = MyUser.builder()
                    .Id(from.id())
                    .username(from.username())
                    .firstname(from.firstName())
                    .lastname(from.lastName())
                    .baseState(RegisterState.REGISTER_STATE.name())
                    .build();
            userService.save(myUser);
        }
        return myUser;
    }
    /*protected void execute(Message message){
        
    }*/
}
