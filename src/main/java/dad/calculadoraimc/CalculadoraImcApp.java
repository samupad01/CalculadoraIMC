package dad.calculadoraimc;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculadoraImcApp extends Application {
    private DoubleProperty pesoProperty = new SimpleDoubleProperty(0);
    private DoubleProperty alturaProperty = new SimpleDoubleProperty(1);  // Inicializar con 1 para evitar NaN
    private DoubleProperty imcProperty = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
    	//Creación del VBox que contendrá todo.
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        //Creación del HBox que contiene el peso.
        HBox pesoBox = new HBox(10);
        TextField pesoTextField = new TextField();
        pesoTextField.setPromptText("Peso en kg");
        pesoTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                pesoProperty.set(Double.parseDouble(newVal));
            } catch (NumberFormatException e) {
                pesoProperty.set(0);
            }
        });
        pesoBox.getChildren().addAll(new Label("Peso:"), pesoTextField, new Label("kg"));
        pesoBox.setAlignment(Pos.CENTER);
        //Creación del HBox que contiene la altura.
        HBox alturaBox = new HBox(10);
        TextField alturaTextField = new TextField();
        alturaTextField.setPromptText("Altura en cm");
        alturaTextField.textProperty().addListener((o, ol, n) -> {
            try {
                alturaProperty.set(Double.parseDouble(n) / 100);  // Convertir cm a metros
            } catch (NumberFormatException e) {
                alturaProperty.set(1);
            }
        });
        alturaBox.getChildren().addAll(new Label("Altura:"), alturaTextField, new Label("cm"));
        alturaBox.setAlignment(Pos.CENTER);
        //Creación del HBox que contiene el IMC
        HBox imcBox = new HBox(10);
        Label imcLabel = new Label("IMC: ");
        //Salida por pantalla en función de los datos introducidos
        imcLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (pesoProperty.get() == 0 && alturaProperty.get() == 1) {  
                return "IMC: (peso * altura ^2)";
            } else {
                return String.format("IMC: %.2f", imcProperty.get());
            }
        }, imcProperty, pesoProperty, alturaProperty));
        imcBox.getChildren().addAll(imcLabel);
        imcBox.setAlignment(Pos.CENTER);
        //Creación del HBox para la salida del resultado
        HBox resultBox = new HBox(10);
        Label resultLabel = new Label("");
        resultBox.getChildren().addAll(resultLabel);
        resultBox.setAlignment(Pos.CENTER);
        //Bindeo para la automatización del resultado.
        imcProperty.bind(pesoProperty.divide(alturaProperty.multiply(alturaProperty)));
        
        //Bindings en función del resultLabel
        resultLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            if (pesoProperty.get() == 0 || alturaProperty.get() == 1) {
                return "Bajo peso | Normal | Sobrepeso | Obeso";
            } else if (imcProperty.get() < 18.5) {
                return "Bajo peso";
            } else if (imcProperty.get() < 24.9) {
                return "Normal";
            } else if (imcProperty.get() < 29.9) {
                return "Sobrepeso";
            } else {
                return "Obeso";
            }
        }, imcProperty, pesoProperty, alturaProperty));

        root.getChildren().addAll(pesoBox, alturaBox, imcBox, resultBox);
        
        //Creación de la escena y mostrarla.
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Calculadora de IMC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


