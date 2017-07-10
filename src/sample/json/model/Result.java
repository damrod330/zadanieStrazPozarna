package sample.json.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("miasta")
    @Expose
    private List<String> miasta = null;
    @SerializedName("drogi")
    @Expose
    private List<Drogi> drogi = null;
    @SerializedName("timeout")
    @Expose
    private Integer timeout;
    @SerializedName("max_czas_przejazdu")
    @Expose
    private Integer maxCzasPrzejazdu;

    public List<String> getMiasta() {
        return miasta;
    }

    public void setMiasta(List<String> miasta) {
        this.miasta = miasta;
    }

    public List<Drogi> getDrogi() {
        return drogi;
    }

    public void setDrogi(List<Drogi> drogi) {
        this.drogi = drogi;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxCzasPrzejazdu() {
        return maxCzasPrzejazdu;
    }

    public void setMaxCzasPrzejazdu(Integer maxCzasPrzejazdu) {
        this.maxCzasPrzejazdu = maxCzasPrzejazdu;
    }

}