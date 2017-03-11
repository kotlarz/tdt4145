package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

@Table
public class OvelseKategoriTilhorlighet {
    @TableColumn(length = 6, foreignKey = {"ovelse", "id"})
    private int ovelseId;

    @TableColumn(length = 6, foreignKey = {"ovelse_kategori", "id"})
    private int kategoriId;

    OvelseKategoriTilhorlighet(int ovelseId, int kategoriId) {
        this.ovelseId = ovelseId;
        this.kategoriId = kategoriId;
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
}
