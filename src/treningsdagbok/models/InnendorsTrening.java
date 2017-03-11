package treningsdagbok.models;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table
public class InnendorsTrening extends TreningsOkt {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(precision = 3, scale = 1)
    private String luftkvalitet;

    @TableColumn(length = 6)
    private int antallTilskuere;

    public InnendorsTrening(LocalDate dato, LocalTime tidspunkt, int varighet, int form, int prestasjon, String notat,
                            String luftkvalitet, int antallTilskuere) {
        super(dato, tidspunkt, varighet, form, prestasjon, notat, 0);
        this.luftkvalitet = luftkvalitet;
        this.antallTilskuere = antallTilskuere;
    }

    public int getTreningsOktId() { return treningsOktId; }

    public String getLuftkvalitet() {
        return luftkvalitet;
    }

    public int getAntallTilskuere() {
        return antallTilskuere;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        super.create();
        this.treningsOktId = super.getId();
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);
        ps.executeUpdate();
    }
}
