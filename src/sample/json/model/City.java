package sample.json.model;

import java.util.List;


/**
 @Author Konrad Baczyński
Class City is a model class.
 */
public class City {

    private String name;
    private List<String> listOfCitiesInRange;

    public City(String name, List<String> listOfCitiesInRange) {
        this.name = name;
        this.listOfCitiesInRange = listOfCitiesInRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListOfCitiesInRange() {
        return listOfCitiesInRange;
    }

    public void setListOfCitiesInRange(List<String> listOfCitiesInRange) {
        this.listOfCitiesInRange = listOfCitiesInRange;
    }
}
