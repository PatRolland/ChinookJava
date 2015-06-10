/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Communication.COM_Comm;
import java.util.Enumeration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.comm.CommPortIdentifier;

/**
 *
 * @author club
 */
public class Test extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        /*Parent root = FXMLLoader.load(getClass().getResource("FXMLMainView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();*/
    }
    
    public void addScene(Scene s) throws Exception
    {
         

    }

    static CommPortIdentifier portId;
    static Enumeration portList;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM12")) {
			//                if (portId.getName().equals("/dev/term/a")) {
                    COM_Comm reader = new COM_Comm();
                }
            }
        }
        
        //launch(args);
        
        
    }
    
}
