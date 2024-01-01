package net.teranity.api.user;

import java.net.SocketAddress;
import java.util.UUID;

public interface IUser {

    UUID getUuid();

    String getMinecraftName();

    boolean exists();

    String getNickname();
    void setNickname(String nickname);

    INetworkLevel getNetworkLevel();
    IUserReward getUserReward();
    IRankCollection getRankCollection();
    IUserStatus getUserStatus();

    boolean hasRank();
    boolean isStaff();
    boolean isDeveloper();
    boolean isAdmin();
    boolean isOwner();

    int getCoins();
    void setCoins(int coins);
}
