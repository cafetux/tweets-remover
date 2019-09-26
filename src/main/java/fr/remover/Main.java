package fr.remover;

import fr.remover.action.DeleteAction;
import fr.remover.action.TweetAction;
import fr.remover.model.Retweet;
import fr.remover.model.Tweet;
import fr.remover.model.TweeterStatus;
import twitter4j.*;
import twitter4j.api.TweetsResources;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
public class Main {

    private static final Twitter twitter = TwitterFactory.getSingleton();
    private static final boolean REAL_DELETION = true;
    private static DeleteAction deleteAction = new DeleteAction(twitter.tweets());
    private static TweetAction fakeAction = statusId -> System.out.println("delete "+statusId);


    public static void main (String[] args) throws Exception {

        List<Status> all = readAllTweets().stream().sorted(Comparator.comparing(Status::getId)).collect(Collectors.toList());

        System.out.println("retrieve "+all.size()+" tweets");

        List<TweeterStatus> homeTimeLine = classify(all);
        System.out.println("mades "+homeTimeLine.size()+" groups");

        for (TweeterStatus status : homeTimeLine) {
            System.out.println(status.toString());
            if(!status.isImmunised()) {
                if(status.isExpired()) {
                    status.apply(REAL_DELETION?deleteAction:fakeAction);
                }
            }
        }
    }

    /**
     * Permet de regrouper les Threads (que je souhaites supprimmer d'un bloc) et mettre les RT à par.
     * */
    private static List<TweeterStatus> classify(List<Status> all) {
        List<TweeterStatus> homeTimeLine = new ArrayList<>();
        for (Status status : all) {
                if(status.isRetweet()) {
                    homeTimeLine.add(new Retweet(status.getId(),createdAt(status), status.getText(),status.getRetweetCount(),status.getFavoriteCount()));
                } else {
                    long inReplyToStatusId = status.getInReplyToStatusId();
                    if (isRoot(inReplyToStatusId)) {
                        homeTimeLine.add(tweetFrom(status));
                    } else {
                        Optional<TweeterStatus> previous = homeTimeLine.stream().filter(t -> t.matchWith(inReplyToStatusId)).findFirst();
                        if(previous.isPresent()) {
                            TweeterStatus previousStatus = previous.get();
                            homeTimeLine.remove(previousStatus);
                            homeTimeLine.add(previousStatus.add(tweetFrom(status)));
                        } else {
                            homeTimeLine.add(tweetFrom(status));
                        }
                    }
                }
            }
        return homeTimeLine;
    }

    private static boolean isRoot(long inReplyToStatusId) {
        return inReplyToStatusId == -1;
    }

    private static List<Status> readAllTweets() throws TwitterException {
        List<Status> all = new ArrayList<>();
        int page = 1;
        ResponseList<Status> htl = twitter.timelines().getUserTimeline(new Paging(page));
        do {
            System.out.println("retrieve page "+page);
            all.addAll(htl);
            page +=1;
            htl = twitter.timelines().getUserTimeline(new Paging(page));
        } while (htl.size()>0);
        return all;
    }

    private static Tweet tweetFrom(Status status) {
        return new Tweet(status.getId(), createdAt(status), status.getText(), status.getRetweetCount(), status.getFavoriteCount());
    }

    private static LocalDate createdAt(Status status) {
        ZonedDateTime instant = status.getCreatedAt().toInstant().atZone(ZoneId.systemDefault());
        return LocalDate.from(instant);
    }
}
