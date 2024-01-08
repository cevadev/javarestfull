/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
/**
 * Clase que representa un objeto o registro product
 */
public class ProductData {
    public int id_product = -1;
    public String name;
    // indicamos que se ignora a getPrice como propiedad pero double price sera la propiedad
    @JsonProperty
    public double price;
    public String description;
    @JsonIgnore
    public Map<String,String> errors;
    
    @JsonIgnore
    public String getPrimaryKey() {
        // retornamos vacio cuando id_product es -1
        return id_product != -1 ? String.valueOf(id_product) : "";
    }
    
    public String getName() {
        return name != null ? name : "";
    }
    
    // indicamos que no tome a getPrice() como una propiedad del objeto
    @JsonIgnore
    public String getPrice() {
        return String.format(Locale.US, "%.2f", price);
    }
    
    public String getDescription() {
        return description != null ? description : "";
    }
    
    // mensajes de error en el formulario de captura de datos del product
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
