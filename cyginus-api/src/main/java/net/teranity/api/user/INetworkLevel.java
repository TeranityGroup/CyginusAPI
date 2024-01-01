package net.teranity.api.user;

import net.teranity.api.reward.Reward;

import java.util.Collection;

public interface INetworkLevel {

    int getLevel();
    void setLevel(int level);

    int getExp();
    void setExp(int exp);

    int getRequiredExps();
    void setExps(int exps);

    void updateStats();
    void resetStats();

    Collection<Reward> getRewards();

    void addReward(Reward reward);
    void removeReward(Reward reward);
}
