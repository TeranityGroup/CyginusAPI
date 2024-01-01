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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserReward implements IUserReward {
    private UUID uuid;

    private Collection<Reward> rewards;

    public UserReward(UUID uuid) {
        if (UserChecker.check(uuid)) {
            this.uuid = uuid;

            rewards = new ArrayList<>();
            loadStats();
        }
    }

    @Override
    public Collection<Reward> getRewards() {
        return rewards;
    }

    @Override @SneakyThrows(RecordException.class)
    public void addReward(Reward reward) {
        if (getRewards().contains(reward)) return;

        RecordInserter recordInserter = RecordInserter.build(Cyginus.getInstance().getRewardTable());
        recordInserter.addRecord("name", "type", "max");
        recordInserter.addObject((String) reward.getRewardName(), (Integer) reward.getRewardType().getId(), (Integer) reward.getMax());
        recordInserter.setup();

        getRewards().add(reward);
    }

    @Override @SneakyThrows(RecordException.class)
    public void removeReward(Reward reward) {
        if (!getRewards().contains(reward)) return;

        RecordRemover recordRemover = RecordRemover.build(Cyginus.getInstance().getRewardTable());

        recordRemover.setParent("uuid", (String) uuid.toString());
        recordRemover.setSecondParent("name", (String) reward.getRewardName());
        recordRemover.setup();

        getRewards().add(reward);
    }

    @SneakyThrows(RecordException.class)
    private void loadStats() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getRewardTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                String rewardName = resultSet.getString("name");
                RewardType rewardType = RewardType.getTypeByID(resultSet.getInt("id"));
                int max = resultSet.getInt("max");

                Reward reward = new Reward(rewardName, rewardType, max);
                getRewards().add(reward);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
