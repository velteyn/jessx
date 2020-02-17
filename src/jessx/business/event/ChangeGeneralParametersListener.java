// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.event;

import java.util.EventListener;

public interface ChangeGeneralParametersListener extends EventListener
{
    void generalParametersChanged(final ChangeGeneralParametersEvent p0);
}
