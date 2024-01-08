/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ceva.store;

/**
 * Interface que encapsula los RequestParameters
 */
public interface Parameters {
    public String getString(String name);
    public int getInt(String name, int defaultValue);
    public double getDouble(String name, double defaultValue);
    public void setInt(String name, int value);
    public void setString(String name, String value);
}
