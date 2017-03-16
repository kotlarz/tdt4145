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
public class Ovelse implements DataTableWithId {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 30)
    private String navn;

    @TableColumn(nullable = true)
    private String beskrivelse;

    private Set<OvelseKategori> kategorier;

    private Set<OvelseResultat> resultater;

    public Ovelse() {}

    public Ovelse(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.kategorier = null;
    }

    @Override
    public String toString() {
        return "[Ovelse"
                + " | id=" + this.id
                + " | navn=" + this.navn
                + " | beskrivelse=" + this.beskrivelse + "]";
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

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(Ovelse.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny øvelse feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Oppretting av en ny øvelse feilet, returnerte ingen ID.");
            }
        }
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(Ovelse.class, this);
        ps.executeUpdate();
    }

    public void addKategori(OvelseKategori kategori) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        this.getKategorier();
        if (this.kategorier.contains(kategori)) {
            throw new IllegalArgumentException("Kategori eksiterer i treningsøkten allerede.");
        }
        OvelseKategoriTilhorlighet newTilhorlighet = new OvelseKategoriTilhorlighet(this, kategori);
        newTilhorlighet.create();
        this.kategorier.add(kategori);
    }

    public Set<OvelseKategori> getKategorier() throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        if (this.kategorier == null) {
            this.kategorier = new HashSet<>();
            Set<OvelseKategoriTilhorlighet> tilhorligheter = OvelseKategoriTilhorlighet.getByOvelseId(this.getId());
            for (OvelseKategoriTilhorlighet tilhorlighet : tilhorligheter) {
                this.kategorier.add(OvelseKategori.getById(tilhorlighet.getKategoriId()));
            }
        }
        return this.kategorier;
    }

    public Set<OvelseResultat> getResultater() throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        if (this.resultater == null) {
            this.resultater = OvelseResultat.getByOvelse(this);
        }
        return this.resultater;
    }

    public static Ovelse getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (Ovelse) DataGetters.getById(Ovelse.class, id);
    }

    public static Set<Ovelse> getAll() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getAll(Ovelse.class);
        Iterator<Object> iterator = objects.iterator();
        Set<Ovelse> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((Ovelse) iterator.next());
        }
        return result;
    }
}
