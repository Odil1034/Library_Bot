package uz.pdp.maven.bot.states.child;

import uz.pdp.maven.bot.states.State;

public enum MyFavouriteBooksState implements State {

    BOOK_LIST,
    SELECT_FILE,
    DOWNLOAD;

    private AddBookState prevState;

    MyFavouriteBooksState(AddBookState prevState) {
        this.prevState = prevState;
    }

    MyFavouriteBooksState() {
        this.prevState = null;
    }

}
