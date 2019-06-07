package org.srinivas.siteworks.data;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;
import org.srinivas.siteworks.exception.CalculateException;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PropertiesReadWriter {

    private static final String COIN_INVENTORY_INFORMATION = "Coin-Inventory Information";
    private static final String FILE_NAME_COIN_INVENTORY_PROPERTIES = "coin-inventory.properties";
    private Map<Integer, Integer> inventoryMap;

    public static final org.apache.logging.log4j.Logger log = LogManager.getLogger ( PropertiesReadWriter.class );
    private String resourceName;


    public void readInventoryData() throws Exception {
        Properties prop = new Properties ();
        InputStream input = null;
        if (Paths.get ( getResourceName () ).isAbsolute ()) {
            input = new FileInputStream ( getResourceName () );
        } else {
            input = this.getClass ().getClassLoader ().getResourceAsStream ( getResourceName () );
        }

        try {
            prop.load ( input );
            Map<Integer, Integer> inventoryData = extractInventoryData ( prop );
            setInventoryMap ( inventoryData );
        } catch (IOException ex) {
            log.error ( "Unable to Read Sucesssfully", ex );
            throw new CalculateException ( "Unable to Read Sucesssfully", ex );
        } finally {
            try {
                if (input != null) {
                    input.close ();
                }
            } catch (IOException ex) {
                log.error ( "InputStream Not Closed", ex );
                throw new CalculateException ( "InputStream Not Closed", ex );
            }

        }
    }

    public void writeInventoryData(Map<Integer, Integer> inventoryMap) throws Exception {
        URL url = null;
        if (Paths.get ( getResourceName () ).isAbsolute ()) {
            url = new File ( getResourceName () ).toURI ().toURL ();
        } else {

            url = this.getClass ().getClassLoader ().getResource ( getResourceName () );
        }
        Properties prop = new Properties ();
        Writer writer = new FileWriter ( url.getPath () );
        try {
            inventoryMap.entrySet ().stream ().forEach ( coin -> {
                prop.setProperty ( String.valueOf ( coin.getKey () ), String.valueOf ( coin.getValue () ) );
            } );
            prop.store ( writer, COIN_INVENTORY_INFORMATION );
            writer.close ();
        } catch (IOException ex) {
            log.error ( "Unable to Write Sucesssfully", ex );
            throw new CalculateException ( "Unable to Read Sucesssfully", ex );
        } finally {

            try {
                if (writer != null) {
                    writer.close ();
                }
            } catch (IOException ex) {
                log.error ( "Writer Not Closed", ex );
                throw new CalculateException ( "Writer Not Closed", ex );
            }

        }
    }

    public Integer[] denominations() {
        Integer[] denominations = getInventoryMap ().keySet ().stream ().toArray ( Integer[]::new );
        Arrays.sort ( denominations, Collections.reverseOrder () );
        return denominations;
    }

    public Map<Integer, Integer> getInventoryMap() {
        if (inventoryMap == null) {
            inventoryMap = new HashMap<Integer, Integer> ();
        }
        return inventoryMap;
    }

    public void setInventoryMap(Map<Integer, Integer> inventoryMap) {
        this.inventoryMap = inventoryMap;
    }


    public String getResourceName() {
        if (resourceName == null) {
            setResourceName ( FILE_NAME_COIN_INVENTORY_PROPERTIES );
        }
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }


    private Map<Integer, Integer> extractInventoryData(Properties prop) {
        return prop.entrySet ().stream ()
                .collect ( Collectors.toMap ( e -> Integer.valueOf ( String.valueOf ( e.getKey () ) ), e -> Integer.valueOf ( String.valueOf ( e.getValue () ) ) ) );
    }

}
