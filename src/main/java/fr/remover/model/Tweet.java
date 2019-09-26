package fr.remover.model;

import fr.remover.action.TweetAction;

import java.time.LocalDate;

public class Tweet implements TweeterStatus {

    private long id;
    private String text;
    protected LocalDate createdAt;
    private long nbRT;
    private long nbFav;

    public Tweet(long id, LocalDate createdAt, String text, long nbRT, long nbFav) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.nbRT = nbRT;
        this.nbFav = nbFav;
    }

    @Override
    public boolean isReTweet() {
        return false;
    }

    @Override
    public boolean matchWith(long inReplyToStatusId) {
        return this.id == inReplyToStatusId;
    }

    @Override
    public TweeterStatus add(Tweet next) {
        Thread thread = new Thread(this);
        thread.add(next);
        return thread;
    }

    @Override
    public void apply(TweetAction action) {
        action.apply(id);
    }

    @Override
    public boolean isImmunised() {
        return engagement() >= 30;
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate());
    }

    protected LocalDate expirationDate() {
        return this.createdAt.plusDays(6).plusDays(2*engagement());
    }

    public long engagement() {
        return this.nbRT + this.nbFav;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", nbRT=" + nbRT +
                ", nbFav=" + nbFav +
                '}';
    }

    long id() {
        return id;
    }
}
