package net.teranity.api.user;

import net.teranity.api.reward.Reward;

import java.util.Collection;

public interface IUserReward {

    Collection<Reward> getRewards();

    void addReward(Reward reward);
    void removeReward(Reward reward);
}
