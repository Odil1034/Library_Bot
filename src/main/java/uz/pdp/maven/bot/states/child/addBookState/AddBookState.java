package uz.pdp.maven.bot.states.child.addBookState;

import uz.pdp.maven.bot.states.State;

public enum AddBookState implements State {

    BOOK_NAME,
    SELECT_GENRE,
    ENTER_AUTHOR,
    ENTER_PHOTO_OF_BOOK,
    ENTER_DESCRIPTION,
    UPLOAD_FILE;

}
