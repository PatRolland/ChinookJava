/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chinook;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.comm.CommPortIdentifier;

/**
 *
 * @author club
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML SplitMenuButton BaudRateButton;
    @FXML SplitMenuButton StopBitsButton;
    @FXML SplitMenuButton XGraphButton;
    @FXML SplitMenuButton YGraphButton;
    @FXML TextField GraphNameTextField;
    @FXML Button AddGraphButton;
    @FXML PieChart testPieChart;
    @FXML Label lbGear;
    
    @FXML public void handleBaudRate(ActionEvent event){ BaudRateButton.setText( ((MenuItem)event.getSource()).getText() ); }
    @FXML public void handleStopBits(ActionEvent event){ StopBitsButton.setText( ((MenuItem)event.getSource()).getText() ); }
    @FXML public void handleXGraphItem(ActionEvent event){ XGraphButton.setText( ((MenuItem)event.getSource()).getText() ); }
    @FXML public void handleYGraphItem(ActionEvent event){ YGraphButton.setText( ((MenuItem)event.getSource()).getText() ); }
    
    SerialCommunication serialComm;
    static CommPortIdentifier portId;
    static Enumeration portList;
    
    DataLogger dataLogger;
    
    ArrayList<FXMLGraphController> Graphs;
    
    //Gauge datas
    ObservableList<PieChart.Data> testPieChartData = FXCollections.observableArrayList();
    
    @FXML public void handleAddGraphButton(ActionEvent event)throws IOException {
        addGraph(GraphNameTextField.getText(), XGraphButton.getText(), YGraphButton.getText());
    }
    
    @FXML public void startSerialComm(ActionEvent event)
    {
        //<editor-fold desc="Serial Comm Init">
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM12")) {
			//                if (portId.getName().equals("/dev/term/a")) {
                        serialComm = new SerialCommunication(portId, dataLogger);
                 }
            }
        }
        //</editor-fold>
    }
    @FXML public void stopSerialComm(ActionEvent event)
    {
        serialComm.close();
    }
    
    public FXMLGraphController addGraph(String title, String xAxisTitle, String yAxisTitle) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLGraph.fxml"));
        AnchorPane newWindow = (AnchorPane)loader.load();
        FXMLGraphController controller = loader.getController();
        controller.setMainWindow(this);
        controller.setTitle(title);
        controller.setXaxisTitle(xAxisTitle);
        controller.setYaxisTitle(yAxisTitle);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);        
        Scene scene = new Scene(newWindow);
        stage.setScene(scene);
        stage.show();
        
        Graphs.add(controller);
        
        return controller;
    }
    
    public void updateData(Data data)//not used?
    {
        for(FXMLGraphController c : Graphs)
        {
            if(c.getTypeX().compareTo(data.getType()) == 0)
                c.setTempX(data);
            if(c.getTypeY().compareTo(data.getType()) == 0)
                c.setTempY(data);
        }
        
        if(data.getType().compareTo("Gear") == 0)
        {
            testPieChart.getData().get(1).setPieValue(data.getValue());
            testPieChart.getData().get(2).setPieValue(14-data.getValue());
            lbGear.setText(data.getValueString());
        }
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Graphs = new ArrayList<>();
        
        //<editor-fold desc="DataLogger Init">
        dataLogger = new DataLogger(this);
        //</editor-fold>
        
        //<editor-fold desc="Add default graphs">
        try {            
            addGraph("Graph1", "Temps", "Temps");  
            addGraph("Graph2", "Temps", "Power");
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        testPieChart.setData(testPieChartData);
        testPieChartData.add(new PieChart.Data(null, 5));
        testPieChartData.add(new PieChart.Data(null, 7));
        testPieChartData.add(new PieChart.Data(null, 7));
        testPieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #FFFFFF;");
        testPieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #00FF00;");
        testPieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #FF0000;");
        testPieChart.setRotate(43);
        
    }    
    
}
