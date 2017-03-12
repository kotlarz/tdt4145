package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.interfaces.DataTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Table
public class TreningsMalTilhorlighet implements DataTable {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(length = 6, foreignKey = {"ovelse", "id"})
    private int ovelseId;

    TreningsMalTilhorlighet(int treningsOktId, int ovelseId) {
        this.treningsOktId = treningsOktId;
        this.ovelseId = ovelseId;
    }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public void setTreningsOktId(int treningsOktId) {
        this.treningsOktId = treningsOktId;
    }

    public int getOvelseId() {
        return ovelseId;
    }

    public void setOvelseId(int ovelseId) {
        this.ovelseId = ovelseId;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);
        ps.executeUpdate();
    }
}
