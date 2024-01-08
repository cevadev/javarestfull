/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ceva.store;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejamos la funcionalidad comun a todos los parametros.
 * La funcionalidad comun con los parametros extra
 */
public class Parameters {
    private final Map<String,String> extraParams = new HashMap<>();
    
    public String getString(String name) {
        String v = extraParams.get(name);
        return v;
    }
    
    public int getInt(String name, int defaultValue) {
        String v = getString(name);
        if ((v != null) && (v.length() > 0)) {
            try {
                int i = Integer.parseInt(v);
                return i;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public double getDouble(String name, double defaultValue) {
        String v = getString(name);
        if ((v != null) && v.length() > 0) {
            try {
                double d = Double.parseDouble(v);
                return d;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public void setInt(String name, int value) {
        extraParams.put(name, String.valueOf(value));
    }
    
    public void setString(String name, String value) {
        extraParams.put(name, value);
    }
}
