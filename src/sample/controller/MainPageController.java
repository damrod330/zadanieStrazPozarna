package sample.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import sample.async.CalculateAsync;
import sample.json.Drogi;
import sample.json.ParseJson;
import sample.json.Result;
import sample.model.City;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MainPageController {

    private List<City> cityList = null;
    private int timeout;

    @FXML
    private TextArea resultTextArea;

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
        infoText.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JSON file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File targetFile = fileChooser.showOpenDialog(mainContainer.getScene().getWindow());
        if (targetFile != null) {
            jsonPathtextField.setText(targetFile.getPath());
            ParseJson parseJson = new ParseJson();
            Result result = parseJson.openJsonFile(targetFile, infoText);
            if (result != null) {
                cityList = parseJson.generateCitiesFromJson(result);
                timeout = result.getTimeout();
                buttonProcesData.setDisable(false);
            } else {
                cityList = null;
                timeout = 0;
            }
        }
    }

    @FXML
    void buttonProcessDataClicked(MouseEvent event) {
        buttonProcesData.setDisable(true);
        if (cityList != null) {
            List<String> citiesToCover = new ArrayList<>();
            for (City c1 : cityList) {
                citiesToCover.add(c1.getName());
            }
            computeData(cityList, citiesToCover, timeout);
        }
    }

    private static final ScheduledExecutorService pool =
            Executors.newScheduledThreadPool(2,
                    new ThreadFactoryBuilder()
                            .setDaemon(true)
                            .setNameFormat("FutureOps-%d")
                            .build()
            );

    public static <T> CompletableFuture<T> timeoutAfter(
            Duration duration) {
        final CompletableFuture<T> promise = new CompletableFuture<>();
        pool.schedule(
                () -> promise.completeExceptionally(new TimeoutException()),
                (long) duration.toMillis(), TimeUnit.MILLISECONDS);
        return promise;
    }

    public void computeData(List<City> cityList, List<String> citiesToCover, long timeout) {


        //ExecutorService executorService = Executors.newSingleThreadExecutor();
        CalculateAsync calculateAsync = new CalculateAsync(cityList, citiesToCover);

        CompletableFuture<List<String>> futureTimeout = timeoutAfter(Duration.seconds(timeout));
        CompletableFuture<List<String>> futureCalculations = doAsyncCalculations(calculateAsync);

        futureCalculations.applyToEither(futureTimeout, lambda -> {
            List<String> result = calculateAsync.getResult();
            System.out.println("Result: " + result);
            System.out.println("Task finished before timeout");
            resultTextArea.setText(result.toString());
            return futureCalculations;
        }).exceptionally(lambda -> {

            List<String> result = calculateAsync.getResult();
            //futureCalculations.complete(result);
            System.out.println("Result: " + result);
            System.out.println("Timeout called, task terminated");
            resultTextArea.setText(result.toString());
            futureCalculations.cancel(true);

            return futureCalculations;

        });


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
    }

    private CompletableFuture<List<String>> doAsyncCalculations(CalculateAsync calculateAsync) {
        final CompletableFuture<List<String>> promise = new CompletableFuture<>();
        pool.schedule(calculateAsync,0, TimeUnit.MICROSECONDS);
        return promise;
    }


}
