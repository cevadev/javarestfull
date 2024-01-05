/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ceva.store;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author Test
 */
@WebServlet(name = "RESTProduct", urlPatterns = {"/product/*"})
public class RESTProduct extends HttpServlet {

    /**
     * Convierte a json los datos enviados
     * @param data -> contiene la informacion a convertir
     * @return 
     */
     private String toJSON(List<ProductData> data) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder res = factory.createArrayBuilder();

        for (ProductData pd : data) {
            JsonObject prod = factory.createObjectBuilder()
                    .add("id_product", pd.id_product)
                    .add("name", pd.getName())
                    .add("description", pd.getDescription())
                    .add("price", pd.getPrice())
                    .build();
            res.add(prod);
        }
        return res.build().toString();
    }
    
     /**
      * ahora el id del producto sera parte del url http://localhost:8080/products/1 
      */
    private int parseId_product(String pathInfo) {
        if ((pathInfo == null) || (pathInfo.length() == 0))
            return -1;
        
        String str;
        // obtenemos el valor despues del simbolo /, que representa al id del producto
        int idx = pathInfo.indexOf('/', 1);
        if (idx > 0)
            str = pathInfo.substring(1, idx);
        else
            str = pathInfo.substring(1);
        try {
            int id_product = Integer.parseInt(str);
            return id_product;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // obtenemos el formato a utilizar
        String accept = request.getHeader("accept");
        // si useJson true, generamos la respuesta en Json de lo contrario en html
        boolean useJson = "application/json".equals(accept);
        Parameters params = new RequestParameters(request);
        
        int id_product = parseId_product(request.getPathInfo());
        
        String action = request.getParameter("action");
        if ("del".equals(action)) {
            doDelete(request, response);
            return;
        }
        
        ProductModel model = new ProductModel();
        // validamos si hay un id_product
        if (id_product == -1) {
            // no tenemos product, entonces obtenemos la lista de productos sin paginacion
            List<ProductData> data = model.loadData(params, !useJson);
            // retorno de la informacion
            if (useJson) {
                // retornamos en formato json
                // establecemos el tipo de dato a entregar
                response.setContentType("application/json");
                // convertimos a JSON la informacion
                response.getOutputStream().println(toJSON(data));
            } else {
                // funcionalidad de la lista de productos en html
                request.setAttribute("data", data);
                request.setAttribute("pageBar", PageBarBuilder.getPageBar("product", model.getProductCount(), model.getPageSize(), model.getCurPage()));
                
                request.getRequestDispatcher("/abc_productList7.jsp").forward(request, response);
            }
        } else {
            // si es diferente a -1 lo que queremos es un product
            ProductData data = model.loadProduct(id_product);
            if (data != null) {
                if (useJson) {
                    // TODO: Tarea
                } else {
                    request.setAttribute("data", data);
                    request.getRequestDispatcher("/abc_product7.jsp").forward(request, response);
                }
            }
       }
    }

    // metodo que se invoca cuando se agrega un nuevo registro
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Parameters params = new RequestParameters(request);
        boolean useJson = "application/json".equals(request.getHeader("accept"));
        
        if (useJson) {
            JsonParser parser = Json.createParser(request.getReader());
            String key = null;
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case KEY_NAME:
                        key = parser.getString();
                        break;
                    case VALUE_STRING:
                        if ("name".equals(key) || "description".equals(key)) {
                            params.setString(key, parser.getString());
                        }
                        break;
                    case VALUE_NUMBER:
                        if ("price".equals(key))
                            params.setString(key, String.valueOf(parser.getBigDecimal().doubleValue()));
                        break;
                }
            }
            parser.close();
        }
        
        ProductModel model = new ProductModel();
        boolean success = model.save(params);
        
        if (useJson) {
            response.setContentType("application/json");
            if (success) {
                int id_product = params.getInt("id_product", -1);
                response.getOutputStream().println("{\"result\":\"success\",\"id_product\":" + id_product + "}");
            } else {
                response.getOutputStream().println("{\"result\":\"error\"}");
            }
        } else {
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product");
            } else {
                response.getOutputStream().println("<html><body>error</body></html>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Parameters params = new RequestParameters(request);
        boolean useJson = "application/json".equals(request.getHeader("accept"));
        boolean success;
        
        int id_product = parseId_product(request.getPathInfo());
        
        if (id_product == -1) {
            // insertamos el nuevo producto (para insert es PUT, para update es POST)
            doPut(request, response);
            return;
        } else {
            
            params.setInt("id_product", id_product);
            if (useJson) {
                JsonParser parser = Json.createParser(request.getReader());
                String key = null;
                while (parser.hasNext()) {
                    JsonParser.Event event = parser.next();
                    switch (event) {
                        case KEY_NAME:
                            key = parser.getString();
                            break;
                        case VALUE_STRING:
                            if ("name".equals(key) || "description".equals(key)) {
                                params.setString(key, parser.getString());
                            }
                            break;
                        case VALUE_NUMBER:
                            if ("price".equals(key))
                                params.setString(key, String.valueOf(parser.getBigDecimal().doubleValue()));
                            break;
                    }
                }
                parser.close();
            }
            // actualizamos el producto
            ProductModel model = new ProductModel();
            // En params se encuentra informacio del product
            success = model.save(params);
        }
        
        if (useJson) {
            // manejamos la info de json
            response.setContentType("application/json");
            if (success) {
                response.getOutputStream().println("{\"result\":\"success\"}");
            } else {
                response.getOutputStream().println("{\"result\":\"error\"}");
            }
        } else {
            // manejamos la info html
            if (success) {
                response.sendRedirect(request.getContextPath() + "/product");
            } else {
                response.getOutputStream().println("<html><body>error</body></html>");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success;
        
        int id_product = parseId_product(request.getPathInfo());
        if (id_product == -1)
            success = false;
        else {
            ProductModel model = new ProductModel();
            success = model.delete(id_product);
        }
        
        boolean useJson = "application/json".equals(request.getHeader("accept"));
        if (useJson) {
            response.setContentType("application/json");
            if (success) {
                response.getOutputStream().println("{\"result\":\"success\"}");
            } else {
                response.getOutputStream().println("{\"result\":\"error\"}");
            }
        } else {
            if (success) {
                response.sendRedirect("product");
            } else {
                response.getOutputStream().println("<html><body>error</body></html>");
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "REST demo";
    }
}
