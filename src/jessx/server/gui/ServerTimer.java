// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.utils.Utils;
import jessx.server.net.NetworkCore;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ServerTimer extends Thread
{
    private JButton button;
    private JTextField textField;
    private boolean endOfExp;
    
    public ServerTimer(final JButton button, final JTextField textField) {
        this.button = button;
        this.textField = textField;
    }
    
    @Override
    public void run() {
        while (true) {
            final int state = NetworkCore.getExperimentManager().getExperimentState();
            if (state == 2) {
                this.endOfExp = true;
                this.textField.setText("Experiment ON");
                this.button.setText("Period " + (NetworkCore.getExperimentManager().getPeriodNum() + 1) + " - " + this.msecToString(NetworkCore.getExperimentManager().getTimeRemainingInPeriod()) + " remaining...");
            }
            else if (state == 0) {
                if (this.endOfExp) {
                    this.button.setText("End of experiment");
                    this.button.setEnabled(true);
                    this.endOfExp = false;
                }
                this.textField.setText("Experiment OFF");
            }
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ex) {
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
