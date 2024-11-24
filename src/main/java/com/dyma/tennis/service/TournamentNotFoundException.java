package com.dyma.tennis.service;

public class TournamentNotFoundException extends RuntimeException {
    public TournamentNotFoundException(String identifier) {
        super("Tournament with identifier " + identifier + " could not be found.");
    }
}
