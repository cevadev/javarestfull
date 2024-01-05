/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.database.HibernateUtil;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 * Clase que lee, modifica, elimina productos
 */
public class ProductModel {
    private static final int PAGESIZE = 10; // tamano de pagina
    private int curPage = 0; // pagina actual
    private int productCount = 0; // total de productos
    
    public ProductModel() {
        
    }
    
    public int getPageSize() {
        return PAGESIZE;
    }
    
    public int getCurPage() {
        return curPage;
    }
    
    public int getProductCount() {
        return productCount;
    }
    
    public List<ProductData> loadData(Parameters params) {
        return loadData(params, true);
    }
    
    /**
     * 
     * @param params
     * @param usePaging - indica si utilizar informacion paginada o no paginada
     * @return 
     */
    public List<ProductData> loadData(Parameters params, boolean usePaging) {
        if (usePaging) {
            curPage = params.getInt("pg", 0);
            if (curPage > 0)
                curPage--;
            else if (curPage < 0)
                curPage = 0;
        } else {
            curPage = -1;
        }
        
        List<ProductData> data;
        try (Session session = HibernateUtil.newSession()) {
            if (curPage != -1) {
                // manejamos la paginacion
                productCount = ((Number) session.createQuery("select count(*) from Product")
                                                .uniqueResult()).intValue();
                int numPages = ((productCount+PAGESIZE-1)/PAGESIZE);
                if (curPage >= numPages)
                    curPage = numPages-1;
            }
            
            Query query = session.createQuery("from Product order by id_product");
            if (curPage >= 0)
                query.setFirstResult(curPage*PAGESIZE)
                     .setMaxResults(PAGESIZE);
            // retoramos la info sin paginar, es decir, todos los products
            List<Product> list = query.list();
            data = new LinkedList<>();
            for (Product p : list) {
                ProductData pd = new ProductData();
                pd.id_product = p.getId_product();
                pd.name = p.getName();
                pd.price = p.getPrice();
                pd.description = p.getDescription();

                data.add(pd);
            }
        }
        return data;
    }
    
    public ProductData loadProduct(int id_product) {
        if (id_product == -1)
            return null;
        
        try (Session session = HibernateUtil.newSession()) {
            Product product = (Product) session.get(Product.class, id_product);
            if (product != null) {
                ProductData pd = new ProductData();
                pd.id_product = product.getId_product();
                pd.name = product.getName();
                pd.description = product.getDescription();
                pd.price = product.getPrice();
                return pd;
            }
        }
        return null;
    }
    
    public boolean save(Parameters params) {
        try (Session session = HibernateUtil.newSession()) {
            Transaction tx = session.beginTransaction();
            
            Product product;
            int id_product = params.getInt("id_product", -1);
            if (id_product != -1)
                product = session.get(Product.class, id_product);
            else
                product = new Product();
            product.setName(params.getString("name"));
            product.setPrice(params.getDouble("price", 0));
            product.setDescription(params.getString("description"));
            session.save(product);
            
            if (id_product == -1)
                // aqui ya debemos tener un id que lo obtuvo hibernate al insertar el product
                params.setInt("id_product", product.getId_product());
            
            tx.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
            
            return false;
        }
    }
    
    public boolean delete(int id_product) {
        try (Session session = HibernateUtil.newSession()) {
            Transaction tx = session.beginTransaction();
            
            session.createQuery("delete from Product where id_product=:id_product")
                    .setParameter("id_product", id_product)
                    .executeUpdate();
            
            tx.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
            
            return false;
        }
    }
}
