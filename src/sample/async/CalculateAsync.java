package sample.async;

import sample.model.City;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

       calculateFireDepartments(citiesToCover,cityList,null);
        System.out.println("Task ended..");
        System.out.println("Finished after: " + (System.currentTimeMillis() - startingTime));
        return result;

    }

    private List<String> calculateFireDepartments(List<String> citiesToCover, List<City> cityList, List<String> currentResult) {
        if(result==null){
            result = citiesToCover;
            currentResult = result;
        }
        //usun losowy element
        currentResult.remove(new Random().nextInt(currentResult.size()));
        //sprawdz, czy po usunieciu elementu nic sie nie zawali
        if(resultIsPossible(currentResult, cityList) ){
            //jeśli nie to sprawdz, czy nowy rezultat jest mniejszy niz poprzedni
            if (currentResult.size() < result.size()){
                result = currentResult;
                return
            }
        }



//        for (City city : cityList) {
//            List<String> currentResoult = new ArrayList<>();
//            if (equalLists(city.getListOfCitiesInRange(), citiesToCover)) {
//                currentResoult.add(city.getName());
//                if (result.size() > currentResoult.size()) {
//                    result = currentResoult;
//                    return result;
//                } else {
//
//                }
//            } else {
//                cityList.remove(city);
//                currentResoult.add(city.getName());
//                for (String s1 : city.getListOfCitiesInRange()) {
//                    citiesToCover.remove(s1);
//                }
//                result.add(city.getName());
//                return coveredCities(citiesToCover, cityList, result);
//            }
 //       }

        return result;
    }

    private boolean resultIsPossible(List<String> currentResult, List<City> cityList) {
        return true;
    }

    public boolean equalLists(List<String> one, List<String> two) {
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
