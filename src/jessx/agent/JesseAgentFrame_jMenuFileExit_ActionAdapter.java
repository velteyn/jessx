// 
// Decompiled by Procyon v0.6.0
// 

package jessx.agent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class JesseAgentFrame_jMenuFileExit_ActionAdapter implements ActionListener
{
    JesseAgentFrame adaptee;
    
    JesseAgentFrame_jMenuFileExit_ActionAdapter(final JesseAgentFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuFileExit_actionPerformed(e);
    }
}
