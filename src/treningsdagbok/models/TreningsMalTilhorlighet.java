package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(TreningsMalTilhorlighet.class, this);
        ps.executeUpdate();
    }

    public static TreningsMalTilhorlighet getByIds(int treningsOktId, int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return (TreningsMalTilhorlighet) DataGetters.getByTwoFields(
                "TreningsOktId",
                "OvelseId",
                int.class,
                int.class,
                TreningsMalTilhorlighet.class,
                treningsOktId,
                ovelseId
        );
    }

    public static Set<TreningsMalTilhorlighet> getByTreningsOktId(int treningsOktId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "TreningsOktId",
                int.class,
                UtendorsTrening.class,
                treningsOktId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsMalTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsMalTilhorlighet) iterator.next();
        }
        return result;
    }

    public static Set<TreningsMalTilhorlighet> getByOvelseId(int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "ovelseId",
                int.class,
                UtendorsTrening.class,
                ovelseId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsMalTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsMalTilhorlighet) iterator.next();
        }
        return result;
    }
}
