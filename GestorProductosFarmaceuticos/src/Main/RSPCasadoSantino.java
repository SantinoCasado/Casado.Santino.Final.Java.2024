package Main;

import Controllers.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RSPCasadoSantino extends Application{

    @Override
    public void start(Stage stage) throws Exception{
        //Cargo la vista de fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/View.fxml"));
        
        //Creo la escena
        Scene scene = new Scene(loader.load()); //Utilizo el metodo que viene con FXMLLoader
        ViewController controladorVista = loader.getController();
        
        //Seteo la escena en el stage
        stage.setScene(scene);
        
        //Muestro
        stage.setOnCloseRequest(e -> controladorVista.GuardarArchivo());        
        stage.show();
    }
    
    public static void main (String[] args){
            Application.launch();
    }
    
}
