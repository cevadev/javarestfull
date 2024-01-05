/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * Clase que representa un objeto o registro product
 */
public class ProductData {
    public int id_product = -1;
    public String name;
    public double price;
    public String description;
    public Map<String,String> errors;
    
    public String getPrimaryKey() {
        return id_product != -1 ? String.valueOf(id_product) : "";
    }
    
    public String getName() {
        return name != null ? name : "";
    }
    
    public String getPrice() {
        return String.format(Locale.US, "%.2f", price);
    }
    
    public String getDescription() {
        return description != null ? description : "";
    }
    
    public void setError(String name, String message) {
        if (errors == null)
            errors = new HashMap<>();
        errors.put(name, message);
    }
    
    public String getError(String name) {
        if (errors == null)
            return "";
        String message = errors.get(name);
        return message != null ? message : "";
    }
}
