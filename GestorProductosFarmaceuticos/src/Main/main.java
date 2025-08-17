package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class main extends Application{
    
    @Override
    public void start(Stage stage) throws Exception{
        //Cargo la vista de fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/mainView.fxml"));
        
        //Creo la escena
        Scene scene = new Scene(loader.load()); //Utilizo el metodo que viene con FXMLLoader
        
        //Seteo la escena en el stage
        stage.setScene(scene);
        
        //Muestro
        stage.show();
    }
    
    public static void main (String[] args){
            Application.launch();
    }
    
}

