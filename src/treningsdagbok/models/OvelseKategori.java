package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.interfaces.DataTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Table
public class OvelseKategori implements DataTable {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 30)
    private String navn;

    @TableColumn(nullable = true)
    private String beskrivelse;

    public OvelseKategori(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) { this.id = id; }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseKategori.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny treningsøkt feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny treningsøkt feilet, returnerte ingen ID.");
            }
        }
    }
}
