package net.teranity.api.util;

import lombok.SneakyThrows;
import net.teranity.api.Cyginus;
import net.teranity.lib.exceptions.RecordException;
import net.teranity.lib.table.records.RecordGetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserChecker {

    @SneakyThrows(RecordException.class)
    public static boolean check(UUID uuid) {
        RecordGetter recordGetter = RecordGetter.build(Cyginus.getInstance().getUserTable());

        recordGetter.setSelect("*");
        recordGetter.setParent("uuid", (String) uuid.toString());
        recordGetter.setup();

        ResultSet resultSet = recordGetter.getResultSet();
        try {
            while (resultSet.next()) {
                return true;
            } return false;
        }catch (SQLException e) {
            e.printStackTrace();
        } return false;
    }
}
