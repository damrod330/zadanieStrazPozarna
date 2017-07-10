package sample.json.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Drogi {

    @SerializedName("miasta")
    @Expose
    private List<String> miasta = null;
    @SerializedName("czas_przejazdu")
    @Expose
    private Integer czasPrzejazdu;

    public List<String> getMiasta() {
        return miasta;
    }

    public void setMiasta(List<String> miasta) {
        this.miasta = miasta;
    }

    public Integer getCzasPrzejazdu() {
        return czasPrzejazdu;
    }

    public void setCzasPrzejazdu(Integer czasPrzejazdu) {
        this.czasPrzejazdu = czasPrzejazdu;
    }

}