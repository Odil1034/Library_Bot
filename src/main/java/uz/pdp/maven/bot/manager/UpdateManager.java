package uz.pdp.maven.bot.manager;

import com.pengrad.telegrambot.model.Update;
import uz.pdp.maven.bot.handlers.BaseHandler;
import uz.pdp.maven.bot.handlers.CallBackQueryHandler;
import uz.pdp.maven.bot.handlers.MessageHandler;

public class UpdateManager {

    private BaseHandler messageHandler;
    private BaseHandler callBackQueryHandler;

    public UpdateManager() {
        this.messageHandler = new MessageHandler();
        this.callBackQueryHandler = new CallBackQueryHandler();
    }

    public void manage(Update update){
        if(update.message() != null){
            messageHandler.handle(update);
        }else if(update.callbackQuery() != null){
            callBackQueryHandler.handle(update);
        }
    }
}