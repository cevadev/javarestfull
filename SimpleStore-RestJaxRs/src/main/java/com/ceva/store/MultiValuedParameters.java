/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import java.util.List;
import jakarta.ws.rs.core.MultivaluedMap; 
/**
 *
 * Clase que lee a partir de formParams un string
 */

public class MultiValuedParameters extends Parameters {
    MultivaluedMap<String,String> formParams;

    public MultiValuedParameters(MultivaluedMap<String,String> formParams) {
        this.formParams = formParams;
    }
    
    @Override
    public String getString(String name) {
        String v = super.getString(name);
        if (v == null) {
            List<String> tmp = formParams.get(name);
            if ((tmp == null) || (tmp.size() == 0))
                return null;
            return tmp.get(0);
        }
        return v;
    }
    
}