package net.teranity.api.user;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.util.UserChecker;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;
import net.teranity.lib.table.records.RecordSetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User implements IUser{
    private UUID uuid;
    private String minecraftName;

    private String nickname;
    private String previousNickname;
    private int coins;

    private UserReward userReward;
    private NetworkLevel networkLevel;
    private RankCollection rankCollection;
    private UserStatus userStatus;

    public User(@NonNull UUID uuid, @NonNull String minecraftName) {
        if (UserChecker.check(uuid)) {
            this.uuid = uuid;
            this.minecraftName = minecraftName;

            loadEverything();

            userReward = new UserReward(getUuid());
            networkLevel = new NetworkLevel(getUuid());
            rankCollection = new RankCollection(getUuid());
            userStatus = new UserStatus(getUuid());
        } return;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getMinecraftName() {
        return minecraftName;
    }

    @Override
    public boolean exists() {
        return UserChecker.check(getUuid());
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override @SneakyThrows(RecordException.class)
    public void setNickname(String nickname) {
        RecordSetter recordSetter = RecordSetter.build(Cyginus.getInstance().getUserTable());

        recordSetter.setSelect("nick", (String) nickname);
        recordSetter.setParent("Uuid", (String) getUuid().toString());
        recordSetter.setup();

        this.nickname = nickname;
        previousNickname = (String) recordSetter.getPreviousObject();
    }

    @Override
    public INetworkLevel getNetworkLevel() {
        return networkLevel;
    }

    @Override
    public IUserReward getUserReward() {
        return userReward;
    }

    @Override
    public IRankCollection getRankCollection() {
        return rankCollection;
    }

    @Override
    public IUserStatus getUserStatus() {
        return userStatus;
    }

    @Override
    public boolean hasRank() {
        if (getRankCollection().getCurrentRank().getRankPriority() > 0) {
            return true;
        } return false;
    }

    @Override
    public boolean isStaff() {
        if (getRankCollection().getCurrentRank().getRankPriority() > 89) {
            return true;
        } return false;
    }

    @Override
    public boolean isDeveloper() {
        if (getRankCollection().getCurrentRank().getRankPriority() > 98) {
            return true;
        } return false;
    }

    @Override
    public boolean isAdmin() {
        if (getRankCollection().getCurrentRank().getRankPriority() > 99) {
            return true;
        } return false;
    }

    @Override
    public boolean isOwner() {
        if (getRankCollection().getCurrentRank().getRankPriority() == 100) {
            return true;
        } return false;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override @SneakyThrows(RecordException.class)
    public void setCoins(int coins) {
        RecordSetter recordSetter = RecordSetter.build(Cyginus.getInstance().getUserTable());

        recordSetter.setSelect("coins", (Integer) coins);
        recordSetter.setParent("uuid", (String) getUuid().toString());
        recordSetter.setup();

        this.coins = coins;
    }

    @SneakyThrows(RecordException.class)
    private void loadEverything() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                nickname = resultSet.getString("nick");
                coins = resultSet.getInt("coins");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
