/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.Misc;
import com.ceva.database.DBUtil;
import com.ceva.database.HibernateUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Test
 */
public class CatProduct6 extends CatHandler {
    private static final Map<String,Integer> productMapIndex;
    static {
        productMapIndex = new HashMap<>();
        productMapIndex.put("id_product", 0);
        productMapIndex.put("name", 1);
        productMapIndex.put("description", 2);
        productMapIndex.put("price", 3);
    }

    public CatProduct6() {
        super();
        mapIndex = productMapIndex;
    }
    
    @Override
    boolean doSave() throws IOException {
        // leemos datos y validaciones
        boolean error = false;
        int id_product = parseInt(request.getParameter("id_product"), -1);
        String name = request.getParameter("name");
        if ((name == null) || (name.trim().length() == 0)) {
            setError("name", "Falta el nombre del producto.");
            error = true;
        }
        String description = request.getParameter("description");
        double price = 0;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            setError("price", "N&uacute;mero inv&aacute;lido.");
            error = true;
        }

        if (!error) {
            Product product;
            try (Session session = HibernateUtil.newSession()) {
                Transaction tx = session.beginTransaction();
                
                if (id_product != -1)
                    product = session.get(Product.class, id_product);
                else
                    product = new Product();
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                session.save(product);
                //if (id_product == -1)
                    // insert & update  
                    // session.save(product);
                
                tx.commit();
                response.sendRedirect("abc_productList6.jsp");
                return true;
            }
        } else {
            setInteger("id_product", id_product);
            setString("name", nullToEmpty(name));
            setString("description", nullToEmpty(description));
            setDouble("price", price);
        }
        editFlag = true;
        return false;
    }
    
    @Override
    boolean doEdit() {
        editFlag = true;
        int id_product = parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            try (Session session = HibernateUtil.newSession()) {
                Product product = (Product) session.get(Product.class, id_product);
                
                setInteger("id_product", product.getId_product());
                setString("name", product.getName());
                setString("description", product.getDescription());
                setDouble("price", product.getPrice());
            }
        } else {
            setInteger("id_product", -1);
            setString("name", "");
            setString("description", "");
            setDouble("price", 0d);
        }
        return false;
    }
    
    @Override
    boolean doDelete() throws IOException {
        int id_product = parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            try (Session session = HibernateUtil.newSession()) {
                Transaction tx = session.beginTransaction();
                
                // 1. forma
                //Product p = session.get(Product.class, id_product);
                //session.delete(p);
                
                // 2. forma
                session.createQuery("delete from Product where id_product=:id_product")
                       .setParameter("id_product", id_product)
                       .executeUpdate();
                
                tx.commit();
            }
        }
        response.sendRedirect("abc_productList6.jsp");
        return true;
    }
    
    @Override
    boolean doList() {
        int curPage = Misc.parseInt(request.getParameter("pg"), 0);
        if (curPage > 0)
            curPage--;
        else if (curPage < 0)
            curPage = 0;
        int pageSize = 10;
        
        try (Session session = HibernateUtil.newSession()) {
            // siempre es bueno acostumbrarnos a hacer una Transaction
            Transaction tx = session.beginTransaction();
            // obtenemos el nro de prdudctos
            int productCount = ((Number) session.createQuery("select count(*) from Product")
                                            .uniqueResult()).intValue();
            // numPages indican el nro de paginas que se generan
            int numPages = ((productCount+pageSize-1)/pageSize);
            // validamos si la pagina que quiero ver es mayor al nro de paginas disponibles
            if (curPage >= numPages)
                curPage = numPages-1;
            
            List<Product> list = session.createQuery("from Product order by id_product")
                    // empezamos a leer desde el elemento curPage*pageSize
                                        .setFirstResult(curPage*pageSize)
                    // leemos un maximo de pageSize elementos
                                        .setMaxResults(pageSize)
                                        .list();
            data = new LinkedList<>();
            for (Product p : list) {
                curData = null;
                setInteger("id_product", p.getId_product());
                setString("name", p.getName());
                setString("description", p.getDescription());
                setDouble("price", p.getPrice());
                data.add(curData);
            }
            
            // armamos la barra de paginacion
            request.setAttribute("pageBar", PageBarBuilder.getPageBar("abc_productList6.jsp", productCount, pageSize, curPage));
            
            tx.commit();
        }
        return false;
    }
    
    public String getPrimaryKey() {
        int id = getInteger("id_product");
        return id != -1 ? String.valueOf(id) : "";
    }
    
    public String asMoney(String name) {
        return String.format(Locale.US, "%.2f", getDouble(name));
    }
    
    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        return super.handleRequest();
    }
}
