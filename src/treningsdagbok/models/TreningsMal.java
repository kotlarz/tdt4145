package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class TreningsMal implements DataTable {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 6)
    private int treningsOktId;

    @TableColumn(length = 30)
    private String navn;

    public TreningsMal(TreningsOkt treningsOkt, String navn) {
        this.treningsOktId = treningsOkt.getId();
        this.navn = navn;
    }

    private void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) { this.navn = navn; }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(TreningsMal.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny trenings mal feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny trenings mal feilet, returnerte ingen ID.");
            }
        }
    }

    public static TreningsMal getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (TreningsMal) DataGetters.getById(TreningsMal.class, id);
    }

    public static Set<TreningsMal> getByTreningsOkt(TreningsOkt treningsOkt) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByTreningsOktId(treningsOkt.getId());
    }

    public static Set<TreningsMal> getByTreningsOktId(int treningsOktId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "TreningsOktId",
                int.class,
                TreningsMal.class,
                treningsOktId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsMal> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsMal) iterator.next());
        }
        return result;
    }
}
