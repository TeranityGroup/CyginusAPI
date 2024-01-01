package net.teranity.api;

import lombok.Getter;
import lombok.Setter;
import net.teranity.api.managers.RanksManager;
import net.teranity.api.managers.UsersManager;
import net.teranity.api.user.User;
import net.teranity.lib.OrionTable;

import java.sql.Connection;
import java.util.UUID;

public class Cyginus {
    @Getter @Setter private static Cyginus instance;
    @Getter @Setter private Connection connection;

    @Getter private UsersManager usersManager;
    @Getter private RanksManager ranksManager;

    @Getter private OrionTable userTable;
    @Getter private OrionTable rewardTable;
    @Getter private OrionTable rankTable;
    @Getter private OrionTable rankPermsTable;
    @Getter private OrionTable giftsTable;

    // CyginusAPI
    public Cyginus() {
        setInstance(this);

        usersManager = new UsersManager();
        ranksManager = new RanksManager();

        if (connection == null) return;

        userTable = new OrionTable("users", connection);
        rewardTable = new OrionTable("rewards", connection);
        rankTable = new OrionTable("ranks", connection);
        rankPermsTable = new OrionTable("rankperms", connection);
        giftsTable = new OrionTable("gifts", connection);
    }

    public User getUser(UUID uuid) {
        return usersManager.getUser(uuid);
    }

    public User getUser(String minecraftName) {
        return usersManager.getUser(minecraftName);
    }
}
