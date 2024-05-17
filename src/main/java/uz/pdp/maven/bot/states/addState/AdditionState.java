package uz.pdp.maven.bot.states.addState;

import uz.pdp.maven.bot.states.State;

public enum AdditionState implements State {

    ADD_BOOK,
    BOOK_NAME,
    SELECT_CATEGORY,
    ENTER_AUTHOR,
    ENTER_COVER_PICTURE_OF_BOOK,
    SELECT_FILE_FORMAT,
    UPLOAD_FILE;

}
