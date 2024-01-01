package net.teranity.api.user;

import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.reward.Reward;
import net.teranity.api.reward.RewardType;
import net.teranity.api.util.UserChecker;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;
import net.teranity.lib.table.records.RecordInserter;
import net.teranity.lib.table.records.RecordRemover;
import net.teranity.lib.table.records.RecordSetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class NetworkLevel implements INetworkLevel{
    private UUID uuid;

    private int level, exp, exps;

    private Collection<Reward> rewards;

    public NetworkLevel(UUID uuid) {
        if (UserChecker.check(uuid)) {
            this.uuid = uuid;

            loadStats();

            rewards = new ArrayList<>();
            for (Reward reward : Cyginus.getInstance().getUser(uuid).getUserReward().getRewards()) {
                if (reward.getRewardType() == RewardType.LEVEL) {
                    rewards.add(reward);
                }
            }
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        set("level", (Integer) level);
        this.level = level;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        set("exp", (Integer) exp);
        this.exp = exp;
    }

    @Override
    public int getRequiredExps() {
        return exps;
    }

    @Override
    public void setExps(int exps) {
        set("exps", (Integer) exps);
        this.exps = exps;
    }

    @Override
    public void updateStats() {
        setLevel(getLevel() + 1);
        setExp(0);
        setExps(getRequiredExps() + 350);
    }

    @Override
    public void resetStats() {
        setLevel(1);
        setExp(0);
        setExps(400);
    }

    @Override
    public Collection<Reward> getRewards() {
        return rewards;
    }

    @Override @SneakyThrows(RecordException.class)
    public void addReward(Reward reward) {
        if (getRewards().contains(reward)) return;

        Cyginus.getInstance().getUser(uuid).getUserReward().addReward(reward);
    }

    @Override @SneakyThrows(RecordException.class)
    public void removeReward(Reward reward) {
        if (!getRewards().contains(reward)) return;

        Cyginus.getInstance().getUser(uuid).getUserReward().removeReward(reward);
        getRewards().remove(reward);
    }

    @SneakyThrows(RecordException.class)
    private void loadStats() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                level = resultSet.getInt("level");
                exp = resultSet.getInt("exp");
                exps = resultSet.getInt("exps");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows(RecordException.class)
    private void set(String record, Object object) {
        RecordSetter recordSetter = RecordSetter.build(Cyginus.getInstance().getUserTable());

        recordSetter.setSelect(record, object);
        recordSetter.setParent("uuid", (String) uuid.toString());
        recordSetter.setup();
    }
}
