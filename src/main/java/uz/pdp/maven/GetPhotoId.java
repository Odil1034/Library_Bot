package uz.pdp.maven;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.List;

public class GetPhotoId {
    public static void main(String[] args) {
        String botToken = "5979503741:AAFVrAwDnjTovy4bfUVftJsea6nuFSZ9FMs";

        TelegramBot bot = new TelegramBot(botToken);

            GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates());
            List<Update> updates = updatesResponse.updates();

            for (Update update : updates) {
                if (update.message() != null) {
                    Message message = update.message();

                    if (message.photo() != null) {
                        PhotoSize[] photos = message.photo();
                        for (PhotoSize photoSize : photos) {
                            System.out.println("Photo file ID: " + photoSize.fileId());
                        }
                    }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
