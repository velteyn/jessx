package jessx.server.net;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jessx.business.BusinessCore;
import jessx.business.Institution;
import jessx.business.OrderBook;
import jessx.business.PlayerType;
import jessx.business.Portfolio;
import jessx.net.DividendInfo;
import jessx.net.ExpUpdate;
import jessx.net.Message;
import jessx.net.NetworkWritable;
import jessx.server.net.event.ExperimentStateListener;
import jessx.utils.Constants;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;

public class ExperimentManager extends Thread implements Constants {
  public static final int EXP_ON_PER_ON = 2;
  
  public static final int EXP_ON_PER_OFF = 1;
  
  public static final int EXP_OFF = 0;
  
  private int periodNum = -1;
  
  private Date periodBeginning;
  
  private int experimentState;
  
  private Vector listeners = new Vector();
  
  public void addExperimentStateListener(ExperimentStateListener listener) {
    this.listeners.add(listener);
  }
  
  public void removeExperimenetStateListener(ExperimentStateListener listener) {
    this.listeners.remove(listener);
  }
  
  private void fireExperimentStateChanged() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((ExperimentStateListener)this.listeners.elementAt(i)).experimentStateChanged(getExperimentState()); 
  }
  
  private void setExperimentState(int newState) {
    this.experimentState = newState;
    fireExperimentStateChanged();
  }
  
  public int getExperimentState() {
    return this.experimentState;
  }
  
  public int getPeriodNum() {
    return this.periodNum;
  }
  
  public boolean beginExperiment(JFrame ParentFrame) {
    Utils.logger.info("Experiment is initiated...");
    Vector information = BusinessCore.getScenario().getListInformation();
    int size = information.size();
    int periodCount = BusinessCore.getGeneralParameters().getPeriodCount();
    int periodDuration = BusinessCore.getGeneralParameters().getPeriodDuration();
    int i = 0;
    boolean noProblem = true;
    while ( i < size & noProblem) {
      if (Integer.parseInt(((String[])information.get(i))[3]) >= 
        periodDuration || 
        Integer.parseInt(((String[])information.get(i))[2]) > 
        periodCount) {
        String warnMessage = "Some pieces of information are sent after the end of a period\nor after the end of the experiment.\nDo you want to correct this mistake?";
        Utils.logger.warn(warnMessage);
        int reponse = JOptionPane.showConfirmDialog(ParentFrame, warnMessage, 
            "Initiating experiment error.", 
            0, 
            2);
        if (reponse == 0)
          return false; 
        noProblem = false;
      } 
      i++;
    } 
    Iterator<String> iter = NetworkCore.getPlayerList().keySet().iterator();
    while (iter.hasNext()) {
      String login = iter.next();
      String pc = NetworkCore.getPlayer(login).getPlayerCategory();
      if (NetworkCore.getPlayer(login).getPlayerStatus() == 1 && (pc == null || !BusinessCore.getScenario().getPlayerTypes().containsKey(pc))) {
        String warnMessage = "Some players don't have a player type. The experiment won't begin before.";
        Utils.logger.warn(warnMessage);
        JOptionPane.showConfirmDialog(ParentFrame, warnMessage, "Initiating experiment error.", -1, 2);
        return false;
      } 
    } 
    if (NetworkCore.getPlayerList().size() == 0) {
      JOptionPane.showConfirmDialog(ParentFrame, "No player connected", 
          "Initiating experiment error.", 
          -1, 
          2);
      return false;
    } 
    String pwd = BusinessCore.getGeneralParameters().getWorkingDirectory();
    String fileName = BusinessCore.getGeneralParameters().getLoggingFileName();
    if (pwd == null || pwd.equalsIgnoreCase("") || 
      fileName == null || fileName.equalsIgnoreCase("")) {
      JOptionPane.showConfirmDialog(ParentFrame, "You have to choose a name in the general parameters\n to save the results of the experiment.", 
          "Initiating experiment error.", 
          -1, 
          2);
      return false;
    } 
    if (!pwd.substring(pwd.length() - 1).equals(System.getProperty("file.separator")))
      pwd = String.valueOf(pwd) + System.getProperty("file.separator"); 
    if (fileName.length() < 5 || 
      !fileName.substring(fileName.length() - 4).equalsIgnoreCase(".xml"))
      fileName = String.valueOf(fileName) + ".xml"; 
    File file = new File(String.valueOf(pwd) + fileName);
    int j = 0;
    while (file.exists()) {
      String name = String.valueOf(pwd) + fileName;
      name.endsWith("");
      name = name.substring(0, name.lastIndexOf(" ") + 1);
      j++;
      BusinessCore.getGeneralParameters().setLoggingFileName((String.valueOf(name) + j).substring(pwd.length(), (String.valueOf(name) + j).length()));
      name = String.valueOf(name) + j + ".xml";
      file = new File(name);
    } 
    try {
      file.createNewFile();
    } catch (IOException ex) {
      JOptionPane.showConfirmDialog(ParentFrame, "An error occured.\nCheck the working directory and the name of the logging file in the general parameters\nThen, try to start the experiment.", 
          "Initiating experiment error.", 
          -1, 
          2);
      return false;
    } 
    Iterator<String> iter2 = NetworkCore.getPlayerList().keySet().iterator();
    while (iter2.hasNext()) {
      String key = iter2.next();
      NetworkCore.getPlayer(key).initClient();
    } 
    this.periodNum = -1;
    setExperimentState(1);
    NetworkCore.sendToAllPlayers((NetworkWritable)new ExpUpdate(0, "", -1));
    produceDividendMessages(0);
    Utils.logger.debug("Experiment is on.");
    NetworkCore.arePlayersReady("Click on OK when you are ready\nto begin.");
    return true;
  }
  
  public ExperimentManager() {
    this.experimentState = 0;
    start();
  }
  
  public void beginNewPeriod() {
    Utils.logger.debug("Beginning a new period.");
    this.periodNum++;
    Iterator<String> iter = BusinessCore.getInstitutions().keySet().iterator();
    while (iter.hasNext()) {
      Institution inst = BusinessCore.getInstitution(iter.next());
      if (!inst.getKeepingOrderBook())
        inst.emptyOrderBook(); 
    } 
    resetPortfolios();
    setExperimentState(2);
    this.periodBeginning = new Date();
    NetworkCore.sendToAllPlayers((NetworkWritable)new ExpUpdate(2, Long.toString(getTimeRemainingInPeriod()), this.periodNum));
  }
  
  private void endPeriodNow() {
    Utils.logger.debug("Ending the period" + (this.periodNum + 1));
    setExperimentState(1);
    NetworkCore.sendToAllPlayers((NetworkWritable)new ExpUpdate(3, "", this.periodNum));
    Element dividends = payDividends();
    logDividends(dividends);
    logPortfolios();
    if (this.periodNum < BusinessCore.getGeneralParameters().getPeriodCount() - 1)
      produceDividendMessages(this.periodNum + 1); 
    resetOrderBooks();
    NetworkCore.getLogManager().saveLogToFile();
    if (this.periodNum == BusinessCore.getGeneralParameters().getPeriodCount() - 1) {
      endExperimentNow();
    } else {
      NetworkCore.arePlayersReady("Click on OK when you are ready\nto begin next period.");
    } 
  }
  
  private void endExperimentNow() {
    Classement Score = new Classement();
    System.out.println("\nNbre de joueurs : " + Score.getNbrPlayer());
    Utils.logger.debug("Pouette");
    Score.showtab();
    Utils.logger.debug("Pouette2");
    String message = "Experiment is finished\n\nClassification : " + Score.showtab();
    NetworkCore.sendToAllPlayers((NetworkWritable)new ExpUpdate(1, 
          message, 
          this.periodNum));
    setExperimentState(0);
  }
  
  private void produceDividendMessages(int period) {
    Iterator<String> playerTypes = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
    while (playerTypes.hasNext()) {
      PlayerType pt = BusinessCore.getScenario().getPlayerType(playerTypes.next());
      String divMessage = "";
      divMessage = String.valueOf(divMessage) + "Interest rate :" + BusinessCore.getGeneralParameters().getInterestRate(period) + "%\n\n";
      Iterator<String> assets = BusinessCore.getAssets().keySet().iterator();
      while (assets.hasNext()) {
        String key = assets.next();
        divMessage = String.valueOf(divMessage) + ":: " + key + " ::\n";
        if (pt.getDividendInfo(key).isDisplayingSessionLength())
          divMessage = String.valueOf(divMessage) + 
            "The session has " + 
            BusinessCore.getGeneralParameters().getPeriodCount() + 
            " periods.\n"; 
        if (pt.getDividendInfo(key).isDisplayingHoldingValueForExperiment())
          divMessage = String.valueOf(divMessage) + 
            "The holding value for the session is " + 
            BusinessCore.getAsset(key).getDividendModel().getExperimentHoldingValue(period) + 
            ".\n"; 
        if (pt.getDividendInfo(key).isDisplayingWindowSize())
          divMessage = String.valueOf(divMessage) + 
            "Following Info are given for the next " + 
            pt.getDividendInfo(key).getWindowSize() + " periods :\n"; 
        int windowSize = pt.getDividendInfo(key).getWindowSize();
        if (pt.getDividendInfo(key).isDisplayHoldingValueForWindow())
          divMessage = String.valueOf(divMessage) + "The holding value is " + 
            BusinessCore.getAsset(key).getDividendModel().getWindowHoldingValue(period, windowSize) + "\n"; 
        if (3 != pt.getDividendInfo(key).getDividendDetailledproperties())
          if (1 == pt.getDividendInfo(key).getDividendDetailledproperties()) {
            divMessage = String.valueOf(divMessage) + "For the next period: " + 
              BusinessCore.getAsset(key).getDividendModel().getDividendAt(period)
              .getDetails() + "\n";
          } else if (2 == pt.getDividendInfo(key).getDividendDetailledproperties()) {
            for (int i = period; i < period + windowSize; i++)
              divMessage = String.valueOf(divMessage) + "For the period " + Integer.toString(i + 1) + ": " + 
                BusinessCore.getAsset(key).getDividendModel().getDividendAt(i)
                .getDetails() + "\n"; 
          } else {
            for (int i = period; i < BusinessCore.getGeneralParameters().getPeriodCount(); i++)
              divMessage = String.valueOf(divMessage) + "For the period " + Integer.toString(i + 1) + ": " + 
                BusinessCore.getAsset(key).getDividendModel().getDividendAt(i)
                .getDetails() + "\n"; 
          }  
        divMessage = String.valueOf(divMessage) + "_______________\n";
        DividendInfo divInfo = new DividendInfo();
        divInfo.setAssetName(key);
        NetworkCore.sendToPlayerCategory((NetworkWritable)divInfo, pt.getPlayerTypeName());
      } 
      NetworkCore.sendToPlayerCategory((NetworkWritable)new Message(divMessage), pt.getPlayerTypeName());
    } 
  }
  
  private Element payDividends() {
    HashMap<Object, Object> oneMessageForOnePlayer = new HashMap<Object, Object>();
    Iterator<String> p = NetworkCore.getPlayerList().keySet().iterator();
    float interest = BusinessCore.getGeneralParameters().getInterestRate(getPeriodNum());
    while (p.hasNext()) {
      Player player = NetworkCore.getPlayer(p.next());
      oneMessageForOnePlayer.put(player.getLogin(), 
          "For the last period :\n\nYour cash rewards you " + (
          
          player.getPortfolio().getCash() * 
          interest / 100.0F) + " " + 
          "$" + " of interest.\n\n");
      player.getPortfolio().setCash(player.getPortfolio().getCash() * (
          100.0F + interest) / 100.0F);
    } 
    Element dividends = new Element("Dividends");
    Iterator<String> assets = BusinessCore.getAssets().keySet().iterator();
    while (assets.hasNext()) {
      Element div = new Element("dividend");
      dividends.addContent((Content)div);
      Iterator<String> players = NetworkCore.getPlayerList().keySet().iterator();
      String asset = assets.next();
      float dividend = BusinessCore.getAsset(asset).getDividendModel()
        .getDividend(this.periodNum);
      div.setAttribute("asset", asset);
      div.setAttribute("value", String.valueOf(dividend));
      while (players.hasNext()) {
        Player pl = NetworkCore.getPlayer(players.next());
        String unfinished = (String)oneMessageForOnePlayer.get(pl.getLogin());
        float fl = pl.getPortfolio().payDividend(asset, dividend);
        oneMessageForOnePlayer.remove(pl.getLogin());
        oneMessageForOnePlayer.put(pl.getLogin(), String.valueOf(unfinished) + "- " + 
            asset + "'s dividends : " + 
            dividend + " " + 
            "$" + 
            "\n  You are credited of " + fl + " " + 
            "$" + " \n");
      } 
    } 
    Iterator pIter = NetworkCore.getPlayerList().keySet().iterator();
    while (pIter.hasNext()) {
      Object playeurP = pIter.next();
      NetworkCore.sendToPlayer((NetworkWritable)new Message((String)oneMessageForOnePlayer.get(playeurP.toString())), playeurP.toString());
    } 
    return dividends;
  }
  
  private void resetPortfolios() {
    Iterator<String> playerNames = NetworkCore.getPlayerList().keySet().iterator();
    while (playerNames.hasNext()) {
      String playerName = playerNames.next();
      Player player = NetworkCore.getPlayer(playerName);
      Portfolio playerPortfolio = player.getPortfolio();
      if (BusinessCore.getScenario().getPlayerType(player.getPlayerCategory()) != null) {
        Vector<String> institutionsVect = BusinessCore.getScenario()
          .getPlayerType(player.getPlayerCategory())
          .getInstitutionsWherePlaying();
        playerPortfolio.preInitializeInvestments();
        for (int i = 0; i < institutionsVect.size(); i++) {
          String institutionName = institutionsVect.elementAt(i);
          Institution institution = BusinessCore.getInstitution(institutionName);
          String assetName = institution.getAssetName();
          if (!institution.getKeepingOrderBook()) {
            playerPortfolio.setInvestmentsWhenNotKeepingOrderBook(institutionName);
          } else {
            playerPortfolio.setInvestmentsWhenKeepingOrderBook(institutionName, 
                assetName);
          } 
        } 
        NetworkCore.getPlayer(playerName).send((NetworkWritable)playerPortfolio);
      } 
    } 
  }
  
  private void resetOrderBooks() {
    Iterator<String> instits = BusinessCore.getInstitutions().keySet().iterator();
    while (instits.hasNext()) {
      Institution inst = BusinessCore.getInstitution(instits.next());
      if (!inst.getKeepingOrderBook()) {
        OrderBook orderbook = inst.getOrderBook();
        orderbook.reinit();
        NetworkCore.sendToAllPlayers((NetworkWritable)orderbook);
      } 
    } 
  }
  
  private void logPortfolios() {
    Iterator<String> players = NetworkCore.getPlayerList().keySet().iterator();
    while (players.hasNext()) {
      Element pl = new Element("Portfolio");
      Player player = NetworkCore.getPlayer(players.next());
      pl.setAttribute("player", player.getLogin());
      pl.setAttribute("type", player.getPlayerCategory());
      player.getPortfolio().saveToXml(pl);
      NetworkCore.getLogManager().log(pl);
    } 
  }
  
  private void logDividends(Element div) {
    NetworkCore.getLogManager().log(div);
  }
  
  public long getTimeRemainingInPeriod() {
    Date now = new Date();
    return (
      BusinessCore.getGeneralParameters().getPeriodDuration() * 1000) - 
      now.getTime() + 
      this.periodBeginning.getTime();
  }
  
  public long getTimeInPeriod() {
    Date now = new Date();
    return 
      now.getTime() - 
      this.periodBeginning.getTime();
  }
  
  public void run() {
    while (true) {
      while (getExperimentState() == 0) {
        try {
          sleep(100L);
        } catch (InterruptedException ex) {
          Utils.logger.warn("ExperimentManager sleep was interrupted: ");
        } 
      } 
      if (getExperimentState() != 1) {
        long rt = getTimeRemainingInPeriod();
        if (rt <= 0L)
          endPeriodNow(); 
      } 
      try {
        sleep(1000L);
      } catch (InterruptedException interruptedException) {
        Utils.logger.warn("ExperimentManager sleep was interrupted: " + interruptedException.toString());
      } 
    } 
  }
}
