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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class OvelseKategori implements DataTableWithId {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 30)
    private String navn;

    @TableColumn(nullable = true)
    private String beskrivelse;

    public OvelseKategori() {}

    public OvelseKategori(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) { this.id = id; }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseKategori.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny treningsøkt feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny treningsøkt feilet, returnerte ingen ID.");
            }
        }
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(OvelseKategori.class, this);
        ps.executeUpdate();
    }

    public static OvelseKategori getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (OvelseKategori) DataGetters.getById(OvelseKategori.class, id);
    }

    public static Set<OvelseKategori> getAll() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getAll(OvelseKategori.class);
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseKategori> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseKategori) iterator.next());
        }
        return result;
    }
}
