package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;
import treningsdagbok.interfaces.DataTableWithId;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class OvelseMaal implements DataTableWithId {
    @TableColumn(length = 11, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn
    private LocalDateTime start;

    @TableColumn
    private LocalDateTime end;

    @TableColumn
    private String beskrivelse;

    public OvelseMaal() {}

    public OvelseMaal(LocalDateTime start, LocalDateTime end, String beskrivelse) {
        this.start = start;
        this.end = end;
        this.beskrivelse = beskrivelse;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) { this.id = id; }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseMaal.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny øvelse mål feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny øvelse mål feilet, returnerte ingen ID.");
            }
        }
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(OvelseMaal.class, this);
        ps.executeUpdate();
    }

    public static OvelseMaal getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (OvelseMaal) DataGetters.getById(OvelseMaal.class, id);
    }

    public static Set<OvelseMaal> getAll() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getAll(OvelseMaal.class);
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseMaal> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseMaal) iterator.next());
        }
        return result;
    }
}
