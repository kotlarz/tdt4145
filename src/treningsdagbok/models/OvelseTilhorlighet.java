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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class OvelseTilhorlighet implements DataTable {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(length = 6, foreignKey = {"ovelse", "id"})
    private int ovelseId;

    OvelseTilhorlighet(TreningsOkt treningsOkt, Ovelse ovelse) {
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
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseTilhorlighet.class, this);
        ps.executeUpdate();
    }

    public static OvelseMaal getByObjects(TreningsOkt treningsOkt, Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByIds(treningsOkt.getId(), ovelse.getId());
    }

    public static OvelseMaal getByIds(int treningsOktId, int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return (OvelseMaal) DataGetters.getByTwoFields(
                "TreningsOktId",
                "OvelseId",
                int.class,
                int.class,
                OvelseMaal.class,
                treningsOktId,
                ovelseId
        );
    }

    public static Set<OvelseTilhorlighet> getByTreningsOkt(TreningsOkt treningsOkt) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByTreningsOktId(treningsOkt.getId());
    }

    public static Set<OvelseTilhorlighet> getByTreningsOktId(int treningsOktId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "TreningsOktId",
                int.class,
                OvelseMaal.class,
                treningsOktId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseTilhorlighet) iterator.next());
        }
        return result;
    }

    public static Set<OvelseTilhorlighet> getByOvelse(Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByOvelseId(ovelse.getId());
    }

    public static Set<OvelseTilhorlighet> getByOvelseId(int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "OvelseId",
                int.class,
                OvelseMaal.class,
                ovelseId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseTilhorlighet) iterator.next());
        }
        return result;
    }
}
