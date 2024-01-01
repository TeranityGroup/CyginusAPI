package net.teranity.api.user;

import java.net.SocketAddress;

public interface IUserStatus {

    boolean isOnline();

    SocketAddress getCurrentServer();

    String getMode();
    String getMap();
}
