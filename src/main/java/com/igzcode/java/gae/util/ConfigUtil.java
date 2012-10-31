package com.igzcode.java.gae.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.igzcode.java.gae.configuration.ConfigurationDto;
import com.igzcode.java.gae.configuration.ConfigurationManager;
import com.igzcode.java.gae.configuration.ConfigurationManager.InvalidConfigurationException;

/**
 * Utility for save/get/delete configuration values easily. The values are
 * readed from datastore and cached in static memory. The configurations can be public
 * or private.
 * You must call ConfigUtil.init() at application startup in a servlet context listener or an init servlet - wherever your application starts running.
 */
public class ConfigUtil {
    
    
    private ConfigUtil () {}
    
    static private ConfigUtil instance = new ConfigUtil();
    
    static public ConfigUtil getInstance () {
        return instance;
    }
    
    
    private static final Logger LOG = Logger.getLogger(ConfigUtil.class.getName());

    private boolean isDev = false;

    private HashMap<String, String> cachedValues;

    private ConfigurationManager configurationManager = new ConfigurationManager();

    public void init() {
        
        LOG.info("Initializing application configurations...");

        try {
            
            InetAddress localhost = InetAddress.getByName("127.0.0.1");
            
            if ( NetworkInterface.getByInetAddress(localhost) != null ) {
                isDev = true;
                
                LOG.info("Dev environment mode");
            }
            
        } catch (UnknownHostException unknownHostE) {
            isDev = false;
        } catch (SocketException socketE) {
            isDev = false;
        }
        
        loadValuesFromDatastore();
        LOG.info("All configuration values has been loaded and cached");
    }

    /**
     * Get a configuration value as integer.
     * 
     * @param p_key
     *            The configuration key
     * @return The configuration value
     */
    public Integer getInteger(String p_key) {
        return Integer.valueOf(getValue(p_key));
    }

    /**
     * Get a configuration value.
     * 
     * @param p_key
     *            The configuration key
     * @return The configuration value
     */
    public String getValue(String p_key) {
        checkCachedValues();
        return cachedValues.get(p_key);
    }

    /**
     * Create or update a configuration value.
     * 
     * @param p_key
     *            The configuration key
     * @param p_value
     *            The configuration value
     * @param p_private
     *            The configuration scope
     * @throws InvalidConfigurationException 
     */
    public void save(String p_key, String p_value) throws InvalidConfigurationException {
        configurationManager.save(p_key, p_value);
        cachedValues.put(p_key, p_value);
    }

    /**
     * Delete if exists a configuration value.
     * 
     * @param p_key
     *            The configuration key
     */
    public void delete(String p_key) {
        if ( cachedValues.containsKey(p_key) ) {
            cachedValues.remove(p_key);
        }
        configurationManager.delete(p_key);
    }

    /**
     * Refresh the cached values.
     */
    public void loadValuesFromDatastore() {
        cachedValues = new HashMap<String, String>();
        List<ConfigurationDto> configs = configurationManager.findAll();

        if (configs != null && configs.size() > 0) {
            for (ConfigurationDto config : configs) {
                cachedValues.put(config.getKey(), config.getValue());
            }
        }
    }

    /**
     * Indicate if the current environment is localhost.
     * 
     * @return A boolean that indicate if the current environment is localhost
     */
    public boolean isDev() {
        return isDev;
    }
    
    private void checkCachedValues() {
        if (cachedValues == null) {
            loadValuesFromDatastore();
        }
    }
}
