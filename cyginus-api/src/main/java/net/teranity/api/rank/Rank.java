package net.teranity.api.rank;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class Rank {
    @NonNull private String rankName, rankColor;
    @NonNull private int rankPriority;
    @NonNull private Collection<RankPermission> rankPermissions;

    public String getPrefix() {
        return rankColor + rankName;
    }
}
