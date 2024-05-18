package uz.pdp.maven.forExample;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendPhoto;

public class SendPhotoo {
    public static void main(String[] args) {
    String token = "5979503741:AAFVrAwDnjTovy4bfUVftJsea6nuFSZ9FMs";
    TelegramBot bot = new TelegramBot(token);

        SendPhoto sendPhoto = new SendPhoto(1712225965L, "AgACAgIAAxkBAAMkZke0sGukua_lBMM-mP3WPYvKbAYAAm7hMRtoiEFK3mpSv9QoWv4BAAMCAAN4AAM1BA");
        SendPhoto sendPhoto1 = new SendPhoto(1712225965L, "AgACAgIAAxkBAAMgZkexbodrT9iH6KragII6ycvO5XMAAjHhMRtoiEFKZoGwVkE8PkUBAAMCAANtAAM1BA");
        bot.execute(sendPhoto);
        bot.execute(sendPhoto1);

    }
}
