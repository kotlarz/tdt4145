package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.enums.VaerType;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Table
public class UtendorsTrening extends TreningsOkt {
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
}