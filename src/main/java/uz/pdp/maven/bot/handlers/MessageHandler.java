package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.service.userService.UserService;
import uz.pdp.maven.bot.maker.MessageMaker;
import uz.pdp.maven.bot.states.base.BaseState;
import uz.pdp.maven.bot.states.child.mainMenuState.MainMenuState;
import uz.pdp.maven.bot.states.child.registrationState.RegistrationState;

import java.util.Objects;

public class MessageHandler extends BaseHandler {

    private final UserService userService;
    private final MessageMaker messageMaker;

    public MessageHandler(UserService userService, MessageMaker messageMaker) {
        this.userService = userService;
        this.messageMaker = messageMaker;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        User from = message.from();
        super.update = update;
        super.curUser = getUserOrCreate(from);
        String text = message.text();

        if (text != null && text.equals("/start")) {
            handleStartCommand(from);
        } else if (message.contact() != null) {
            handleContactMessage(message.contact());
        } else if (text != null && text.equals("Search Book")) {
            handleSearchBook();
        } else if (text != null) {
            handleSearchBookOptions(text);
        } else {
            sendMainMenu();
        }
    }

    private void handleStartCommand(User from) {
        String welcomeMessage = "Assalomu Alaykum kutubxona botimizga xush kelibsiz 😊😊😊";
        SendMessage welcome = new SendMessage(from.id(), welcomeMessage);
        bot.execute(welcome);

        if (Objects.isNull(curUser.getPhoneNumber())
                || curUser.getPhoneNumber().isEmpty()
                || curUser.getPhoneNumber().isBlank()) {
            curUser.setBaseState(BaseState.REGISTRATION_STATE.name());
            curUser.setState(RegistrationState.REGISTER.name());
            userService.save(curUser);
            enterPhoneNumber();
        } else {
            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
            sendMainMenu();
        }
    }

    private void handleContactMessage(Contact contact) {
        String phoneNumber = contact.phoneNumber();
        curUser.setPhoneNumber(phoneNumber);
        curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
        userService.save(curUser);
        sendMainMenu();
    }

    private void enterPhoneNumber() {
        SendMessage sendMessage = messageMaker.enterPhoneNumber(curUser);
        bot.execute(sendMessage);
    }

    public void sendMainMenu() {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleSearchBook() {
        if (Objects.equals(curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())) {
            SendMessage sendMessage = messageMaker.searchBookMenu(curUser);
            bot.execute(sendMessage);
        }
    }

    private void handleSearchBookOptions(String text) {
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
                    bot.execute(messageMaker.searchBookMenu(curUser));
                    return;
            }

            bot.execute(responseMessage);
        }
    }
}