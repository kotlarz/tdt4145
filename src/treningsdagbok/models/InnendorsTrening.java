package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table
public class InnendorsTrening extends TreningsOkt implements DataTable {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"}, identifier = true)
    private int treningsOktId;

    @TableColumn(precision = 3, scale = 1)
    private float luftkvalitet;

    @TableColumn(length = 6)
    private int antallTilskuere;

    public InnendorsTrening() {}

    public InnendorsTrening(LocalDate dato, LocalTime tidspunkt, int varighet, int form, int prestasjon, String notat,
                            float luftkvalitet, int antallTilskuere) {
        super(dato, tidspunkt, varighet, form, prestasjon, notat, 0);
        this.luftkvalitet = luftkvalitet;
        this.antallTilskuere = antallTilskuere;
    }

    public int getTreningsOktId() { return treningsOktId; }

    public float getLuftkvalitet() {
        return luftkvalitet;
    }

    public int getAntallTilskuere() {
        return antallTilskuere;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        super.create();
        this.treningsOktId = super.getId();
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(InnendorsTrening.class, this);
        ps.executeUpdate();
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(InnendorsTrening.class, this);
        ps.executeUpdate();
        super.delete();
    }

    public static InnendorsTrening getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (InnendorsTrening) DataGetters.getBy(
                "TreningsOktId",
                int.class,
                InnendorsTrening.class,
                id
        );
    }
}
