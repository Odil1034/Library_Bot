package uz.pdp.maven.bot.states.child.mainMenuState;

import uz.pdp.maven.bot.states.State;

public enum MainMenuState implements State {


    MAIN_MENU,
    ADD_BOOK,
    SEARCH_BOOK,
    MY_FAVOURITE_BOOKS;

    private MainMenuState prevState;

    MainMenuState(MainMenuState prevState) {
        this.prevState = prevState;
    }

    MainMenuState() {
        this.prevState = null;
    }
}
