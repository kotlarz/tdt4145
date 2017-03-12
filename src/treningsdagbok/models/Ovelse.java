package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.enums.Belastning;
import treningsdagbok.interfaces.DataTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Table
public class Ovelse implements DataTable {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 30)
    private String navn;

    @TableColumn(nullable = true)
    private String beskrivelse;

    @TableColumn(length = 15, nullable = true, dataType = String.class)
    private Belastning belastning;

    @TableColumn(length = 3)
    private int antallRepetisjoner;

    @TableColumn(length = 3)
    private int antallSett;

    public Ovelse(String navn, String beskrivelse, Belastning belastning, int antallRepetisjoner, int antallSett) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.belastning = belastning;
        this.antallRepetisjoner = antallRepetisjoner;
        this.antallSett = antallSett;
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

    public Belastning getBelastning() {
        return belastning;
    }

    public void setBelastning(Belastning belastning) {
        this.belastning = belastning;
    }

    public int getAntallRepetisjoner() {
        return antallRepetisjoner;
    }

    public void setAntallRepetisjoner(int antallRepetisjoner) {
        this.antallRepetisjoner = antallRepetisjoner;
    }

    public int getAntallSett() {
        return antallSett;
    }

    public void setAntallSett(int antallSett) {
        this.antallSett = antallSett;
    }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);

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
