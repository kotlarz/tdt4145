package treningsdagbok.interfaces;

import java.sql.SQLException;

public interface DataTable {
    public void create() throws SQLException, IllegalAccessException;
    public void delete() throws SQLException, IllegalAccessException;
}
