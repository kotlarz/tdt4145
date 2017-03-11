package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

@Table
public class TreningsMalTilhorlighet {
    @TableColumn(length = 6, foreignKey = {"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(length = 6, foreignKey = {"ovelse", "id"})
    private int ovelseId;

    TreningsMalTilhorlighet(int treningsOktId, int ovelseId) {
        this.treningsOktId = treningsOktId;
        this.ovelseId = ovelseId;
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
}
