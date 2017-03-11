package treningsdagbok.models;

import treningsdagbok.TreningsDagbok;
import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.logging.Logger;

@Table
public class TreningsOkt {
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

    public TreningsOkt(LocalDate dato, LocalTime tidspunkt, int varighet,
                       int form, int prestasjon, String notat, int erUtendors) {
        this.dato = dato;
        this.tidspunkt = tidspunkt;
        this.varighet = varighet;
        this.form = form;
        this.prestasjon = prestasjon;
        this.notat = notat;
        this.erUtendors = erUtendors;
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

    public void setErUtendors(int erUtendors) {
        this.erUtendors = erUtendors;
    }

    public static TreningsOkt getTreningsOktById(int id) throws SQLException {
        String query = "SELECT * FROM treningsokt WHERE id = ?";
        PreparedStatement ps = TreningsDagbok.getDataManager().getConnection().prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Id "+rs.getInt("id")+" Name "+rs.getString("notat"));
        }
        ps.close();

        /*
        TreningsOkt treningsOkt = new TreningsOkt(
                rs.getDate("dato"), rs.getTime("tidspunkt"), rs.getInt("varighet"),
                rs.getInt("form"), rs.getInt("prestasjon"), rs.getString("notat")
        );
        treningsOkt.setId(rs.getInt("id"));
        return treningsOkt;*/
        return null;
    }

    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(this);

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
}
