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
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"}, identifier = true)
    private int treningsOktId;

    @TableColumn(length = 6, foreignKey = {"ovelse", "id"}, identifier = true)
    private int ovelseId;

    TreningsMalTilhorlighet(TreningsOkt treningsOkt, Ovelse ovelse) {
        this.treningsOktId = treningsOkt.getId();
        this.ovelseId = ovelse.getId();
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

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(TreningsMalTilhorlighet.class, this);
        ps.executeUpdate();
    }

    public static TreningsMalTilhorlighet getByObjects(TreningsOkt treningsOkt, Ovelse ovelse) throws
            NoSuchMethodException, IllegalAccessException, InstantiationException, SQLException,
            DataItemNotFoundException, InvocationTargetException {
        return getByIds(treningsOkt.getId(), ovelse.getId());
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

    public static Set<TreningsMalTilhorlighet> getByTreningsOkt(TreningsOkt treningsOkt) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByTreningsOktId(treningsOkt.getId());
    }

    public static Set<TreningsMalTilhorlighet> getByTreningsOktId(int treningsOktId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "TreningsOktId",
                int.class,
                TreningsMalTilhorlighet.class,
                treningsOktId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsMalTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsMalTilhorlighet) iterator.next());
        }
        return result;
    }

    public static Set<TreningsMalTilhorlighet> getByOvelse(Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByOvelseId(ovelse.getId());
    }

    public static Set<TreningsMalTilhorlighet> getByOvelseId(int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "OvelseId",
                int.class,
                TreningsMalTilhorlighet.class,
                ovelseId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsMalTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsMalTilhorlighet) iterator.next());
        }
        return result;
    }
}
