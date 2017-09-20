package Cardgame.Core;

import java.io.IOException;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;


public class CardFactory extends Observable {
    private static final Map<String,CardConstructor> map = new HashMap<>();
    private static final CardFactory instance = new CardFactory();
    
    
    private CardFactory() {
        URL url=getClass().getClassLoader().getResource("Cardgame/Cards");
        if (url.toString().startsWith("jar:")) loadCardsFromJar(url);
        else loadCardsFromDir(url);
    }

    private void loadCardsFromJar(URL url) {
        //decoding jar
        String[] jarparts = url.toString().substring("jar:".length()).split("!");

        URL jarurl;
        try {
            jarurl = new URL(jarparts[0]);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("cannot find jar " + jarparts[0]);
        }

        ZipInputStream zip;
        try {
            zip = new ZipInputStream(jarurl.openStream());
        } catch (IOException ex) {
            throw new RuntimeException("cannot read Cards directory from jar");
        }

        ZipEntry entry=null;
        try { entry=zip.getNextEntry(); } catch (IOException ex) {}
        while ( entry != null ) {
            //new entry
            String entryName = entry.getName();
            if (entryName.startsWith("Cardgame/Cards/")) {
                String fileName=entryName.substring("Cardgame/Cards/".length());

                //look for all top level classe in package Cardgame.Cards
                //eliminate subdirectories and inner-classes
                if (fileName.endsWith(".class") && !fileName.contains("/") && !fileName.contains("$")) {
                    loadClass(fileName);
                }
            }
            
            //get new entry
            try { entry=zip.getNextEntry(); } catch (IOException ex) {}
        }
           
    }
    
    private void loadCardsFromDir(URL url) {
    Scanner classes;
        try {
            classes=new Scanner( url.openStream() );
        } catch (IOException ex) {
            throw new RuntimeException("cannot read Cards directory");
        }

        while ( classes.hasNext() ) {
            String fileName=classes.next();
            
            //look for all top level classe in package Cardgame.Cards
            if (fileName.endsWith(".class") && !fileName.contains("$")) {
                loadClass(fileName);
            }
        }
    }
    
    
    private void loadClass(String fileName) {
        boolean loaded=true;
        String classname = fileName.substring(0,fileName.length() - ".class".length());
        try { Class.forName("Cardgame.Cards." + classname);}
        catch (ClassNotFoundException ex) {
            loaded=false;
            //throw new RuntimeException("class " + classname + " not found"); 
        }
        if (loaded)
            /*TODO se vanza tempo:
            * non fa nulla in realtà, e non va bene che abbia come argomento una stringa (quindi serve un LoadedCard)
            */
            notifyObservers(fileName + " Loaded");
    }
 
    
    public static Card construct(String s) {
    
    CardConstructor c=map.get(s);
    if (c==null) {
       throw new RuntimeException("card " + s + " did not register itsef");
    }
    
    return c.create();
    }
    
    public static int size() { return map.size(); }
    
    public static void register(String s, CardConstructor c) {
        map.put(s, c);
        //CardGame.instance.getModel().sendToModel("registering "+s);
    }
    
    public static Set<String> known_cards() { return map.keySet(); }
    

}
