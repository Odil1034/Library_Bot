package uz.pdp.maven;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;

public class SendFile {
    public static void main(String[] args) {
        String botToken = "5979503741:AAFVrAwDnjTovy4bfUVftJsea6nuFSZ9FMs";

        TelegramBot bot = new TelegramBot(botToken);



        SendDocument sendDocument = new SendDocument(1712225965,"BQACAgIAAxkBAAMrZke7_WnFpYShw0WZ9Lfk7zgLd9gAAmFPAAJoiEFKcrCOjP8wYSY1BA");
        bot.execute(sendDocument);

    }
}
