package fr.remover.model;

import fr.remover.action.TweetAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Thread implements TweeterStatus {

    private List<Tweet> tweets = new ArrayList<>();

    public Thread(Tweet root) {
        tweets.add(root);
    }

    @Override
    public TweeterStatus add(Tweet next) {
        this.tweets.add(next);
        return this;
    }

    @Override
    public void apply(TweetAction action) {
        for (Tweet tweet : tweets) {
            action.apply(tweet.id());
        }
    }

    @Override
    public boolean isImmunised() {
        return engagement() >= 30;
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate());
    }

    @Override
    public boolean isReTweet() {
        return false;
    }


    private long engagement() {
        return tweets.stream().mapToLong(Tweet::engagement).sum();
    }


    @Override
    public boolean matchWith(long inReplyToStatusId) {
        return tweets.stream().anyMatch(t->t.matchWith(inReplyToStatusId));
    }

    private LocalDate expirationDate() {
        return this.tweets.get(0).createdAt.plusDays(6).plusDays(2*engagement());
    }


    @Override
    public String toString() {
        return "Thread{" +
                "tweets=" + tweets.stream().map(Tweet::toString).collect(Collectors.joining(",")) +
                '}';
    }
}
