package sample.async;

import sample.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Damrod on 08.07.2017.
 */
public class CalculateAsync implements Callable<List<String>> {

    private List<City> cityList;
    private List<String> citiesToCover;
    private List<String> result = new ArrayList<>();

    public List<String> getResult() {
        return result;
    }

    public CalculateAsync(List<City> cityList, List<String> citiesToCover) {
        this.citiesToCover = citiesToCover;
        this.cityList = cityList;
    }

    @Override
    public List<String> call() throws Exception {
        long startingTime = System.currentTimeMillis();
        System.out.println("Task Started..");

        for (String s1 : citiesToCover) {
            Thread.sleep(500);
            result.add("A");
            System.out.println("working...");
        }
        System.out.println("Task ended..");
        System.out.println("Finished after: " + (System.currentTimeMillis() - startingTime));
        return result;

    }


}
