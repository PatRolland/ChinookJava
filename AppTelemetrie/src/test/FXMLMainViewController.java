/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;

import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author club
 */
public class FXMLMainViewController implements Initializable {
    DataBase d = new DataBase();
    ObservableList<AreaChart.Series> areaChartData = FXCollections.observableArrayList();
    
    AreaChart.Series<Double, Double> serieTheorique = new AreaChart.Series();
    ObservableList<AreaChart.Data<Double, Double>> serieTheoriqueData = FXCollections.observableArrayList();
    AreaChart.Series<Double, Double> seriePratique = new AreaChart.Series();
    ObservableList<AreaChart.Data<Double, Double>> seriePratiqueData = FXCollections.observableArrayList();
    
    double i = 0;
    double j = 0;
    Thread t;
    
   
    @FXML
    ListView DocumentList;
    
    @FXML
    AreaChart RPMROUE_Chart;
     
    
    /**
     * Initializes the controller class.
     */
    @FXML
    private void handleOpenMenu(ActionEvent event)
    {
        System.out.println("testopen");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File filechosen = fileChooser.showOpenDialog(null);
        
        System.out.println(filechosen.getAbsolutePath());               
    }
    
    @FXML
    private void handleCheckBox_Theorique(ActionEvent event)
    {  
        for(int i = 0; i < 10; ++i) {
            
                Platform.runLater(t);
            
                
        }
        
    }
    @FXML
    private void handleCheckBox_Pratique(ActionEvent event)
    {  
        CSV_Data dat = new CSV_Data("1", "2", "3");
        
        addItem(1, Double.parseDouble(dat.docNumber), Double.parseDouble(dat.index));
    }
    
    public void addItem(int serie, double x, double y)
    {
        AreaChart.Data<Double, Double> data;
        data = new AreaChart.Data<>(x,y);
        Thread thread;
        
        if(serie == 1)
        {
            serieTheorique.getData().add(data);
//            thread = new addItemThread(serieTheorique, x, y);
        }
        else //if(serie == 2)
        {
            seriePratique.getData().add(data);
            //thread = new addItemThread(seriePratique, x, y);
        }
        
        //thread.start();       
    }
    
    @FXML
    private void handleMenuItem_New(ActionEvent event) throws IOException
    {
        //FXMLNewChart_ViewController a = new FXMLNewChart_ViewController();
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLNewChart_View.fxml"));

        Stage stage = new Stage();

        stage.setScene(new Scene(root, 450, 450));

        stage.show();

        
    }
    
    //@FXML
    //TextField XaxisLabel, YaxisLabel;
    
    
    @FXML
    private void ChartButton_handle(ActionEvent event)
    {
        //XaxisLabel.getText();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //AreaChart1.getXAxis().setAutoRanging(true);

        //serieTheoriqueData.add( new AreaChart.Data(0, 0));
        serieTheorique.setName("Serie1");
        
        //seriePratiqueData.add( new AreaChart.Data(0, 0));
        seriePratique.setName("Serie2");
        
        serieTheorique.setData(serieTheoriqueData);
        seriePratique.setData(seriePratiqueData);
        
        areaChartData.add(serieTheorique);
        areaChartData.add(seriePratique);
        
        NumberAxis xAxis = new NumberAxis("X Values", 1.0d, 9.0d, 2.0d);
        NumberAxis yAxis = new NumberAxis("Y Values", 0.0d, 10.0d, 2.0d);
        
        
        
        RPMROUE_Chart.getData().addAll(0, areaChartData);
        RPMROUE_Chart.getXAxis().setLabel("This is X");
        RPMROUE_Chart.getYAxis().setLabel("This is Y");
        RPMROUE_Chart.getXAxis().animatedProperty().set(false);
//        for(int i = 0; i < 600; i++)
//        {
//           addItem(1, i,i * i/1.68);
//           addItem(2, i, i*i/2);
//        }
        t = new addItemThread(serieTheorique, 1, 1);
        
    }     
}
