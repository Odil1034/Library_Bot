package uz.pdp.maven.bot.states.child.addBookState;

import lombok.Getter;
import uz.pdp.maven.bot.states.State;

@Getter
public enum AddBookState implements State {

    BOOK_NAME(null),

    SELECT_GENRE(BOOK_NAME),
    ENTER_AUTHOR(SELECT_GENRE),
    ENTER_PHOTO_OF_BOOK(ENTER_AUTHOR),
    ENTER_DESCRIPTION(ENTER_PHOTO_OF_BOOK),
    UPLOAD_FILE(ENTER_DESCRIPTION);

    public final AddBookState prevState;

    AddBookState(AddBookState prevState1) {
        this.prevState = prevState1;
    }


}
