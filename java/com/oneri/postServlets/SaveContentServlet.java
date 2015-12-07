package com.oneri.postServlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Gaby on 17/10/2015.
 */
public class SaveContentServlet extends javax.servlet.http.HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("You called the get method on the SaveContentServlet");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Retrieve informations from the URL
        String title = req.getParameter("title");
        String contentType = req.getParameter("contentType");
        String creator = req.getParameter("creator");
        String imageURL = req.getParameter("imageURL");
        String description = req.getParameter("description");
        String commercialLink = req.getParameter("commercialLink");

        if (!(contentType.equals("movie") || contentType.equals("series") && contentType.equals("comic")
                || contentType.equals("video game") || contentType.equals("book") || contentType.equals("music")) ){
            resp.getWriter().println(contentType + " n'est pas un type valide");
            return;
        }

        // Take a reference of the datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        // Generate or retrieve the key associated with an existent contact
        // Create or modify the entity associated with the contact
        Entity content;

        //This line means that the title and contentType are assembled to make the key
        content = new Entity("Content", title + contentType);
        content.setProperty("Title", title);
        content.setProperty("ContentType", contentType);
        content.setProperty("Creator", creator);
        content.setProperty("ImageURL", imageURL);
        content.setProperty("Description", description);
        content.setProperty("CommercialLink", commercialLink);

        //permet de vérifier si aucun champ n'est nul
        if (checkContact(content, resp)) {
            return;
        }

        // Save in the Datastore
        datastore.put(content);
        resp.getWriter().println("Content" + title + " saved with key " +
                KeyFactory.keyToString(content.getKey()));

        //Go to appengine.google.com to see the DB

    }

    public boolean checkContact(Entity entity, HttpServletResponse resp) throws IOException {


        Map<String, Object> map = entity.getProperties();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String) map.get(key);
            if (value == null) {
                resp.getWriter().println(key + " est null so " + entity.getProperty("Title") + " cannot be saved in the database");
                return true;
            }
        }

        return false;
    }


}
