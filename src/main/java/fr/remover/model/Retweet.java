package fr.remover.model;

import java.time.LocalDate;

public class Retweet extends Tweet{


    public Retweet(long id, LocalDate createdAt, String text, long nbRT, long nbFav) {
        super(id, createdAt, text, nbRT, nbFav);
    }

    @Override
    public boolean isReTweet() {
        return true;
    }

    @Override
    public String toString() {
        return "ReTweet{"+ super.toString() +'}';
    }

    @Override
    protected LocalDate expirationDate() {
        return this.createdAt.plusMonths(1);
    }

    @Override
    public boolean isImmunised() {
        return false;
    }

}
