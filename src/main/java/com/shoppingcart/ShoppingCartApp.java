package com.shoppingcart;

import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShoppingCartApp extends Application {
    
    public static double calculateItemCost(double price, int quantity) {
        return price * quantity;
    }

    public static double calculateTotalCost(List<Double> itemCosts) {
        return itemCosts.stream()
                .collect(Collectors.summingDouble(Double::doubleValue));
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Shopping Cart - Wang Yongzhi");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}