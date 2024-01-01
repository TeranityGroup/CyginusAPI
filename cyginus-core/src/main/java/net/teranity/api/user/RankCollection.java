package net.teranity.api.user;

import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.rank.Rank;
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

public class RankCollection implements IRankCollection {
    private UUID uuid;

    private Rank rank;
    private Collection<Rank> giftRanks;

    public RankCollection(UUID uuid) {
        if (UserChecker.check(uuid)) {
            this.uuid = uuid;

            giftRanks = new ArrayList<>();

            loadRank();
            loadGifts();
        }
    }

    @Override
    public Rank getCurrentRank() {
        return rank;
    }

    @Override @SneakyThrows(RecordException.class)
    public void setCurrentRank(Rank rank) {
        RecordSetter recordSetter = RecordSetter.build(Cyginus.getInstance().getUserTable());

        recordSetter.setSelect("rank", (Integer) rank.getRankPriority());
        recordSetter.setSelect("uuid", (String) uuid.toString());
        recordSetter.setup();
        this.rank = rank;
    }

    @Override
    public Collection<Rank> getGiftRanks() {
        return giftRanks;
    }

    @Override @SneakyThrows(RecordException.class)
    public void addGiftRank(Rank rank) {
        if (getGiftRanks().contains(rank)) return;

        RecordInserter recordInserter = RecordInserter.build(Cyginus.getInstance().getGiftsTable());

        recordInserter.addRecord("rank", "Uuid");
        recordInserter.addObject((Integer) rank.getRankPriority(), (String) uuid.toString());
        recordInserter.setup();

        getGiftRanks().add(rank);
    }

    @Override @SneakyThrows(RecordException.class)
    public void removeGiftRank(Rank rank) {
        if (!getGiftRanks().contains(rank)) return;

        RecordRemover recordRemover = RecordRemover.build(Cyginus.getInstance().getGiftsTable());

        recordRemover.setParent("uuid", (String) uuid.toString());
        recordRemover.setSecondParent("rank", (Integer) rank.getRankPriority());
        recordRemover.setup();

        getGiftRanks().remove(rank);
    }

    @Override
    public Rank getGiftRank(int rankPriority) {
        for (Rank rank : getGiftRanks()) {
            if (rank.getRankPriority() == rankPriority) {
                return rank;
            }
        } return null;
    }

    @SneakyThrows(RecordException.class)
    private void loadRank() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("rank");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        this.rank = Cyginus.getInstance().getRanksManager().getRank((Integer) recordGetter.get());
    }

    @SneakyThrows(RecordException.class)
    private void loadGifts() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getGiftsTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                int rankPriority = resultSet.getInt("rank");

                Rank rank = Cyginus.getInstance().getRanksManager().getRank(rankPriority);
                if (rank == null) return;

                getGiftRanks().add(rank);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
