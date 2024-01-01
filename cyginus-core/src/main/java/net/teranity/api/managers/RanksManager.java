package net.teranity.api.managers;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.rank.Rank;
import net.teranity.api.rank.RankPermission;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class RanksManager {
    @Getter @Setter private static RanksManager instance;

    @Getter private Collection<Rank> registeredRanks;

    public RanksManager() {
        setInstance(this);

        registeredRanks = new ArrayList<>();
        loadRanks();
    }

    public void addRank(Rank rank) {

    }

    public void removeRank(Rank rank) {

    }

    public Rank getRank(int rankPriority) {
        for (Rank rank : getRegisteredRanks()) {
            if (rank.getRankPriority() == rankPriority) {
                return rank;
            }
        } return null;
    }

    @SneakyThrows(RecordException.class)
    private void loadRanks(){
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getRankTable());

        recordGetter.setSelect("*");
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                String rankName = resultSet.getString("name");
                String rankColor = resultSet.getString("color");
                int rankPriority = resultSet.getInt("priority");
                Collection<RankPermission> rankPermissions = loadPermissions(rankPriority);

                Rank rank = new Rank(rankName, rankColor, rankPriority, rankPermissions);
                registeredRanks.add(rank);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows(RecordException.class)
    private Collection<RankPermission> loadPermissions(int rank) {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getRankPermsTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("rank", (Integer) rank);
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                String value = resultSet.getString("perm");
                boolean adminOnly = resultSet.getBoolean("admin");
                Collection<RankPermission> rankPermissions = new ArrayList<>();

                RankPermission rankPermission = new RankPermission(value, adminOnly);
                return rankPermissions;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }
}
