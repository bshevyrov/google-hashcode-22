package ua.com.home;

import java.util.Comparator;
import java.util.Set;

public class Client implements Comparable<Client>{

    private Set<String> likes;
    private double likesSize;
    private Set<String> dislikes;
    private double dislikesSize;
    private double efficient;
    private double opinionSize;

    public double getOpinionSize() {
        return opinionSize;
    }

    public void setOpinionSize(double opinionSize) {
        this.opinionSize = opinionSize;
    }

    public double getLikesSize() {
        return likesSize;
    }

    public void setLikesSize(double likesSize) {
        this.likesSize = likesSize;
    }

    public double getDislikesSize() {
        return dislikesSize;
    }

    public void setDislikesSize(double dislikesSize) {
        this.dislikesSize = dislikesSize;
    }

    public double getEfficient() {
        return efficient;
    }

    public void setEfficient(double efficient) {
        this.efficient = efficient;
    }

    public Client() {
    }

    public Set<String> getLikes() {
        return likes;
    }

    public void setLikes(Set<String> likes) {
        this.likes = likes;
    }

    public Set<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<String> dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public int compareTo(Client o) {

        return Comparators.OP_AND_EFF.compare(this,o);
    }

    public static class Comparators {
        public static final Comparator<Client> OP_SIZE = Comparator.comparingDouble(Client::getOpinionSize);
        public static final Comparator<Client> EFFICIENT = Comparator.comparingDouble(Client::getEfficient);
        public static final Comparator<Client> OP_AND_EFF = (Client o1, Client o2) -> EFFICIENT.thenComparing(OP_SIZE).compare(o1, o2);
    }

    @Override
    public String toString() {
        return "Client{" +
                "L:" + likesSize +
                " D:" + dislikesSize +
                " E:" + efficient +
                " O:" + opinionSize +
                '}'+"\n";
    }
}
