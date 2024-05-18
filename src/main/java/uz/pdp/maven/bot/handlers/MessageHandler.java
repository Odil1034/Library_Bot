package uz.pdp.maven.bot.handlers;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.maven.backend.models.book.Book;
import uz.pdp.maven.backend.models.myUser.MyUser;
import uz.pdp.maven.backend.service.bookService.BookService;
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
            if (message.contact() == null) {
                handleContactMessage(message.contact());
            }
            String baseState = curUser.getBaseState();
            BaseState curBaseState = BaseState.valueOf(baseState);
            if (Objects.equals(curBaseState, BaseState.MAIN_MENU_STATE)) {
                handleMainMenu(curUser);
            } else if (Objects.equals(curBaseState, BaseState.ADD_BOOK_STATE)) {
                handleAddBook(curUser);
            } else if (Objects.equals(curBaseState, BaseState.SEARCH_BOOK_STATE)) {
                handleSearchBook(curUser);
            } else if (Objects.equals(curBaseState, BaseState.MY_FAVOURITE_BOOKS_STATE)) {
                handleMyFavouriteBook(curUser);
            } else {
                SendMessage sendMessage = new SendMessage(curUser.getId(), "Unexpected option");
                bot.execute(sendMessage);
            }
        }
    }

    private void handleStartCommand(User from) {
        SendMessage welcome = welcomeMessage(from);
        bot.execute(welcome);

        if (Objects.isNull(curUser.getPhoneNumber())
                || curUser.getPhoneNumber().isEmpty()
                || curUser.getPhoneNumber().isBlank()) {
            enterPhoneNumber();

            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        } else {
            handleMainMenu(curUser);

            curUser.setBaseState(BaseState.MAIN_MENU_STATE.name());
            userService.save(curUser);
        }
    }

    private void handleMyFavouriteBook(MyUser curUser) {
        Message message = update.message();

    }

    private void handleAddBook(MyUser curUser) {

        Message message = update.message();
        String text = message.text();
        System.out.println(text);
        /*if (text != null) {
            if(text.equals("Add Book")){

            }
        }else {

        }*/

        /*Book.builder()
                .name()
                .author()
                .genre()
                .description()
                .fileId()
                .photoId()
                .Id()
                .userId(curUser.getId())
                .isComplete(true)
                .build();*/

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

    public void handleMainMenu(MyUser curUser) {
        SendMessage sendMessage = messageMaker.mainMenu(curUser);
        bot.execute(sendMessage);
    }

    private void handleSearchBook(MyUser curUser) {
        if (Objects.equals(this.curUser.getBaseState(), BaseState.MAIN_MENU_STATE.name())) {
            SendMessage sendMessage = messageMaker.searchBookMenu(this.curUser);
            bot.execute(sendMessage);
        }
    }
}