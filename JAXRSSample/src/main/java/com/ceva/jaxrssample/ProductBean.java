/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.jaxrssample;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Clase que representa los datos que vamos a compartir
 * @author Test
 */

@XmlRootElement
public class ProductBean {
    public int id_product;
    public String name;
    
    public ProductBean() {
    }
    
    public ProductBean(int id_product, String name) {
        this.id_product = id_product;
        this.name = name;
    }
}

