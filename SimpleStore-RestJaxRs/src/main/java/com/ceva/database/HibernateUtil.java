/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
/**
 *
 * @author Test
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    private static void createSessionFactory() {
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

        sessionFactory = new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();        
    }
    
    public static Session newSession() throws HibernateException {
        if (sessionFactory == null)
            createSessionFactory();
        return sessionFactory.openSession();
    }
}
