package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

import java.time.LocalDateTime;

@Table
public class OvelseMal {
    @TableColumn(length = 11, primaryKey = true, autoIncrement = true)
    private int id;

    @TableColumn
    private LocalDateTime start;

    @TableColumn
    private LocalDateTime end;

    @TableColumn
    private String beskrivelse;

    public OvelseMal(LocalDateTime start, LocalDateTime end, String beskrivelse) {
        this.start = start;
        this.end = end;
        this.beskrivelse = beskrivelse;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) { this.id = id; }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
}
