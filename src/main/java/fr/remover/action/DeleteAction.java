package fr.remover.action;

import twitter4j.TwitterException;
import twitter4j.api.TweetsResources;

public class DeleteAction implements TweetAction{

    private final TweetsResources tweetsResources;

    public DeleteAction(TweetsResources tweetsResources) {
        this.tweetsResources = tweetsResources;
    }


    @Override
    public void apply(long statusId) {
        try {
            System.out.println("Try to delete "+statusId);
            tweetsResources.destroyStatus(statusId);
        } catch (TwitterException e) {
            System.out.println("Exception when try to delete "+statusId+ ":"+e.getMessage());
        }
    }
}
