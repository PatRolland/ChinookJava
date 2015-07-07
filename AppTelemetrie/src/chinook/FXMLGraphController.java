/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chinook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;

/**
 * FXML Controller class
 *
 * @author club
 */
public class FXMLGraphController implements Initializable {


    
    private FXMLDocumentController mainWindow;
    ObservableList<AreaChart.Series<Double, Double>> areaChartData = FXCollections.observableArrayList();    
    @FXML AreaChart GraphChart;
    
  
    Data DataXTemp = null;
    Data DataYTemp = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphChart.setData(areaChartData);
        
        
        areaChartData.add(new AreaChart.Series<Double, Double>());
        areaChartData.get(0).setName("Serial Values");
    }
    
    public void addItem(Data data1, Data data2)
    {
        for(AreaChart.Series<Double, Double> s : areaChartData)
        {
            if(data1.getType().compareTo(getTypeX()) == 0)
                if(data2.getType().compareTo(getTypeY()) == 0)
                    s.getData().add(new AreaChart.Data<>(data1.getValue(), data2.getValue()));
            if(data2.getType().compareTo(getTypeX()) == 0)
                if(data1.getType().compareTo(getTypeY()) == 0)
                    s.getData().add(new AreaChart.Data<>(data2.getValue(), data1.getValue()));
        }
        
    }
    
    public void addItem(double i1, double i2)
    {
        areaChartData.get(0).getData().add(new AreaChart.Data<>(i1, i2));
    }
    
    private void addItem()
    {
        areaChartData.get(0).getData().add(new AreaChart.Data<>(DataXTemp.getValue(), DataYTemp.getValue()));
        DataXTemp = null;
        DataYTemp = null;
    }
    
    public void setPosition(int x, int y)
    {
        
    }
    
    public void setTempX(Data data)
    {
        DataXTemp = new Data(data.getType(), data.getValue());
        if(DataYTemp != null)
            addItem();        
    }
    public void setTempY(Data data)
    {
        DataYTemp = new Data(data.getType(), data.getValue());
        if(DataXTemp != null)
            addItem();        
    }
    
    public void setMainWindow(FXMLDocumentController mainWindow){
        this.mainWindow = mainWindow;
    }
    
    public void setTitle(String title)
    {
        GraphChart.setTitle(title);
    }
    public void setXaxisTitle(String title)
    {
        GraphChart.getXAxis().setLabel(title);
    }
    public void setYaxisTitle(String title)
    {
        GraphChart.getYAxis().setLabel(title);
    }
    public String getTypeX()
    {
        return GraphChart.getXAxis().getLabel();
    }
    public String getTypeY()
    {
        return GraphChart.getYAxis().getLabel();
    }  
    
    @FXML public void clearData(ActionEvent event)
    {
        areaChartData.remove(0, areaChartData.size());
    }
    
}
