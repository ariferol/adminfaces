/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.listener;

import java.util.Enumeration;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Session create yada destroyda yapilacak isler icin kullanilabilir.
 *
 * @author aerol
 */
@Stateless
@LocalBean
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("SampleKey", UUID.randomUUID().toString());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            Enumeration<String> attrs = se.getSession().getAttributeNames();
            while (attrs.hasMoreElements()) {
                String key = attrs.nextElement();
                if (key.contains("SampleKey")) { // kilitli vakalari kaldir.
                    System.out.println("Session destroyda kaldirilan attribute : " + key);
                    se.getSession().removeAttribute("SampleKey");
                }
            }

        } catch (Exception e) {
            Logger.getLogger(SessionListener.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
