package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.bot.states.base.BaseState;

import java.util.Objects;

import static uz.pdp.maven.bot.maker.MessageMaker.welcomeMessage;

public class MessageHandler extends BaseHandler {

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();

        if (text != null) {
            if (Objects.equals(text, "/start")) {
                handleStartCommand(from);
            }
        } else {
            String baseState = curUser.getBaseState();
            BaseState curBaseState = BaseState.valueOf(baseState);
            if (message.contact() != null) {
                handleContactMessage(message.contact());
            } else if (text.equals("Search Book")) {
                handleSearchBook();
            } else if (text.equals("Add Book")) {
                handleAddBook();
            } else if (Objects.equals(text, "My favourite Books")){

            }
            else {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Unexpected option");
                bot.execute(sendMessage);
            }
        }
    }

    private void handleAddBook() {

    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (Objects.isNull(curUser.getPhoneNumber())
                || curUser.getPhoneNumber().isEmpty()
                || curUser.getPhoneNumber().isBlank()) {
            enterPhoneNumber();
//            curUser.setPhoneNumber();
            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        } else {
            mainMenu();
            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        }
    }

    private void handleContactMessage(Contact contact) {
        String phoneNumber = contact.phoneNumber();
        curUser.setPhoneNumber(phoneNumber);
        curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
        userService.save(curUser);
        mainMenu();
    }

    private void enterPhoneNumber() {
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }

    public void mainMenu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleSearchBook() {
        if (Objects.equals(curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())) {
            SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
            bot.execute(sendMessage);
        }
    }

    /*private void handleSearchBookOptions(String text) {
        if (Objects.equals(curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())) {
            SendMessage responseMessage;

            switch (text) {
                case "By Author":
                    responseMessage = new SendMessage(curUser.getId(), "You choose to search by Author.");
                    break;
                case "By Name":
                    responseMessage = new SendMessage(curUser.getId(), "You choose to search by Name.");
                    break;
                case "By Genre":
                    responseMessage = new SendMessage(curUser.getId(), "You choose to search by Genre.");
                    break;
                case "Skip":
                    responseMessage = new SendMessage(curUser.getId(), "You choose to Skip.");
                    break;
                default:
                    responseMessage = new SendMessage(curUser.getId(), "Invalid option. Please choose again.");
            }
            bot.execute(messageMaker.searchBookMenu(curUser));
            bot.execute(responseMessage);
        }
    }*/
}