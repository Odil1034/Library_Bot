package uz.pdp.maven.bot.states.child;

import uz.pdp.maven.bot.states.State;

public enum RegistrationState implements State {
    REGISTER,
    SEND_PHONE_NUMBER,
    NOT_REGISTERED
}
