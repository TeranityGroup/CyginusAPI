package net.teranity.api.user;

import net.teranity.api.rank.Rank;

import java.util.Collection;

public interface IRankCollection {

    Rank getCurrentRank();
    void setCurrentRank(Rank rank);

    Collection<Rank> getGiftRanks();

    void addGiftRank(Rank rank);
    void removeGiftRank(Rank rank);

    Rank getGiftRank(int rankPriority);
}
