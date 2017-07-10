package sample.json;

import com.google.gson.Gson;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import sample.model.City;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Damrod on 08.07.2017.
 */
public class ParseJson {

    public Result openJsonFile(File targetFile, Text infoText) {

        if (targetFile != null) {
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

    public List<City> generateCitiesFromJson(Result result) {
        List<City> cityList = new ArrayList<>();
        int range = result.getMaxCzasPrzejazdu();

        for (String mainCity : result.getMiasta()) {
            List<String> citiesInRangeList = new ArrayList<>();
            for (String destinyCity : result.getMiasta()) {
                int cost = calculateDistance(mainCity, mainCity, destinyCity, result, 0);
                if (range >= cost) {
                    System.out.print(mainCity + "->" + destinyCity + " time: " + cost + "; ");
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
