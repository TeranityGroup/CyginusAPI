package net.teranity.api.user;

import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.api.util.UserChecker;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;
import net.teranity.lib.table.records.RecordSetter;

import java.net.SocketAddress;
import java.util.UUID;

public class UserStatus implements IUserStatus {
    private UUID uuid;

    public UserStatus(UUID uuid) {
        if (UserChecker.check(uuid)) {
            this.uuid = uuid;
        }
    }

    @Override @SneakyThrows(RecordException.class)
    public boolean isOnline() {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("online");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        if (!recordGetter.next()) return false;

        return ((Boolean) recordGetter.get() != false);
    }

    @SneakyThrows(RecordException.class)
    public void setOnline(boolean online) {
        RecordSetter recordSetter = RecordSetter.build(Cyginus.getInstance().getUserTable());\

        recordSetter.setSelect("online", (Boolean) online);
        recordSetter.setParent("uuid", (String) uuid.toString());
        recordSetter.setup();
    }

    // currentServer is not on the line right now.
    @Override
    public SocketAddress getCurrentServer() {
        return null;
    }

    public void setCurrentServer(SocketAddress socketAddress) {

    }

    @Override
    public String getMode() {
        return null;
    }

    public void setMode() {

    }

    @Override
    public String getMap() {
        return null;
    }

    public void setMap() {

    }
}
