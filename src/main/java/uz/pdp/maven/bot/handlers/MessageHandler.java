package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.bot.states.BaseState;
import uz.pdp.maven.bot.states.mainState.MainState;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();

        MyUser curUser = getUserOrCreate(from);

        String baseStateStr = curUser.getBaseState();
        if(baseStateStr == null){

        }else {
            BaseState baseState = BaseState.valueOf(baseStateStr);
            if (Objects.equals(baseState, MainState.MAIN_STATE)) {


            }
        }



        /*SendMessage phoneNumber = new SendMessage(from.id(), "Telefon raqamini kiriting: ");
        InlineKeyboardButton[][] phoneNumStr = new InlineKeyboardButton[0][0];
        phoneNumStr[0][0] = new InlineKeyboardButton("Telefon raqamini jo'natish").callbackData("phoneNum");*/


        /*SendMessage sendMessage = new SendMessage(from.id(), "Sonlardan birini tanla:");

        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[2][3];
        buttons[0][0] = new InlineKeyboardButton("Bir").callbackData("1");
        buttons[0][1] = new InlineKeyboardButton("Ikki").callbackData("2");
        buttons[0][2] = new InlineKeyboardButton("Uch").callbackData("3");
        buttons[1][0] = new InlineKeyboardButton("To'rt").callbackData("4");
        buttons[1][1] = new InlineKeyboardButton("Besh").callbackData("5");
        buttons[1][2] =new InlineKeyboardButton("Olti").callbackData("6");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);
        sendMessage.replyMarkup(keyboardMarkup);

        bot.execute(sendMessage);*/
    }
}
