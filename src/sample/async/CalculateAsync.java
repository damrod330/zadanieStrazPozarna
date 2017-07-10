package sample.async;

import sample.json.model.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author Konrad Baczyński
 * This class calculates most optimal result for placing fireDepartments based on given data.
 * List<City>cityList, List<String> citiesToCover are mandatory for calculations.
 */
public class CalculateAsync implements Callable<List<String>> {

    private List<City> cityList;
    private List<String> citiesToCover = new ArrayList<>();
    private List<String> result = new ArrayList<>();

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public void setCitiesToCover(List<String> citiesToCover) {
        this.citiesToCover = citiesToCover;
    }

    public List<String> getResult() {
        return result;
    }
    /**
     This function is called when async task is created, calculations are done here.
     */
    @Override
    public List<String> call() throws Exception {
        long startingTime = System.currentTimeMillis();
        System.out.println("Task Started..");
        calculateFireDepartments(null);
        System.out.println("Task Finished after: " + (System.currentTimeMillis() - startingTime) + " millis");
        return result;
    }

    private boolean calculationsAreDone() {
        return true;
    }

    private List<String> calculateFireDepartments(List<String> currentResult) {
        if (currentResult == null) {
            result.removeAll(result);
            result.addAll(citiesToCover);
            currentResult = new ArrayList<>();
            currentResult.addAll(result);
        }
        List<String> newCityList = new ArrayList<>();
        newCityList.addAll(currentResult);
        for (String city : newCityList) {
            currentResult.remove(city);
            if (resultIsPossible(currentResult)) {
                if (currentResult.size() < result.size()) {
                    result.removeAll(result);
                    result.addAll(currentResult);
                }
                calculateFireDepartments(currentResult);
            }
            currentResult.add(city);
            Collections.sort(currentResult);
        }
        return currentResult;
    }

    private boolean resultIsPossible(List<String> currentResult) {
        if (currentResult != null) {
            List<String> citiesInRangeList = new ArrayList<>();
            List<City> currentCityList = new ArrayList<>();
            for (String s1 : currentResult) {
                for (City c1 : this.cityList) {
                    if (s1.equals(c1.getName())) {
                        currentCityList.add(c1);
                    }
                }
            }
            for (City city : currentCityList) {
                for (String cityName : city.getListOfCitiesInRange()) {
                    if (!citiesInRangeList.contains(cityName)) {
                        citiesInRangeList.add(cityName);
                    }
                }
            }
            if (citiesInRangeList.containsAll(citiesToCover)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean equalLists(List<String> one, List<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

}
