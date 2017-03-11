package treningsdagbok.models;

import treningsdagbok.annotations.Table;
import treningsdagbok.annotations.TableColumn;

import java.time.LocalDateTime;

@Table
public class TreningsData {
    @TableColumn(length = 6, foreignKey={"trenings_okt", "id"})
    private int treningsOktId;

    @TableColumn(fieldDefault = "CURRENT_TIMESTAMP")
    private LocalDateTime tid;

    @TableColumn(length = 3, nullable = true)
    private int puls;
    @TableColumn(precision = 11, scale = 8, nullable = true)
    private float lengdegrad;

    @TableColumn(precision = 12, scale = 8, nullable = true)
    private float breddegrad;

    @TableColumn(length = 4, nullable = true)
    private int moh;

    public TreningsData(LocalDateTime tid, int puls, float lengdegrad, float breddegrad, int moh) {
        this.tid = tid;
        this.puls = puls;
        this.lengdegrad = lengdegrad;
        this.breddegrad = breddegrad;
        this.moh = moh;
    }

    public int getTreningsOktId() {
        return treningsOktId;
    }

    public LocalDateTime getTid() {
        return tid;
    }

    public void setTid(LocalDateTime tid) {
        this.tid = tid;
    }

    public int getPuls() {
        return puls;
    }

    public void setPuls(int puls) {
        this.puls = puls;
    }

    public float getLengdegrad() {
        return lengdegrad;
    }

    public void setLengdegrad(float lengdegrad) {
        this.lengdegrad = lengdegrad;
    }

    public float getBreddegrad() {
        return breddegrad;
    }

    public void setBreddegrad(float breddegrad) {
        this.breddegrad = breddegrad;
    }

    public int getMoh() {
        return moh;
    }

    public void setMoh(int moh) {
        this.moh = moh;
    }
}
