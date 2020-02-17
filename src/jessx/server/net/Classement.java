// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.net;

import java.util.Iterator;

public class Classement
{
    double[] tabcash;
    String[] tabname;
    int nbrPlayer;
    
    public Classement() {
        this.tabcash = new double[100];
        this.tabname = new String[100];
        this.getPlayers();
    }
    
    void getPlayers() {
        final Iterator playerNames = NetworkCore.getPlayerList().keySet().iterator();
        int i = 0;
        while (playerNames.hasNext()) {
            final String playerName = (String) playerNames.next();
            final Player joueur = NetworkCore.getPlayer(playerName);
            final float cash = joueur.getPortfolio().getCash();
            this.tabcash[i] = cash;
            this.tabname[i] = playerName;
            ++i;
        }
        this.nbrPlayer = i;
    }
    
    int getNbrPlayer() {
        return this.nbrPlayer;
    }
    
    String showtab() {
        this.sort();
        String msg = "";
        for (int i = 0; i <= this.nbrPlayer - 1; ++i) {
            final int j = i + 1;
            msg = String.valueOf(msg) + "\nn°" + j + " " + this.tabname[i] + "\t -> \t" + this.tabcash[i] + " $";
        }
        return msg;
    }
    
    void sort() {
        for (int i = 0; i <= this.nbrPlayer - 1; ++i) {
            final double val1 = this.tabcash[i];
            final String val2 = this.tabname[i];
            int j;
            for (j = i; j > 0 && 1.0 < this.tabcash[j - 1]; --j) {
                this.tabcash[j] = this.tabcash[j - 1];
                this.tabname[j] = this.tabname[j - 1];
            }
            this.tabcash[j] = val1;
            this.tabname[j] = val2;
        }
    }
}
