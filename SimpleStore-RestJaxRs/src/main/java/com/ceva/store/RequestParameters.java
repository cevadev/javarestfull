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
public class RequestParameters extends Parameters {
    HttpServletRequest request;

    public RequestParameters(HttpServletRequest request) {
        this.request = request;
    }
    
    @Override
    public String getString(String name) {
        // nos fijamos si el valor esta en extraParams
        String v = super.getString(name);
        if (v == null)
            v = request.getParameter(name);
        return v;
    }
}
