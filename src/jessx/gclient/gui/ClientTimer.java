// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import jessx.utils.Utils;
import jessx.client.ClientCore;
import javax.swing.JLabel;

public class ClientTimer extends Thread
{
    private JLabel text;
    
    public ClientTimer(final JLabel field) {
        this.text = field;
    }
    
    public String getText() {
        return this.text.getText();
    }
    
    @Override
    public void run() {
        while (true) {
            if (ClientCore.getExperimentManager().getExperimentState() == 2) {
                this.text.setText("Period " + (ClientCore.getExperimentManager().getCurrentPeriod() + 1) + " - " + this.msecToString(ClientCore.getExperimentManager().getRemainingTimeInPeriod()));
            }
            try {
                Thread.sleep(250L);
            }
            catch (final InterruptedException ex) {
                Utils.logger.warn("ServerTimer sleep interrupted. " + ex.toString());
            }
        }
    }
    
    private String msecToString(final long ms) {
        final int minute = (int)ms / 60000;
        final int second = (int)(ms - minute * 60000) / 1000;
        String seconds;
        if (second < 10) {
            seconds = "0" + second;
        }
        else {
            seconds = Integer.toString(second);
        }
        return String.valueOf((minute == 0) ? "" : new StringBuilder(String.valueOf(Integer.toString(minute))).append("min ").toString()) + seconds + "s";
    }
}
