package jessx.gclient.gui;

/***************************************************************/
/*                     SOFTWARE SECTION                        */
/***************************************************************/
/*
 * <p>Name: Jessx</p>
 * <p>Description: Financial Market Simulation Software</p>
 * <p>Licence: GNU General Public License</p>
 * <p>Organisation: EC Lille / USTL</p>
 * <p>Persons involved in the project : group T.E.A.M.</p>
 * <p>More details about this source code at :
 *    http://eleves.ec-lille.fr/~ecoxp03  </p>
 * <p>Current version: 1.0</p>
 */

/***************************************************************/
/*                      LICENCE SECTION                        */
/***************************************************************/
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/***************************************************************/
/*                       IMPORT SECTION                        */
/***************************************************************/

import javax.swing.*;

import jessx.client.*;
import jessx.utils.*;
import java.awt.Dimension;

/***************************************************************/
/*               ClientTimer CLASS SECTION                 */
/***************************************************************/
/**
 * <p>Title : ClientTimer</p>
 * <p>Description :</p>
 * @author Thierry Curtil
 * @version 1.0
 */

public class ClientTimer extends Thread{

  private JTextField text;

  public ClientTimer(JTextField field) {
    this.text = field;
  }

  public String getText(){
    return text.getText();
  }
  /**
   *If the experiment and period are on, we display the current number of periods and the time remaining
   * in this period.
   * If exception takes place, we send a warn.
   */
  public void run() {
    while(true) {
      if (ClientCore.getExperimentManager().getExperimentState() == ClientExperimentManager.EXP_ON_PER_ON) {
        text.setText("Period " + (ClientCore.getExperimentManager().getCurrentPeriod() + 1)                     + " - " + this.msecToString(ClientCore.getExperimentManager().getRemainingTimeInPeriod()));
      }
      try {
        this.sleep(250);
      }
      catch (InterruptedException ex) {
        Utils.logger.warn("ServerTimer sleep interrupted. " + ex.toString());
      }
    }
  }
  /**
   * We express the Client time in minute and second.
   * We maintain the double digital form of seconds.
   * @param ms long
   * @return String
   */
  private String msecToString(long ms) {
    int minute = (int)ms / 60000;
    int second = (int)(ms - minute * 60000) / 1000;
    String seconds;

    if(second <10) {
      seconds = "0"+second;
    }
    else {
      seconds = Integer.toString(second);
    }
    return ((minute == 0) ? "" : Integer.toString(minute) + "min ") + seconds + "s";
  }
}
