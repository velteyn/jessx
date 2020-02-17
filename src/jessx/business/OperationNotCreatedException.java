// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

public class OperationNotCreatedException extends Exception
{
    public OperationNotCreatedException() {
        super("Operation could not be created.");
    }
}
