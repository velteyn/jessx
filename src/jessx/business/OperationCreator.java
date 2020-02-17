// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

import jessx.utils.Utils;
import java.util.Hashtable;

public abstract class OperationCreator
{
    public static Hashtable operationFactories;
    
    static {
        OperationCreator.operationFactories = new Hashtable();
    }
    
    public static Operation createOperation(final String name) throws OperationNotCreatedException {
        Class operationClass = (Class) OperationCreator.operationFactories.get(name);
        if (operationClass == null) {
            Utils.logger.debug("Class not found. The operation has never been loaded.");
            Utils.logger.debug("As all operations from the modules/operations directory has already been loaded, we are looking in the classPath.");
            try {
                Class.forName("jessx.business.operations." + name);
                operationClass = (Class) OperationCreator.operationFactories.get(name);
                if (operationClass == null) {
                    throw new OperationNotCreatedException();
                }
            }
            catch (ClassNotFoundException e) {
                Utils.logger.warn("operation not found in the classPath.");
                throw new OperationNotCreatedException();
            }
        }
        try {
            return (Operation) operationClass.newInstance();
        }
        catch (IllegalAccessException ex) {
            Utils.logger.error("error creating the requested operation: " + ex.toString());
            ex.printStackTrace();
            throw new OperationNotCreatedException();
        }
        catch (InstantiationException ex2) {
            Utils.logger.error("error creating the requested operation: " + ex2.toString());
            ex2.printStackTrace();
            throw new OperationNotCreatedException();
        }
    }
}
