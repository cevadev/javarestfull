/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 *
 * @author Test
 */
@Path("/test")
public class Test {

    @GET
    @Produces("text/html")
    public String getHtml() {
        return "<html><body>Hello world</body></html>";
    }
    
}