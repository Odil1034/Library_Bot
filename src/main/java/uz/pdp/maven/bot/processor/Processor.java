package uz.pdp.maven.bot.processor;

import com.pengrad.telegrambot.model.Update;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.State;

public abstract class Processor {

    public abstract void processor(Update update, MyUser myUser);

}
