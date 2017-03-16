package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;
import treningsdagbok.program.TreningsDagbookScanner;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class TreningsData implements DataTable {
    @TableColumn(length = 6, foreignKey={"trenings_okt", "id"}, identifier = true)
    private int treningsOktId;

    @TableColumn(fieldDefault = "CURRENT_TIMESTAMP", identifier = true)
    private LocalDateTime tid;

    @TableColumn(length = 3, nullable = true)
    private int puls;

    @TableColumn(precision = 11, scale = 8, nullable = true)
    private float lengdegrad;

    @TableColumn(precision = 12, scale = 8, nullable = true)
    private float breddegrad;

    @TableColumn(length = 4, nullable = true)
    private int moh;

    public TreningsData() {}

    public TreningsData(TreningsOkt treningsOkt, LocalDateTime tid, int puls,
                        float lengdegrad, float breddegrad, int moh) {
        this.treningsOktId = treningsOkt.getId();
        this.tid = tid;
        this.puls = puls;
        this.lengdegrad = lengdegrad;
        this.breddegrad = breddegrad;
        this.moh = moh;
    }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public LocalDateTime getTid() {
        return tid;
    }

    public void setTid(LocalDateTime tid) {
        this.tid = tid;
    }

    public int getPuls() {
        return puls;
    }

    public void setPuls(int puls) {
        this.puls = puls;
    }

    public float getLengdegrad() {
        return lengdegrad;
    }

    public void setLengdegrad(float lengdegrad) {
        this.lengdegrad = lengdegrad;
    }

    public float getBreddegrad() {
        return breddegrad;
    }

    public void setBreddegrad(float breddegrad) {
        this.breddegrad = breddegrad;
    }

    public int getMoh() {
        return moh;
    }

    public void setMoh(int moh) {
        this.moh = moh;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(TreningsData.class, this);
        ps.executeUpdate();
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(TreningsData.class, this);
        ps.executeUpdate();
    }

    public static Set<TreningsData> getByTreningsOkt(TreningsOkt treningsOkt) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByTreningsOktId(treningsOkt.getId());
    }

    public static Set<TreningsData> getByTreningsOktId(int treningsOktId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "TreningsOktId",
                int.class,
                TreningsData.class,
                treningsOktId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsData> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsData) iterator.next());
        }
        return result;
    }
}
