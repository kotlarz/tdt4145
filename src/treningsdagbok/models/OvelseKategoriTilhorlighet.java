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
public class OvelseKategoriTilhorlighet implements DataTable {
    @TableColumn(length = 6, foreignKey = {"ovelse", "id"}, identifier = true)
    private int ovelseId;

    @TableColumn(length = 6, foreignKey = {"ovelse_kategori", "id"}, identifier = true)
    private int kategoriId;

    public OvelseKategoriTilhorlighet() {}

    public OvelseKategoriTilhorlighet(Ovelse ovelse, OvelseKategori kategori) {
        this.ovelseId = ovelse.getId();
        this.kategoriId = kategori.getId();
    }

    public int getOvelseId() {
        return ovelseId;
    }

    public void setOvelseId(int ovelseId) {
        this.ovelseId = ovelseId;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseKategoriTilhorlighet.class, this);
        ps.executeUpdate();
    }

    @Override
    public void delete() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementDelete(OvelseKategoriTilhorlighet.class, this);
        ps.executeUpdate();
    }

    public static Set<OvelseKategoriTilhorlighet> getByOvelse(Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByOvelseId(ovelse.getId());
    }

    public static Set<OvelseKategoriTilhorlighet> getByOvelseId(int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "OvelseId",
                int.class,
                OvelseKategoriTilhorlighet.class,
                ovelseId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseKategoriTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseKategoriTilhorlighet) iterator.next());
        }
        return result;
    }

    public static Set<OvelseKategoriTilhorlighet> getByKategori(OvelseKategori kategori) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByKategoriId(kategori.getId());
    }

    public static Set<OvelseKategoriTilhorlighet> getByKategoriId(int kategoriId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "KategoriId",
                int.class,
                OvelseKategoriTilhorlighet.class,
                kategoriId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseKategoriTilhorlighet> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseKategoriTilhorlighet) iterator.next());
        }
        return result;
    }
}
