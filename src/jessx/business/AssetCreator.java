// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

import jessx.utils.Utils;
import java.util.Hashtable;

public abstract class AssetCreator
{
    public static Hashtable assetFactories;
    
    static {
        AssetCreator.assetFactories = new Hashtable();
    }
    
    public static Asset createAsset(final String name) throws AssetNotCreatedException {
        Utils.logger.info("Trying to create the following asset: " + name);
        Utils.logger.debug("Looking for its class in the Hashtable.");
        Class assetClass = (Class) AssetCreator.assetFactories.get(name);
        if (assetClass == null) {
            Utils.logger.debug("Class not found. The asset has never been loaded.");
            Utils.logger.debug("As all assets from the modules/assets directory has already been loaded, we are looking in the classPath.");
            try {
                Class.forName("jessx.business.assets." + name);
                assetClass = (Class) AssetCreator.assetFactories.get(name);
                if (assetClass == null) {
                    Utils.logger.warn("Asset not found in the classPath.");
                    throw new AssetNotCreatedException();
                }
            }
            catch (ClassNotFoundException e) {
                Utils.logger.warn("Asset not found in the classPath.");
                throw new AssetNotCreatedException();
            }
        }
        Utils.logger.debug("Returning the result of the create method of the factory: the asset.");
        try {
            return (Asset) assetClass.newInstance();
        }
        catch (IllegalAccessException ex) {
            Utils.logger.error("error creating the requested asset: " + ex.toString());
            ex.printStackTrace();
            return null;
        }
        catch (InstantiationException ex2) {
            Utils.logger.error("error creating the requested asset: " + ex2.toString());
            ex2.printStackTrace();
            return null;
        }
    }
}
