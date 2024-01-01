package net.teranity.api.rank;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.teranity.api.user.IUser;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class RankPermission {
    @NonNull
    private String permissionValue;

    @NonNull
    private boolean adminOnly;
}
