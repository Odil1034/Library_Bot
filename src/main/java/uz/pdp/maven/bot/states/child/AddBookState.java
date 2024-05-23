package uz.pdp.maven.bot.states.child;


import lombok.Getter;
import uz.pdp.maven.bot.states.State;


@Getter
public enum AddBookState implements State {

    ENTER_BOOK_NAME(null),
    ENTER_BOOK_AUTHOR(ENTER_BOOK_NAME),
    ENTER_BOOK_PAGE(ENTER_BOOK_AUTHOR),
    ENTER_BOOK_LANGUAGE(ENTER_BOOK_PAGE),
    SELECT_BOOK_GENRE(ENTER_BOOK_LANGUAGE),
    ENTER_BOOK_DESCRIPTION(SELECT_BOOK_GENRE),
    ENTER_BOOK_FILE_ID(ENTER_BOOK_DESCRIPTION),
    ENTER_BOOK_PHOTO_ID(ENTER_BOOK_FILE_ID);

    private AddBookState prevState;

    AddBookState(AddBookState prevState) {
        this.prevState = prevState;
    }

    AddBookState() {
        this.prevState = null;
    }

}
