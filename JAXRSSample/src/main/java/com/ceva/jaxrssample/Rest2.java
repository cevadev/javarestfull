/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.jaxrssample;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;


@Path("/test2")
public class Rest2 {
 
    /**
     * Producimos informacion en JSON
     */
    @GET
    @Produces("application/json")
    public ProductBean getJson() {
        return new ProductBean(1, "producto");
    }
    
    /**
     * Producimos informacion en html
     */
    @GET
    @Produces("text/html")
    public String getHtml() {
        return "<html><body>1 producto</body></html>";
    }

}
