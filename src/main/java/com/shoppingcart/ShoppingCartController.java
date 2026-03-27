package com.shoppingcart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ShoppingCartController {
    private static final String BUNDLE_NAME = "MessagesBundle";
    private ResourceBundle messages;
    
    @FXML private ChoiceBox<String> languageChoice;
    @FXML private TextField numItemsField;
    @FXML private Button createItemsButton;
    @FXML private Button calculateButton;
    @FXML private Label totalCostLabel;
    @FXML private VBox itemsContainer;
    @FXML private VBox itemTotalsContainer;
    
    private List<TextField> priceFields = new ArrayList<>();
    private List<TextField> quantityFields = new ArrayList<>();
    
    @FXML
    public void initialize() {
        languageChoice.setValue("English");
        languageChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                changeLanguage(newVal);
            }
        });
        updateBundle(new Locale("en", "US"));
    }
    
    private void changeLanguage(String language) {
        Locale locale;
        switch (language) {
            case "Suomi":
                locale = new Locale("fi", "FI");
                break;
            case "Svenska":
                locale = new Locale("sv", "SE");
                break;
            case "日本語":
                locale = new Locale("ja", "JP");
                break;
            case "العربية":
                locale = new Locale("ar", "AR");
                break;
            default:
                locale = new Locale("en", "US");
        }
        updateBundle(locale);
    }
    
    private void updateBundle(Locale locale) {
        messages = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        
        boolean isRtl = locale.getLanguage().equals("ar");
        
        Platform.runLater(() -> {
            itemsContainer.setNodeOrientation(isRtl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
            itemTotalsContainer.setNodeOrientation(isRtl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
            
            numItemsField.setPromptText(getMessage("prompt.num.items"));
            createItemsButton.setText(getMessage("button.create"));
            calculateButton.setText(getMessage("button.calculate"));
            totalCostLabel.setText("0.00");
        });
    }
    
    private String getMessage(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
    
    @FXML
    public void onLanguageChanged() {
        String selected = languageChoice.getValue();
        if (selected != null) {
            changeLanguage(selected);
        }
    }
    
    @FXML
    public void onCreateItems() {
        itemsContainer.getChildren().clear();
        itemTotalsContainer.getChildren().clear();
        priceFields.clear();
        quantityFields.clear();
        
        int numItems;
        try {
            numItems = Integer.parseInt(numItemsField.getText());
            if (numItems <= 0) numItems = 1;
        } catch (NumberFormatException e) {
            numItems = 1;
        }
        
        for (int i = 1; i <= numItems; i++) {
            HBox itemRow = new HBox(10);
            itemRow.setSpacing(10);
            
            Label itemLabel = new Label(getMessage("label.item.number") + " " + i + ":");
            itemLabel.setStyle("-fx-font-weight: bold; -fx-min-width: 80;");
            
            TextField priceField = new TextField();
            priceField.setPromptText(getMessage("prompt.item.price"));
            priceField.setPrefWidth(150);
            
            TextField quantityField = new TextField();
            quantityField.setPromptText(getMessage("prompt.item.quantity"));
            quantityField.setPrefWidth(100);
            
            priceFields.add(priceField);
            quantityFields.add(quantityField);
            
            itemRow.getChildren().addAll(itemLabel, priceField, quantityField);
            itemsContainer.getChildren().add(itemRow);
        }
    }
    
    @FXML
    public void onCalculate() {
        double total = 0;
        itemTotalsContainer.getChildren().clear();
        
        for (int i = 0; i < priceFields.size(); i++) {
            double price = 0;
            int quantity = 0;
            
            try {
                String priceText = priceFields.get(i).getText();
                if (priceText != null && !priceText.isEmpty()) {
                    price = Double.parseDouble(priceText);
                }
            } catch (NumberFormatException e) {
                price = 0;
            }
            
            try {
                String quantityText = quantityFields.get(i).getText();
                if (quantityText != null && !quantityText.isEmpty()) {
                    quantity = Integer.parseInt(quantityText);
                }
            } catch (NumberFormatException e) {
                quantity = 0;
            }
            
            double itemTotal = ShoppingCartApp.calculateItemCost(price, quantity);
            total += itemTotal;
            
            Label itemTotalLabel = new Label(getMessage("label.item.number") + " " + (i + 1) + ": " + String.format("%.2f", itemTotal));
            itemTotalsContainer.getChildren().add(itemTotalLabel);
        }
        
        totalCostLabel.setText(String.format("%.2f", total));
    }
}