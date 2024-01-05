/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
/**
 * Clase que se basa en un HttpServletRequest para obtener la informacion
 */
public class RequestParameters implements Parameters {
    HttpServletRequest request;
    // coleccion que nos permite guardar parametros
    Map<String,String> extraParams;

    public RequestParameters(HttpServletRequest request) {
        this.request = request;
        extraParams = new HashMap<>();
    }
    
    @Override
    public String getString(String name) {
        // nos fijamos si el valor esta en extraParams
        String v = extraParams.get(name);
        if (v == null)
            v = request.getParameter(name);
        return v;
    }

    @Override
    public int getInt(String name, int defaultValue) {
        String v = getString(name);
        // validamo si obtenemos un valor
        if ((v != null) && (v.length() > 0)) {
            // tratamos de convertirlo a int
            try {
                int i = Integer.parseInt(v);
                return i;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    @Override
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
        // convertimos el valor a String
        extraParams.put(name, String.valueOf(value));
    }
    
    public void setString(String name, String value) {
        extraParams.put(name, value);
    }
}
