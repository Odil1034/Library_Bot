package uz.pdp.maven.bot.states.child;

import uz.pdp.maven.bot.states.State;

public enum SearchBookState implements State {

    SEARCH_BY,
    BOOK_LIST,
    SELECT_FILE,
    DOWNLOAD,
    ADD_MY_FAVOURITE_BOOKS;

    private AddBookState prevState;
    SearchBookState(AddBookState prevState) {
        this.prevState = prevState;
    }
    SearchBookState() {
        this.prevState = null;
    }

}
