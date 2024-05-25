package uz.pdp.maven.bot.states.child;

import lombok.Getter;
import uz.pdp.maven.bot.states.State;

@Getter
public enum AddBookState implements State {
    ENTER_BOOK_NAME,
    ENTER_BOOK_AUTHOR,
    ENTER_BOOK_PAGE,
    ENTER_BOOK_LANGUAGE,
    SELECT_BOOK_GENRE,
    ENTER_BOOK_DESCRIPTION,
    ENTER_BOOK_FILE_ID,
    ENTER_BOOK_PHOTO_ID
}
