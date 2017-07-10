package sample.json;

import com.google.gson.Gson;
import javafx.scene.text.Text;
import sample.json.model.City;
import sample.json.model.Drogi;
import sample.json.model.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 @Author Konrad Baczyński
 This class is used to read and save json files and convert string for appropriate json string.
 */
public class ParseJson {

    /**
     @param  result - it's a list city's names that are most optimal for placing FireDepartment in them.
     @return jsonString - it's a @param result converted to acceptable json format.
     */
    public String convertCitiesResultToJsonString(List<String> result){
        String resultString = "";
        for (String s1: result) {
            resultString += "\""+s1+"\",";
        }
        resultString = resultString.substring(0, resultString.length()-1);
        String jsonString = "{\"Result\": ["+resultString+"]}";
        //"miasta" : ["A", "B", "C", "D", "E","F", "G", "H", "I", "J"],
        return jsonString;
    }

    /**
     @param  jsonString - a ready to save json String
     @param path - path to a folder where file will be saved
     */
    public void saveJsonFile(String jsonString, String path) throws IOException {
        File file = new File(path+"/out.json");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter myOutWriter =new OutputStreamWriter(fileOutputStream);
        myOutWriter.append(jsonString);
        myOutWriter.close();
        fileOutputStream.close();
    }

    /**
     @param  targetFile - file selected to read json from.
     @param infoText - Text where information about success/failure of opening the file will be posted.
     @return result - Returns Result object, it is a json converted to model.
     */
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
                        infoText.setText("incorrect JSON file");
                    }
                }
            }
        }
        return null;
    }

    /**
     * This function extracts cityList and calculates cities in range from result.
     @param  result - model previoulsy read from json.
     @return cityList - a list of cities with calculated cities in range.
     */
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
            List<Drogi> directRoadList = new ArrayList<>();
            if (currentCity.equals(destinyCity)) {
                return currentDistance;
            } else {
                for (Drogi road : result.getDrogi()) {
                    if ((road.getMiasta().get(0).equals(destinyCity) && (road.getMiasta().get(1).equals(currentCity)))
                            || ((road.getMiasta().get(1).equals(destinyCity)) && (road.getMiasta().get(0).equals(currentCity)))) {
                        if (result.getMaxCzasPrzejazdu() >= currentDistance + road.getCzasPrzejazdu())
                            return calculateDistance(startingCity, destinyCity, destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                    if ((road.getMiasta().get(0).equals(currentCity) || (road.getMiasta().get(1).equals(currentCity)))) {
                        directRoadList.add(road);
                    }
                }
            }
            if (directRoadList != null) {
                for (Drogi road : directRoadList) {
                    if (currentCity.equals(road.getMiasta().get(0)) && startingCity != (road.getMiasta().get(0))) {
                        return calculateDistance(startingCity, road.getMiasta().get(1), destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                    if (currentCity.equals(road.getMiasta().get(1)) && startingCity != (road.getMiasta().get(1))) {
                        return calculateDistance(startingCity, road.getMiasta().get(0), destinyCity, result, currentDistance + road.getCzasPrzejazdu());
                    }
                }
            }

        }
        return result.getMaxCzasPrzejazdu() + 1;
    }

}
