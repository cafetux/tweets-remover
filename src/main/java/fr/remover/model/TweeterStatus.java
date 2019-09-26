package fr.remover.model;

import fr.remover.action.TweetAction;

public interface TweeterStatus {


    boolean isReTweet();

    boolean matchWith(long inReplyToStatusId);

    TweeterStatus add(Tweet next);

    void apply(TweetAction action);

    boolean isImmunised();

    boolean isExpired();

}
