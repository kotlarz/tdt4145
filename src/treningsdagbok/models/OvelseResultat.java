package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;
import treningsdagbok.database.DataGetters;
import treningsdagbok.database.DataUtils;
import treningsdagbok.enums.Belastning;
import treningsdagbok.exceptions.DataItemNotFoundException;
import treningsdagbok.interfaces.DataTable;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Table
public class OvelseResultat implements DataTable {
    @TableColumn(length = 6, foreignKey={"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(length = 6, foreignKey={"ovelse", "id"})
    private int ovelseId;

    @TableColumn(length = 15, nullable = true, dataType = String.class)
    private Belastning belastning;

    @TableColumn(length = 3)
    private int antallRepetisjoner;

    @TableColumn(length = 3)
    private int antallSett;

    public OvelseResultat(TreningsOkt treningsOkt, Ovelse ovelse, Belastning belastning, int antallRepetisjoner,
                          int antallSett) {
        this.treningsOktId = treningsOkt.getId();
        this.ovelseId = ovelse.getId();
        this.belastning = belastning;
        this.antallRepetisjoner = antallRepetisjoner;
        this.antallSett = antallSett;
    }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public int getOvelseId() {
        return ovelseId;
    }

    public Belastning getBelastning() {
        return belastning;
    }

    public void setBelastning(Belastning belastning) {
        this.belastning = belastning;
    }

    public int getAntallRepetisjoner() {
        return antallRepetisjoner;
    }

    public void setAntallRepetisjoner(int antallRepetisjoner) {
        this.antallRepetisjoner = antallRepetisjoner;
    }

    public int getAntallSett() {
        return antallSett;
    }

    public void setAntallSett(int antallSett) {
        this.antallSett = antallSett;
    }

    @Override
    public void create() throws SQLException, IllegalAccessException {
        PreparedStatement ps = DataUtils.generatePrepareStatementInsert(OvelseResultat.class, this);
        ps.executeUpdate();
    }

    public static OvelseResultat getByObjects(TreningsOkt treningsOkt, Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByIds(treningsOkt.getId(), ovelse.getId());
    }

    public static OvelseResultat getByIds(int treningsOktId, int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return (OvelseResultat) DataGetters.getByTwoFields(
                "TreningsOktId",
                "OvelseId",
                int.class,
                int.class,
                OvelseResultat.class,
                treningsOktId,
                ovelseId
        );
    }

    public static Set<OvelseResultat> getByOvelse(Ovelse ovelse) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        return getByOvelseId(ovelse.getId());
    }

    public static Set<OvelseResultat> getByOvelseId(int ovelseId) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, SQLException, DataItemNotFoundException,
            InvocationTargetException {
        Set<Object> objects = DataGetters.getByMultiple(
                "OvelseId",
                int.class,
                OvelseResultat.class,
                ovelseId
        );
        Iterator<Object> iterator = objects.iterator();
        Set<OvelseResultat> result = new HashSet<>();
        while (iterator.hasNext()) {
            result.add((OvelseResultat) iterator.next());
        }
        return result;
    }
}
