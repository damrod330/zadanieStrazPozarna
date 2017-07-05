package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sample.json.Drogi;
import sample.json.Result;
import sample.model.City;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MainPageController {

    private List<City> cityList = null;
    private int timeout;

    @FXML
    private Text infoText;

    @FXML
    private VBox mainContainer;

    @FXML
    private Button btnLoadJson;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button buttonProcesData;

    @FXML
    private TextField jsonPathtextField;

    @FXML
    void buttonLoadJsonClicked(MouseEvent event) {
        Result result = openJsonFile();
        if (result != null) {
            cityList = generateCitiesFromJson(result);
            timeout = result.getTimeout();
            buttonProcesData.setDisable(false);
        } else {
            cityList = null;
            timeout = 0;
        }
    }

    @FXML
    void buttonProcessDataClicked(MouseEvent event) {
        if (cityList != null) {
            List<String> citiesToCover = new ArrayList<>();
            for (City c1 : cityList) {
                citiesToCover.add(c1.getName());
            }
            computeData(cityList, null, citiesToCover, timeout);
        }
    }

    public List<String> computeData(List<City> cityList, List<String> result, List<String> citiesToCover, int timeout) {
        //TODO calculate result
//        if (result == null) {
//            result = new ArrayList<>();
//        }
//        for (int i = 0; i <= cityList.size(); i++) {
//            result.add(cityList.get(i).getName());
//
//            List<String> sumOfElements = new ArrayList<>();
//            for (String element : result) {
//                sumOfElements.
//            }
//                City city = (City) cityList.stream().filter(item -> item.getName().equals(element));
//                if(city.getListOfCitiesInRange().containsAll(citiesToCover)){
//                    return result;
//                }
//                else{
//                    //try other element
//                }
//        }
//
//        return computeData(cityList, result, citiesToCover);
        return null;
    }

    private Result openJsonFile() {
        infoText.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File targetFile = fileChooser.showOpenDialog(mainContainer.getScene().getWindow());

        if (targetFile != null) {
            jsonPathtextField.setText(targetFile.getPath());
            Gson gson = new Gson();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(targetFile.getPath()));
                Result result = gson.fromJson(bufferedReader, Result.class);

                System.out.println(result.getMiasta().toString());
                for (Drogi d : result.getDrogi()) {
                    System.out.println(d.getMiasta() + " - " + d.getCzasPrzejazdu());
                }

                infoText.setText("JSON file loaded succesfully");
                return result;

            } catch (FileNotFoundException e) {
                infoText.setText("file doesnt't exist");
                e.printStackTrace();

            } catch (NullPointerException e) {
                infoText.setText("incorrect JSON file");
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private List<City> generateCitiesFromJson(Result result) {
        List<City> cityList = new ArrayList<>();
        int range = result.getMaxCzasPrzejazdu();

        for (String mainCity : result.getMiasta()) {
            List<String> citiesInRangeList = new ArrayList<>();
            for (String destinyCity : result.getMiasta()) {
                int cost = calculateDistance(mainCity, mainCity, destinyCity, result, 0);
                if (range >= cost) {
                    System.out.print(mainCity+"->"+destinyCity+" time: " + cost + "; ");
                    citiesInRangeList.add(destinyCity);
                }
            }
            System.out.println();
            City city = new City(mainCity, citiesInRangeList);
            cityList.add(city);
        }
        return cityList;
    }


    private int calculateDistance(String startingCity, String currentCity, String destinyCity, Result result, int currentDistance) {
        if (currentDistance <= result.getMaxCzasPrzejazdu()) {
            //System.out.print(currentCity + ">");
            List<Drogi> directRoadList = new ArrayList<>();
            if (currentCity.equals(destinyCity)) {
                return currentDistance;
            } else {
                for (Drogi road : result.getDrogi()) {
                    if ((road.getMiasta().get(0).equals(destinyCity) && (road.getMiasta().get(1).equals(currentCity)))
                            || ((road.getMiasta().get(1).equals(destinyCity)) && (road.getMiasta().get(0).equals(currentCity)))) {
                        //System.out.println(currentCity +"->"+destinyCity+" cost:"+(currentDistance + road.getCzasPrzejazdu()));
                        if (result.getMaxCzasPrzejazdu() >= currentDistance + road.getCzasPrzejazdu())
                            return calculateDistance(startingCity, destinyCity, destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                    if ((road.getMiasta().get(0).equals(currentCity) || (road.getMiasta().get(1).equals(currentCity)))) {
                        directRoadList.add(road);
                    }
                }
            }
            //System.out.println(currentCity +"->"+destinyCity+" cost: ?");
            if (directRoadList != null) {
                for (Drogi road : directRoadList) {
                    if (currentCity.equals(road.getMiasta().get(0)) && startingCity != (road.getMiasta().get(0))) {
                        //System.out.println(road.getMiasta().get(0) +"->"+destinyCity+" cost:"+(currentDistance + road.getCzasPrzejazdu()));
                        return calculateDistance(startingCity, road.getMiasta().get(1), destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                    if (currentCity.equals(road.getMiasta().get(1)) && startingCity != (road.getMiasta().get(1))) {
                        //System.out.println(road.getMiasta().get(1) +"->"+destinyCity+" cost:"+(currentDistance + road.getCzasPrzejazdu()));
                        return calculateDistance(startingCity, road.getMiasta().get(0), destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                }
            }

        }
        return result.getMaxCzasPrzejazdu() + 1;
    }


}
