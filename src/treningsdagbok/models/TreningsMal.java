package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

@Table
public class TreningsMal {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 6)
    private int treningsOktId;

    @TableColumn(length = 30)
    private String navn;

    public TreningsMal(int treningsOktId, String navn) {
        this.treningsOktId = treningsOktId;
        this.navn = navn;
    }

    public int getId() { return id; }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) { this.navn = navn; }
}
