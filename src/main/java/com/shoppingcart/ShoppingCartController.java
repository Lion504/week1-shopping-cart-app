package com.shoppingcart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ShoppingCartController {
    private LocalizationService localizationService;
    private CartService cartService;
    private Map<String, String> localizedStrings;
    private String currentLanguage = "en";
    
    @FXML private ChoiceBox<String> languageChoice;
    @FXML private TextField numItemsField;
    @FXML private Button createItemsButton;
    @FXML private Button calculateButton;
    @FXML private Label totalCostValue;
    @FXML private VBox itemsContainer;
    @FXML private VBox itemTotalsContainer;
    
    @FXML private Label appTitleLabel;
    @FXML private Label languageLabel;
    @FXML private Label numItemsLabel;
    @FXML private Label totalCostLabel;
    @FXML private Label footerLabel;
    @FXML private Label errorLabel;
    
    private List<TextField> priceFields = new ArrayList<>();
    private List<TextField> quantityFields = new ArrayList<>();
    
    @FXML
    public void initialize() {
        localizationService = new LocalizationService();
        cartService = new CartService();
        localizedStrings = localizationService.getLocalizationStrings(currentLanguage);
        
        if (localizedStrings.isEmpty()) {
            System.err.println("Warning: No localization strings loaded. Using keys as fallback.");
        }
        
        ObservableList<String> languages = FXCollections.observableArrayList(
            "English", "Suomi", "Svenska", "日本語", "العربية"
        );
        languageChoice.setItems(languages);
        languageChoice.setValue("English");
        
        updateLabels();
        clearError();
        
        languageChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                changeLanguage(newVal);
            }
        });
    }
    
    private void clearError() {
        errorLabel.setText("");
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
    }
    
    private void updateLabels() {
        appTitleLabel.setText(getMessage("app.title"));
        languageLabel.setText(getMessage("label.language"));
        numItemsLabel.setText(getMessage("prompt.num.items"));
        numItemsField.setPromptText(getMessage("prompt.num.items"));
        createItemsButton.setText(getMessage("button.create"));
        calculateButton.setText(getMessage("button.calculate"));
        totalCostLabel.setText(getMessage("label.total.cost"));
        footerLabel.setText(getMessage("label.footer"));
    }
    
    private void changeLanguage(String language) {
        String dbLanguage;
        switch (language) {
            case "Suomi":
                dbLanguage = "fi";
                break;
            case "Svenska":
                dbLanguage = "sv";
                break;
            case "日本語":
                dbLanguage = "ja";
                break;
            case "العربية":
                dbLanguage = "ar";
                break;
            default:
                dbLanguage = "en";
        }
        currentLanguage = dbLanguage;
        localizedStrings = localizationService.getLocalizationStrings(dbLanguage);
        
        boolean isRtl = dbLanguage.equals("ar");
        
        Platform.runLater(() -> {
            itemsContainer.setNodeOrientation(isRtl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
            itemTotalsContainer.setNodeOrientation(isRtl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT);
            updateLabels();
            totalCostValue.setText("0.00");
        });
    }
    
    private String getMessage(String key) {
        return localizedStrings.getOrDefault(key, key);
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
        clearError();
        itemsContainer.getChildren().clear();
        itemTotalsContainer.getChildren().clear();
        priceFields.clear();
        quantityFields.clear();
        
        int numItems;
        try {
            String text = numItemsField.getText();
            if (text == null || text.trim().isEmpty()) {
                showError(getMessage("error.empty.input"));
                numItems = 1;
            } else {
                numItems = Integer.parseInt(text);
                if (numItems <= 0) {
                    showError(getMessage("error.invalid.number"));
                    numItems = 1;
                } else if (numItems > 100) {
                    showError(getMessage("error.max.items"));
                    numItems = 100;
                }
            }
        } catch (NumberFormatException e) {
            showError(getMessage("error.invalid.number"));
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
        if (priceFields.isEmpty()) {
            showError(getMessage("error.no.items"));
            return;
        }
        
        clearError();
        double total = 0;
        itemTotalsContainer.getChildren().clear();
        
        for (int i = 0; i < priceFields.size(); i++) {
            double price = 0;
            int quantity = 0;
            boolean hasError = false;
            
            try {
                String priceText = priceFields.get(i).getText();
                if (priceText != null && !priceText.isEmpty()) {
                    price = Double.parseDouble(priceText);
                    if (price < 0) {
                        showError(getMessage("error.negative.price"));
                        hasError = true;
                    }
                } else {
                    showError(getMessage("error.empty.field"));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                showError(getMessage("error.invalid.price"));
                hasError = true;
            }
            
            try {
                String quantityText = quantityFields.get(i).getText();
                if (quantityText != null && !quantityText.isEmpty()) {
                    quantity = Integer.parseInt(quantityText);
                    if (quantity < 0) {
                        showError(getMessage("error.negative.quantity"));
                        hasError = true;
                    }
                } else {
                    showError(getMessage("error.empty.field"));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                showError(getMessage("error.invalid.quantity"));
                hasError = true;
            }
            
            if (!hasError) {
                double itemTotal = ShoppingCartApp.calculateItemCost(price, quantity);
                total += itemTotal;
                
                Label itemTotalLabel = new Label(getMessage("label.item.number") + " " + (i + 1) + ": " + String.format("%.2f", itemTotal));
                itemTotalsContainer.getChildren().add(itemTotalLabel);
            }
        }
        
        totalCostValue.setText(String.format("%.2f", total));
        
        List<Double> prices = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<Double> subtotals = new ArrayList<>();
        
        for (int i = 0; i < priceFields.size(); i++) {
            try {
                double price = Double.parseDouble(priceFields.get(i).getText());
                int quantity = Integer.parseInt(quantityFields.get(i).getText());
                double subtotal = ShoppingCartApp.calculateItemCost(price, quantity);
                
                prices.add(price);
                quantities.add(quantity);
                subtotals.add(subtotal);
            } catch (NumberFormatException e) {
            }
        }
        
        cartService.saveCart(priceFields.size(), total, currentLanguage, prices, quantities, subtotals);
    }
}