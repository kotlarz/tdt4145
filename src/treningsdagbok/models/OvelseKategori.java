package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

@Table
public class OvelseKategori {
    @TableColumn(length = 6, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn(length = 30)
    private String navn;

    @TableColumn(nullable = true)
    private String beskrivelse;

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
}
