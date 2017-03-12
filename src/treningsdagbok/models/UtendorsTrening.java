package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;
import treningsdagbok.enums.VaerType;
import treningsdagbok.interfaces.DataTable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Table
public class UtendorsTrening extends TreningsOkt implements DataTable {
    @TableColumn(length = 6, foreignKey={"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(precision = 3, scale = 1)
    private float temperatur;

    @TableColumn(length = 16, dataType = String.class)
    private VaerType vaerType;

    public UtendorsTrening(LocalDate dato, LocalTime tidspunkt, int varighet, int form, int prestasjon, String notat,
                           float temperatur, VaerType vaerType) {
        super(dato, tidspunkt, varighet, form, prestasjon, notat, 1);
        this.temperatur = temperatur;
        this.vaerType = vaerType;
    }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public float getTemperatur() {
        return temperatur;
    }

    public void setTemperatur(float temperatur) {
        this.temperatur = temperatur;
    }

    public VaerType getVaerType() {
        return vaerType;
    }

    public void setVaerType(VaerType vaerType) {
        this.vaerType = vaerType;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        super.create();
        this.treningsOktId = super.getId();
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);
        ps.executeUpdate();
    }
}