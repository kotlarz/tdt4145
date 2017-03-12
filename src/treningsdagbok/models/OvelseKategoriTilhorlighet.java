package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.interfaces.DataTable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Table
public class OvelseKategoriTilhorlighet implements DataTable {
    @TableColumn(length = 6, foreignKey = {"ovelse", "id"})
    private int ovelseId;

    @TableColumn(length = 6, foreignKey = {"ovelse_kategori", "id"})
    private int kategoriId;

    OvelseKategoriTilhorlighet(int ovelseId, int kategoriId) {
        this.ovelseId = ovelseId;
        this.kategoriId = kategoriId;
    }

    public int getOvelseId() {
        return ovelseId;
    }

    public void setOvelseId(int ovelseId) {
        this.ovelseId = ovelseId;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseKategoriTilhorlighet.class, this);
        ps.executeUpdate();
    }
}
