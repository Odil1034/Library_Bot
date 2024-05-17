package uz.pdp.maven.bot.manager;

import com.pengrad.telegrambot.model.Update;
import uz.pdp.maven.bot.handlers.BaseHandler;
import uz.pdp.maven.bot.handlers.CallBackQueryHandler;
import uz.pdp.maven.bot.handlers.MessageHandler;

public class UpdateManager extends BaseHandler {

    private BaseHandler messageHandler;
    private BaseHandler callBackQueryHandler;

    public UpdateManager() {
        this.messageHandler = new MessageHandler(userService,messageMaker);
        this.callBackQueryHandler = new CallBackQueryHandler();
    }

    @Override
    public void handle(Update update){
        if(update.message() != null){
            messageHandler.handle(update);
        }else if(update.callbackQuery() != null){
            callBackQueryHandler.handle(update);
        }
    }
}
