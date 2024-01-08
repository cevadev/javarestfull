/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Clase que ofrece la misma funcionalidad de la clase RestProduct pero implementamos JAX-RS
 */
@Path("/product")
public class RESTProduct2 {
/** MANEJAMOS METODOS PARA PROCESAR JSON **/    
    @GET
    @Produces("application/json")
    public List<ProductData> getProducts() {
        return new ProductModel().loadData(null, false);
    }
    
    @GET
    @Path("{id_product}")
    @Produces("application/json")
    // indicamos que el metodo getProduct() va a tomar el id_product del Path
    public ProductData getProduct(@PathParam("id_product") int id_product) {
        return new ProductModel().loadProduct(id_product);
    }
    
    // Guardamos un nuevo producto. variable in representa el json que envia el client
    @PUT
    @Consumes("application/json") // enviamos los datos que se van a guardar
    @Produces("application/json")
    public String newProduct(InputStream in) throws Exception {
        // podemos generar un json a partir de un map
        Map<String,Object> res = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductData data = mapper.readValue(in, ProductData.class);
            ProductModel model = new ProductModel();
            if (model.save(data)) {
                // armamos la respuesta al client
                res.put("result", "success");
                res.put("id_product", data.id_product);
            } else {
                res.put("result", "error");
            }
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
            res.put("result", "error");
        }
        
        return mapper.writeValueAsString(res);
    }

    // guardamos un producto existente
    @POST
    @Path("{id_product}")
    @Consumes("application/json")
    @Produces("application/json")
    public String saveProduct(@PathParam("id_product") int id_product, InputStream in) throws Exception {
        Map<String,Object> res = new HashMap<>();
        
        ObjectMapper mapper = new ObjectMapper();
        ProductData data = mapper.readValue(in, ProductData.class);
        ProductModel model = new ProductModel();
        if (model.save(data))
            res.put("result", "success");
        else
            res.put("result", "error");

        return mapper.writeValueAsString(res);
    }
    
    // eliminamos un producto. No usamos variable InputStream xq solo necesitamos el id_product
    @DELETE
    @Path("{id_product}")
    @Produces("application/json")
    public String deleteProduct(@PathParam("id_product") int id_product) throws Exception {
        Map<String,Object> res = new HashMap<>();
        
        ObjectMapper mapper = new ObjectMapper();
        ProductModel model = new ProductModel();
        if (model.delete(id_product))
            res.put("result", "success");
        else
            res.put("result", "error");
        
        return mapper.writeValueAsString(res);
    }

/** MANEJAMOS METODOS PARA PROCESAR INFORMACION EN HTML **/
    @GET
    @Produces("text/html")
    public void getHtmlProducts(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {
        Parameters params = new RequestParameters(request);
        ProductModel model = new ProductModel();
        List<ProductData> data = model.loadData(params, true);
        request.setAttribute("data", data);
        request.setAttribute("pageBar", PageBarBuilder.getPageBar("product", model.getProductCount(), model.getPageSize(), model.getCurPage()));

        request.getRequestDispatcher("/abc_productList7.jsp").forward(request, response);
    }
    
    @GET
    @Path("{id_product}")
    @Produces("text/html")
    public void getHtmlProduct(@PathParam("id_product") int id_product, 
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response) throws ServletException, IOException {
        ProductModel model = new ProductModel();
        if ("del".equals(request.getParameter("action"))) {
            if (model.delete(id_product))
                response.sendRedirect(request.getContextPath() + "/rest/product");
        }
        ProductData data = model.loadProduct(id_product);
        if (data != null) {
            request.setAttribute("data", data);
            request.getRequestDispatcher("/abc_product7.jsp").forward(request, response);
        }
    }
    
/*    
    @POST
    @Produces("text/html")
    public void saveProduct(MultivaluedMap<String, String> formParams,
                            @Context HttpServletRequest request,
                            @Context HttpServletResponse response) throws ServletException, IOException {
        Parameters params = new MultiValuedParameters(formParams);
        ProductModel model = new ProductModel();
        if (model.save(params)) {
            response.sendRedirect(request.getContextPath() + "/rest/product");
        }
    }
    
    @POST
    @Path("{id_product}")
    @Produces("text/html")
    public void saveProduct(@PathParam("id_product") int id_product,
                            MultivaluedMap<String,String> formParams,
                            @Context HttpServletRequest request,
                            @Context HttpServletResponse response) throws ServletException, IOException {
        Parameters params = new MultiValuedParameters(formParams);
        params.setInt("id_product", id_product);
        ProductModel model = new ProductModel();
        if (model.save(params)) {
            response.sendRedirect(request.getContextPath() + "/rest/product");
        }
    }
*/
}