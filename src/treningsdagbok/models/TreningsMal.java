package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.interfaces.DataTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Table
public class TreningsMal implements DataTable {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 6)
    private int treningsOktId;

    @TableColumn(length = 30)
    private String navn;

    public TreningsMal(int treningsOktId, String navn) {
        this.treningsOktId = treningsOktId;
        this.navn = navn;
    }

    private void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) { this.navn = navn; }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny trenings mal feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny trenings mal feilet, returnerte ingen ID.");
            }
        }
    }
}
