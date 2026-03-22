package com.shoppingcart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ShoppingCartApp {
    private static final String BUNDLE_NAME = "MessagesBundle";
    private ResourceBundle messages;
    private BufferedReader reader;

    public ShoppingCartApp(Locale locale, BufferedReader reader) {
        this.messages = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        this.reader = reader;
    }

    public String getMessage(String key) {
        return messages.getString(key);
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        reader.close();
    }

    public static Locale selectLanguage(BufferedReader br) throws IOException {
        System.out.println("Select language / Vali kieli / Valja sprak / 言語を選択:");
        System.out.println("1. English");
        System.out.println("2. Suomi (Finnish)");
        System.out.println("3. Svenska (Swedish)");
        System.out.println("4. 日本語 (Japanese)");
        
        String choice = br.readLine();
        if (choice == null) {
            return new Locale("en", "US");
        }
        
        switch (choice.trim()) {
            case "2":
                return new Locale("fi", "FI");
            case "3":
                return new Locale("sv", "SE");
            case "4":
                return new Locale("ja", "JP");
            default:
                return new Locale("en", "US");
        }
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Locale locale = selectLanguage(br);
            ShoppingCartApp app = new ShoppingCartApp(locale, br);
            
            String numItemsPrompt = app.getMessage("prompt.num.items");
            System.out.println(numItemsPrompt);
            
            String input = app.readLine();
            int numItems = Integer.parseInt(input.trim());
            
            List<Double> itemCosts = new ArrayList<>();
            
            for (int i = 1; i <= numItems; i++) {
                String pricePrompt = app.getMessage("prompt.item.price") + " " + i + ": ";
                System.out.print(pricePrompt);
                String priceStr = app.readLine();
                double price = Double.parseDouble(priceStr.trim());
                
                String quantityPrompt = app.getMessage("prompt.item.quantity") + " " + i + ": ";
                System.out.print(quantityPrompt);
                String quantityStr = app.readLine();
                int quantity = Integer.parseInt(quantityStr.trim());
                
                double itemCost = calculateItemCost(price, quantity);
                itemCosts.add(itemCost);
            }
            
            double totalCost = calculateTotalCost(itemCosts);
            
            String totalLabel = app.getMessage("label.total.cost");
            System.out.println(totalLabel + ": " + totalCost);
            
            app.close();
            
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }
    }

    public static double calculateItemCost(double price, int quantity) {
        return price * quantity;
    }

    public static double calculateTotalCost(List<Double> itemCosts) {
        return itemCosts.stream()
                .collect(Collectors.summingDouble(Double::doubleValue));
    }
}
