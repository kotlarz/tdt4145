package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.enums.VaerType;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class UtendorsTrening extends TreningsOkt implements DataTable {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"}, identifier = true)
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
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(UtendorsTrening.class, this);
        ps.executeUpdate();
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(UtendorsTrening.class, this);
        ps.executeUpdate();
        super.delete();
    }

    public static UtendorsTrening getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (UtendorsTrening) DataGetters.getBy(
                "TreningsOktId",
                int.class,
                UtendorsTrening.class,
                id
        );
    }
}