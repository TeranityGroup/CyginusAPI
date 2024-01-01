package net.teranity.api.managers;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.reward.Reward;
import net.teranity.api.reward.RewardType;
import net.teranity.api.user.User;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;
import net.teranity.lib.table.records.RecordInserter;
import net.teranity.lib.table.records.RecordRemover;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UsersManager {
    @Getter @Setter private static UsersManager instance;
    @Getter private Collection<User> registeredUsers;

    public UsersManager() {
        setInstance(this);

        registeredUsers = new ArrayList<>();
        loadUsers();
    }

    @SneakyThrows(RecordException.class)
    public User createUser(UUID uuid, String minecraftName) {
        if (getUser(uuid).exists()) return null;

        RecordInserter recordInserter = RecordInserter.build(Cyginus.getInstance().getUserTable());

        recordInserter.addRecord("uuid", "minecraftName");
        recordInserter.addObject((String) uuid.toString(), (String) minecraftName);
        recordInserter.setup();

        User user = new User(uuid, minecraftName);
        user.setNickname(minecraftName);
        user.setCoins(0);

        Reward reward = new Reward("Welcome to the server,", RewardType.ACHIEVEMENTS, 1);
        user.getUserReward().addReward(reward);
        user.getRankCollection().setCurrentRank(Cyginus.getInstance().getRanksManager().getRank(0));
        user.getNetworkLevel().resetStats();

        return user;
    }

    @SneakyThrows(RecordException.class)
    public void removeUser(UUID uuid) {
        if (!getUser(uuid).exists()) return;

        RecordRemover recordRemover = RecordRemover.build(Cyginus.getInstance().getUserTable());

        recordRemover.setParent("uuid", (String) uuid.toString());
        recordRemover.setup();
    }

    @SneakyThrows(RecordException.class)
    public void removeUser(String minecraftName) {
        if (!getUser(minecraftName).exists()) return;

        RecordRemover recordRemover = RecordRemover.build(Cyginus.getInstance().getUserTable());

        recordRemover.setParent("minecraft", (String) minecraftName);
        recordRemover.setup();
    }

    public User getUser(UUID uuid) {
        for (User user : getRegisteredUsers()) {
            if (user.getUuid() == uuid) {
                return user;
            }
        } return null;
    }

    public User getUser(String minecraftName) {
        for (User user : getRegisteredUsers()) {
            if (user.getMinecraftName().contains(minecraftName)) {
                return user;
            }
        } return null;
    }

    public void resetUser(User user) {

    }

    @SneakyThrows(RecordException.class)
    private void loadUsers() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("*");
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                String minecraftName = resultSet.getString("name");

                User user = new User(uuid, minecraftName);
                registeredUsers.add(user);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
