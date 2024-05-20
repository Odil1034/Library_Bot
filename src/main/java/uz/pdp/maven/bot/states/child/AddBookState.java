package uz.pdp.maven.bot.states.child;


import lombok.Getter;
import uz.pdp.maven.bot.states.State;


@Getter
public enum AddBookState implements State {

    ENTER_BOOK_NAME,
    ENTER_BOOK_AUTHOR,
    SELECT_GENRE,
    ENTER_BOOK_DESCRIPTION,
    ENTER_BOOK_FILE,
    ENTER_BOOK_PHOTO;

    private AddBookState prevState;

    AddBookState(AddBookState prevState) {
        this.prevState = prevState;
    }

    AddBookState() {
        this.prevState = null;
    }

}
