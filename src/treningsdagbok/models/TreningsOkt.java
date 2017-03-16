package treningsdagbok.models;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;
import treningsdagbok.interfaces.DataTableWithId;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

@Table
public class TreningsOkt implements DataTableWithId {
    private static final Logger LOGGER = Logger.getLogger(TreningsOkt.class.getName());

    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn
    private LocalDate dato;

    @TableColumn
    private LocalTime tidspunkt;

    @TableColumn(length = 10)
    private int varighet;

    @TableColumn(length = 2)
    private int form;

    @TableColumn(length = 2)
    private int prestasjon;

    @TableColumn()
    private String notat;

    @TableColumn(length = 1)
    private int erUtendors;

    private Set<Ovelse> ovelser;

    private Set<TreningsData> data;

    private Map<Integer, Set<OvelseResultat>> ovelseResultater;

    public TreningsOkt() {}

    public TreningsOkt(LocalDate dato, LocalTime tidspunkt, int varighet,
                       int form, int prestasjon, String notat, int erUtendors) {
        this.dato = dato;
        this.tidspunkt = tidspunkt;
        this.varighet = varighet;
        this.form = form;
        this.prestasjon = prestasjon;
        this.notat = notat;
        this.erUtendors = erUtendors;
        this.ovelser = null;
        this.data = null;
        this.ovelseResultater = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public LocalTime getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(LocalTime tidspunkt) {
        this.tidspunkt = tidspunkt;
    }

    public int getVarighet() {
        return varighet;
    }

    public void setVarighet(int varighet) {
        this.varighet = varighet;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getPrestasjon() {
        return prestasjon;
    }

    public void setPrestasjon(int prestasjon) {
        this.prestasjon = prestasjon;
    }

    public String getNotat() {
        return notat;
    }

    public void setNotat(String notat) {
        this.notat = notat;
    }

    public int getErUtendors() {
        return erUtendors;
    }

    public boolean isUtendors() {
        return erUtendors == 1;
    }

    public void setErUtendors(int erUtendors) {
        this.erUtendors = erUtendors;
    }

    @Override
    public String toString() {
        return "[TreningsOkt"
                + " | id=" + this.id
                + " | dato=" + this.dato.toString()
                + " | tidspunkt=" + this.tidspunkt.toString()
                + " | utendørs = " + (this.isUtendors() ? "ja" : "nei")
                + "]";
    }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(TreningsOkt.class, this);

        int affectedRows = ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Oppretting av ny treningsøkt feilet, ingen rader påvirket.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                this.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Oppretting av en ny treningsøkt feilet, returnerte ingen ID.");
            }
        }
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(TreningsOkt.class, this);
        ps.executeUpdate();
    }

    public void addOvelse(Ovelse ovelse) throws NoSuchMethodException, IllegalAccessException, InstantiationException,
            SQLException, DataItemNotFoundException, InvocationTargetException {
        this.getOvelser();
        if (this.ovelser.contains(ovelse)) {
            throw new IllegalArgumentException("Øvelse eksiterer i treningsøkten allerede.");
        }
        OvelseTilhorlighet newTilhorlighet = new OvelseTilhorlighet(this, ovelse);
        newTilhorlighet.create();
        this.ovelser.add(ovelse);
    }

    public Set<Ovelse> getOvelser() throws NoSuchMethodException, IllegalAccessException, InstantiationException,
            SQLException, DataItemNotFoundException, InvocationTargetException {
        if (this.ovelser == null) {
            this.ovelser = new HashSet<>();
            Set<OvelseTilhorlighet> tilhorligheter = OvelseTilhorlighet.getByTreningsOktId(this.getId());
            for (OvelseTilhorlighet tilhorlighet : tilhorligheter) {
                this.ovelser.add(Ovelse.getById(tilhorlighet.getOvelseId()));
            }
        }
        return this.ovelser;
    }

    public void addData(TreningsData data) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        this.getTreningsData();
        if (this.data.contains(data)) {
            throw new IllegalArgumentException("Trenings objektet data eksiterer i treningsøkten allerede.");
        }
        this.data.add(data);
    }

    public Set<TreningsData> getTreningsData() throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        if (this.data == null) {
            this.data = TreningsData.getByTreningsOktId(this.getId());
        }
        return this.data;
    }

    public void addOvelseResultat(Ovelse ovelse, OvelseResultat ovelseResultat) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        this.getOvelseResultat(ovelse);
        if (this.ovelseResultater.containsKey(ovelse.getId())
                && this.ovelseResultater.get(ovelse.getId()).contains(ovelseResultat)) {
            throw new IllegalArgumentException("Trenings objektet data eksiterer i treningsøkten allerede.");
        }
        Set<OvelseResultat> newOvelseResultater = this.ovelseResultater.get(ovelse.getId());
        newOvelseResultater.add(ovelseResultat);
        this.ovelseResultater.put(ovelse.getId(), newOvelseResultater);
    }

    public Set<OvelseResultat> getOvelseResultat(Ovelse ovelse) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        if (!this.ovelseResultater.containsKey(ovelse.getId())) {
            Set<OvelseResultat> newOvelseResultater = new HashSet<>();
            OvelseResultat ovelseResultat = OvelseResultat.getByObjects(this, ovelse);
            newOvelseResultater.add(ovelseResultat);
            this.ovelseResultater.put(ovelse.getId(), newOvelseResultater);
        }
        return this.ovelseResultater.get(ovelse.getId());
    }

    public static TreningsOkt getById(int id) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, SQLException, DataItemNotFoundException, InvocationTargetException {
        return (TreningsOkt) DataGetters.getById(TreningsOkt.class, id);
    }

    public static Set<TreningsOkt> getAll() throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getAll(TreningsOkt.class);
        Iterator<Object> iterator = objects.iterator();
        Set<TreningsOkt> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((TreningsOkt) iterator.next());
        }
        return result;
    }
}
