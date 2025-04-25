// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.net.NetworkWritable;
import jessx.net.Information;
import jessx.server.net.NetworkCore;
import jessx.business.BusinessCore;
import jessx.utils.Utils;
import java.util.Vector;

public class MessageTimer extends Thread
{
    private Vector listInformation;
    private Vector listInformationSorted;
    
    public MessageTimer(final Vector Informations) {
        this.checkInformationsToSend(Informations);
        this.listInformation = (Vector)Informations.clone();
        this.listInformationSorted = (Vector)this.sort().clone();
        Utils.logger.info("MessageTimer created...");
    }
    
    public Vector sort() {
        final Vector listSorted = new Vector();
        final int size = this.listInformation.size();
        for (int i = size - 1; i >= 0; --i) {
            String[] temp = (String[]) this.listInformation.get(i);
            int index = i;
            for (int j = i; j >= 0; --j) {
                final boolean comparePeriod = Integer.parseInt(((String[])this.listInformation.get(j))[2]) < Integer.parseInt(temp[2]);
                if (comparePeriod || (Integer.parseInt(((String[])this.listInformation.get(j))[2]) == Integer.parseInt(temp[2]) & Integer.parseInt(((String[])this.listInformation.get(j))[3]) < Integer.parseInt(temp[3]))) {
                    temp = (String[]) this.listInformation.get(j);
                    index = j;
                }
            }
            listSorted.add(new String[] { temp[0], temp[1], temp[2], temp[3] });
            this.listInformation.remove(index);
        }
        return listSorted;
    }
    
    public void checkInformationsToSend(final Vector information) {
        final int periodCount = BusinessCore.getGeneralParameters().getPeriodCount();
        final int periodDuration = BusinessCore.getGeneralParameters().getPeriodDuration();
        final int size = information.size();
        for (int i = size - 1; i >= 0; --i) {
            if (Integer.parseInt(((String[])information.get(i))[3]) >= periodDuration || Integer.parseInt(((String[])information.get(i))[2]) > periodCount) {
                information.remove(i);
            }
        }
    }
    
    @Override
    public void run() {
        final int size = this.listInformationSorted.size();
        if (size != 0) {
            int i = 0;
            try {
                this.listInformationSorted.add(this.listInformationSorted.get(size - 1));
                while (NetworkCore.getExperimentManager().getExperimentState() != 0 & i < size) {
                    while (NetworkCore.getExperimentManager().getExperimentState() == 2 & i < size) {
                        final long timeTemp = 1000 * Integer.parseInt(((String[])this.listInformationSorted.get(i))[3]) - NetworkCore.getExperimentManager().getTimeInPeriod();
                        if (timeTemp > 0L) {
                            Thread.sleep(timeTemp);
                        }
                        if (NetworkCore.getExperimentManager().getPeriodNum() + 1 == Integer.parseInt(((String[])this.listInformationSorted.get(i))[2])) {
                            do {
                                if (((String[])this.listInformationSorted.get(i))[1].equals("All players")) {
                                    NetworkCore.sendToAllPlayers(new Information(((String[])this.listInformationSorted.get(i))[0]));
                                }
                                else {
                                    NetworkCore.sendToPlayerCategory(new Information(((String[])this.listInformationSorted.get(i))[0]), ((String[])this.listInformationSorted.get(i))[1]);
                                }
                            } while (++i < size & Integer.parseInt(((String[])this.listInformationSorted.get(i - 1))[3]) == Integer.parseInt(((String[])this.listInformationSorted.get(i))[3]) & NetworkCore.getExperimentManager().getPeriodNum() + 1 == Integer.parseInt(((String[])this.listInformationSorted.get(i))[2]));
                        }
                        else {
                            while (i < size & NetworkCore.getExperimentManager().getPeriodNum() + 1 > Integer.parseInt(((String[])this.listInformationSorted.get(i))[2])) {
                                ++i;
                                Utils.logger.warn("A message has not be sent");
                            }
                            final long time = NetworkCore.getExperimentManager().getTimeRemainingInPeriod();
                            if (time <= 0L) {
                                continue;
                            }
                            Thread.sleep(time);
                        }
                    }
                    while (NetworkCore.getExperimentManager().getExperimentState() == 1 & i < size) {
                        Thread.sleep(300L);
                        final long n = Math.abs(1000 * Integer.parseInt(((String[])this.listInformationSorted.get(i))[3]));
                    }
                }
            }
            catch (final InterruptedException ex1) {
                Utils.logger.warn("MessageTimer sleep interrupted. " + ex1.toString());
            }
        }
    }
}
