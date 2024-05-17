package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.maker.MessageMaker;
import uz.pdp.maven.bot.states.BaseState;
import uz.pdp.maven.bot.states.mainState.MainState;
import uz.pdp.maven.bot.states.registerState.RegisterState;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();

        if (text!=null && text.equals("/start")){
            if (Objects.equals(curUser.getBaseState(), RegisterState.REGISTER_STATE.name())) {
                String salomlashuvStr = "Assalomu Alaykum kutubxona botimizga xush kelibsiz 😊😊😊";
                SendMessage salomlashuv = new SendMessage(from.id(), salomlashuvStr);
                bot.execute(salomlashuv);

                if(Objects.isNull(curUser.getPhoneNumber()) ||
                        curUser.getPhoneNumber().isEmpty() ||
                        curUser.getPhoneNumber().isBlank()){
                    enterPhoneNumber();
                }
                String enterPhoneNumberRequest = "Registratsiyadan o'tish uchun telefon raqamingizni kiritish";
                SendMessage registerRequest = new SendMessage(from.id(), enterPhoneNumberRequest);
            }
        }else {
            String baseStateStr = curUser.getBaseState();
            String state = curUser.getState();

        }


    }

    private void enterPhoneNumber() {

        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);

        bot.execute(sendMessage);
    }
}
