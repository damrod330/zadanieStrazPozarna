package sample.async;

import sample.model.City;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Damrod on 08.07.2017.
 */
public class CalculateAsync implements Callable<Boolean> {

    private List<City> cityList;
    private List<String> citiesToCover;

    public List<String> getCitiesToCover() {
        return citiesToCover;
    }

    public void setCitiesToCover(List<String> citiesToCover) {
        this.citiesToCover = citiesToCover;
    }

    public CalculateAsync(List<City> cityList, List<String> citiesToCover) {
        this.citiesToCover = citiesToCover;
        this.cityList = cityList;
    }

    @Override
    public Boolean call() throws Exception {
        for (City city : cityList) {
            if(citiesToCover!=null){
                citiesToCover.remove(0);
                Thread.sleep(1421);
            }
        }
        return true;
    }

}
