package jessx.analysis.tools;

/***************************************************************/
/*                     SOFTWARE SECTION                        */
/***************************************************************/
/*
 * <p>Name: JessX</p>
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
 * modify it under the terms of the GNU General Pulimitorderblic License
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

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.jdom.*;
import jessx.analysis.*;
import jessx.utils.*;

/***************************************************************/
/*              XMLExportation CLASS SECTION                   */
/***************************************************************/

/**
 * <p>Title : XMLExportation</p>
 * <p>Description : This class enables to export the parameters<br />
 * and/or the results of an experiment. It creates a new file<br />
 * easier to analyse: a .csv file. </p>
 * @author Christophe Grosjean, Mohamed Amine Hamamouchi, Charles Montez.
 * @version 1.0
 */

public class XMLExportation {

  public XMLExportation() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //The HashMap VectorCollection contains the "columns" to export
  private HashMap vectorCollection = new HashMap();
  //Number of vectors saved in the Hashmap VectorCollection
  private int vectorNumber = 0;
  //All those JCheckBox enables to choose what is interesting to export
  private JCheckBox jCheckBoxGeneralParameters = new JCheckBox(
      "General parameters");
  private JCheckBox jCheckBoxAssetsParameters = new JCheckBox(
      "Assets parameters");
  private JCheckBox jCheckBoxInstitutions = new JCheckBox(
      "Institutions Caracteristics");
  private JCheckBox jCheckBoxScenario = new JCheckBox("Scenario");
  private JCheckBox jCheckBoxdeal = new JCheckBox(
      "Deal (Time,cash) for each asset");
  private JCheckBox jCheckBoxOperations = new JCheckBox(
      "Operations (Time,cash and quantity) for each asset");
  private JCheckBox jCheckBoxPortfolios = new JCheckBox(
      "Portfolios (Cash with assets and quantities) for each player");
  private JCheckBox jCheckBoxInformation = new JCheckBox("Information");
  private JCheckBox jCheckBoxChat = new JCheckBox("Chat");
  private JCheckBox jCheckBoxDividend = new JCheckBox("Dividends");
  //Principal method where the file is created


  public XMLExportation(Document doc, Frame parentFrame) {
    int reponse;
    reponse = optionPopup(parentFrame);
    if (reponse == JOptionPane.OK_OPTION) {

      try {
        //Verify if a JCheckBox is selected and save in vectorCollection(HashMap)
        //the right vector to export

        if (jCheckBoxGeneralParameters.isSelected()) {
          generalParameters(doc);
        }
        if (jCheckBoxAssetsParameters.isSelected()) {
          assetsParameters(doc);
        }
        if (jCheckBoxInstitutions.isSelected()) {
          institutions(doc);
        }
        if (jCheckBoxScenario.isSelected()) {
          scenario(doc);
        }
        if (jCheckBoxDividend.isSelected()) {
          dividend(doc);
        }
        if (jCheckBoxPortfolios.isSelected()) {
          portfolio(doc);
        }
        if (jCheckBoxdeal.isSelected()) {
          deal(doc);
        }
        if (jCheckBoxOperations.isSelected()) {
          operations(doc);
        }
        if (jCheckBoxInformation.isSelected()) {
          information(doc);
        }
        if (jCheckBoxChat.isSelected()) {
          chat(doc);
        }

        else if (! (jCheckBoxDividend.isSelected() ||
                    jCheckBoxChat.isSelected() ||
                    jCheckBoxInformation.isSelected() ||
                    jCheckBoxPortfolios.isSelected() ||
                    jCheckBoxdeal.isSelected() ||
                    jCheckBoxOperations.isSelected() ||
                    jCheckBoxScenario.isSelected() ||
                    jCheckBoxInstitutions.isSelected() ||
                    jCheckBoxGeneralParameters.isSelected() ||
                    jCheckBoxAssetsParameters.isSelected()
                 )) {
          AnalysisToolsCore.logger.info(
              "No JCHeckBox is selected. The exportation ends.");
          JOptionPane.showMessageDialog(parentFrame,
                                        "You tried to create a void file, so the exportation won't be done.",
                                        "Exportation Warning",
                                        JOptionPane.WARNING_MESSAGE);
          return;
        }

        JFileChooser chooser = Utils.newFileChooser(null, "", "CSV files",
            "csv");
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        int option = chooser.showSaveDialog(parentFrame);
        if (option == JFileChooser.CANCEL_OPTION) {
          return;
        }
        try {
          int answer = JOptionPane.OK_OPTION;
          if ( (chooser.getSelectedFile().getAbsolutePath().endsWith(".csv") &&
                chooser.getSelectedFile().exists()) ||
              new File(chooser.getSelectedFile().getAbsolutePath() +
                       ".csv").exists()) {
            answer = JOptionPane.showConfirmDialog(parentFrame,
                "A file with the same name already exists.\nDo you you want to replace it?",
                "JessX Analyzer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);
          }
          if (answer == JOptionPane.OK_OPTION) {
            if (chooser.getSelectedFile().getAbsolutePath().endsWith(".csv")) {
              fileSave(chooser.getSelectedFile().getAbsolutePath(), parentFrame);
            }
            else {
              fileSave(chooser.getSelectedFile().getAbsolutePath() + ".csv",
                       parentFrame);
            }
          }

        }
        catch (Exception ex) {
          System.out.print("Error when the file is created...\n" + ex.toString());
          return;
        }
      }
      catch (Exception e) {
        System.out.println("Error File selected is not correct." + e);
        JOptionPane.showMessageDialog(parentFrame,
                                      "Error with one or several options selected. File selected is not correct.",
                                      "Error", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  private void fileSave(String name, Frame parentFrame) {
    File outputFile = new File(name);
    AnalysisToolsCore.logger.debug("File created, name :" + name);
    FileWriter out = null;
    try {
      out = new FileWriter(outputFile);
    }
    catch (IOException ex) {
      System.out.println("Error with FileWriter" + ex.toString());
    }

    if (outputFile.exists() == false) {
      AnalysisToolsCore.logger.info(
          "File selected doesn't exist. Stopping analyse process.");
      JOptionPane.showMessageDialog(parentFrame,
                                    "File not created. Check the name you choose.");
      return;
    }
    else {
      try {
        {

          //Write in the file the different vectors contained in VectorCollection(HashMap)
          Vector sizeVector = new Vector();
          int maxsize = 0;
          for (int k = 0; k < vectorNumber; k++) {
            int size = ( (Vector) vectorCollection.get("vector" + k)).size();
            sizeVector.add("" + size);
            if (maxsize < size) {
              maxsize = size;
            }
          }
          for (int j = 0; j < maxsize; j++) {
            for (int i = 0; i < vectorNumber; i++) {
              if (Integer.parseInt( (String) sizeVector.elementAt(i)) > j) {

                out.write( (String) ( (Vector) vectorCollection.get("vector" +
                    i)).
                          elementAt(j) + ";");

              }
              else {
                out.write(";");
              }
            }
            out.write("\n");
          }
          out.close();

          AnalysisToolsCore.logger.info(
              "Display the popup informing the exportation is finished.");
          JOptionPane.showMessageDialog(parentFrame,
                                        "Your exportation is completed.\n" +
                                        outputFile.getCanonicalPath(),
                                        "Exportation Info",
                                        JOptionPane.INFORMATION_MESSAGE);
        }
      }
      catch (IOException ex1) {
      }
    }

  }

  /**
   * The method of the popup window displayed. The user chooses here
   * the name of the file, the path, what are the pieces of information
   * he wants to export and the format of the file (Notepad, Excel, Word).
   * @param parentFrame Frame
   * @return String (the name of the file that the user has entered)
   */

  private int optionPopup(Frame parentFrame) {

    JPanel jPanel1 = new JPanel(new GridLayout(2, 1));
    JPanel jPanelParameters = new JPanel(new GridLayout(3, 2));
    JPanel jPanelResults = new JPanel(new GridLayout(2, 2));

    jPanel1.add(jPanelParameters);
    jPanel1.add(jPanelResults);

    jPanelResults.add(jCheckBoxdeal);
    jPanelResults.add(jCheckBoxDividend);
    jPanelResults.add(jCheckBoxOperations);
    jPanelResults.add(jCheckBoxPortfolios);

    jPanelParameters.add(jCheckBoxAssetsParameters);
    jPanelParameters.add(jCheckBoxChat);
    jPanelParameters.add(jCheckBoxGeneralParameters);
    jPanelParameters.add(jCheckBoxInformation);
    jPanelParameters.add(jCheckBoxInstitutions);
    jPanelParameters.add(jCheckBoxScenario);

    jCheckBoxGeneralParameters.setSelected(true);
    jCheckBoxOperations.setSelected(true);
    jCheckBoxdeal.setSelected(true);
    jCheckBoxAssetsParameters.setSelected(true);
    jCheckBoxInstitutions.setSelected(true);
    jCheckBoxScenario.setSelected(true);
    jCheckBoxPortfolios.setSelected(true);
    jCheckBoxInformation.setSelected(true);
    jCheckBoxChat.setSelected(true);
    jCheckBoxDividend.setSelected(true);

    jPanelParameters.setBorder(new TitledBorder(BorderFactory.
                                                createEtchedBorder(
        Color.white, new Color(148, 145, 140)), "Parameters"));
    jPanelResults.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(
        Color.white, new Color(148, 145, 140)), "Results"));

    return JOptionPane.showConfirmDialog(parentFrame, jPanel1,
                                         "JessX analysis",
                                         JOptionPane.OK_CANCEL_OPTION,
                                         JOptionPane.PLAIN_MESSAGE);
  }

  /***************************************************************/
  /*             FUNCTIONS FOR THE OPTIONS                       */
  /***************************************************************/

  /**This Method is used to export the <b>General Parameters</b> of the experiment.
   * It writes the datas in one column. Each row corresponds to one parameter.
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */


  private void generalParameters(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for GeneralParameters");
    Element experimentNode = doc.getRootElement();
    Element generalParameters = experimentNode.getChild("Setup").getChild(
        "GeneralParameters");
    Vector vectorGeneralParameters = new Vector();
    vectorGeneralParameters.add("General Parameters");
    vectorGeneralParameters.add("Scenario " +
                                generalParameters.getChildText("SetupFileName"));
    vectorGeneralParameters.add("XML Log Version " +
                                generalParameters.getChildText("XMLVersion"));
    vectorGeneralParameters.add("Save in " +
                                generalParameters.getChildText(
        "LoggingFileName"));
    vectorGeneralParameters.add("Number of Periods " +
                                generalParameters.getChildText("PeriodNumber"));
    vectorGeneralParameters.add("Duration of a period " +
                                generalParameters.getChildText("PeriodDuration"));
    vectorGeneralParameters.add("Interest rate " +
                                generalParameters.getChildText("InterestRate") +
                                "%");
    vectorCollection.put("vector" + vectorNumber, vectorGeneralParameters);
    vectorNumber++;
    AnalysisToolsCore.logger.debug(
        "Exportation of the General Parameters done.");
  }

  /**This Method is used to export the <b>Parameters of each Asset</b> appearing in the experiment.
   * For each assets two columns are created: one with the dividend and one with the variance.
   * Each row corresponds to one period of the experiment.
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void assetsParameters(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for AssetsParameters");
    Element experimentNode = doc.getRootElement();
    List assetsParameters = experimentNode.getChild("Setup").getChildren(
        "Asset");
    Iterator assetsParametersIter = assetsParameters.iterator();
    while (assetsParametersIter.hasNext()) {
      Vector vectorAssetParametersWithMean = new Vector();
      Vector vectorAssetParametersWithVariance = new Vector();
      Element assetParameters = (Element) assetsParametersIter.next();
      vectorAssetParametersWithMean.add("Assets parameters");
      vectorAssetParametersWithMean.add(assetParameters.getAttributeValue(
          "type"));
      vectorAssetParametersWithMean.add(assetParameters.getAttributeValue(
          "name"));
      vectorAssetParametersWithMean.add("dividend");
      vectorAssetParametersWithVariance.add("Assets parameters");
      vectorAssetParametersWithVariance.add(assetParameters.getAttributeValue(
          "type"));
      vectorAssetParametersWithVariance.add(assetParameters.getAttributeValue(
          "name"));
      vectorAssetParametersWithVariance.add("variance");
      List dividends = assetParameters.getChild("DividendModel").getChildren(
          "Dividend");
      Iterator dividendsIter = dividends.iterator();
      while (dividendsIter.hasNext()) {
        Element dividend = (Element) dividendsIter.next();
        vectorAssetParametersWithMean.add(dividend.getAttributeValue("mean"));
        vectorAssetParametersWithVariance.add(dividend.getAttributeValue(
            "variance"));
      }
      vectorCollection.put("vector" + vectorNumber,
                           vectorAssetParametersWithMean);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber,
                           vectorAssetParametersWithVariance);
      vectorNumber++;
    }
    AnalysisToolsCore.logger.debug("Exportation of the Asset Parameters done.");
  }

  /**
   *
   * @param doc Document
   */

  private void information(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Information");
    Element experimentNode = doc.getRootElement();
    List information = experimentNode.getChild("Setup").getChild("Scenario").
        getChild("Information").
        getChildren("Information");
    Iterator informationIterator = information.iterator();
    Vector vectorInformationContent = new Vector();
    Vector vectorInformationCategory = new Vector();
    Vector vectorInformationPeriod = new Vector();
    Vector vectorInformationTime = new Vector();
    vectorInformationContent.add("Information");
    vectorInformationCategory.add("Information");
    vectorInformationPeriod.add("Information");
    vectorInformationTime.add("Information");

    while (informationIterator.hasNext()) {

      Element informations = (Element) informationIterator.next();
      //For each Information, it creates for columns

      vectorInformationContent.add(informations.getAttributeValue("Content"));
      vectorInformationCategory.add(informations.getAttributeValue("Category"));
      vectorInformationPeriod.add(informations.getAttributeValue("Period"));
      vectorInformationTime.add(informations.getAttributeValue("Time"));
    }
    vectorCollection.put("vector" + vectorNumber, vectorInformationContent);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorInformationCategory);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorInformationPeriod);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorInformationTime);
    vectorNumber++;

  }

  /**
   *
   * @param doc Document
   */

  private void chat(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Chat");
    Element experimentNode = doc.getRootElement();
    List chatMessages = experimentNode.getChild("Setup").getChild("Chat").
        getChildren("ChatMessage");

    Iterator chatMessagesIterator = chatMessages.iterator();
    Vector vectorChatMessageSubject = new Vector();
    Vector vectorChatMessagePeriod = new Vector();
    Vector vectorChatMessageReceivers = new Vector();
    Vector vectorChatMessageTime = new Vector();
    vectorChatMessageSubject.add("ChatMessage");
    vectorChatMessagePeriod.add("ChatMessage");
    vectorChatMessageReceivers.add("ChatMessage");
    vectorChatMessageTime.add("ChatMessage");

    while (chatMessagesIterator.hasNext()) {
      Element chatMessage = (Element) chatMessagesIterator.next();
      vectorChatMessageSubject.add(chatMessage.getAttributeValue("Subject"));
      vectorChatMessagePeriod.add(chatMessage.getAttributeValue("Period"));
      vectorChatMessageReceivers.add(chatMessage.getAttributeValue("Subject"));
      vectorChatMessageTime.add(chatMessage.getAttributeValue("Time"));
    }

    vectorCollection.put("vector" + vectorNumber, vectorChatMessageSubject);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorChatMessagePeriod);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorChatMessageReceivers);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorChatMessageTime);
    vectorNumber++;

  }

  /**This Method is used to export the <b>Descritpion of the Asset's Institution</b>.
   * An <b>Institution</b> is a frame of the market around ONE asset.
   * The methode writes one column by Institution. Each row corresponds to one parameter.
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void institutions(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Institutions");
    Element experimentNode = doc.getRootElement();
    List institutions = experimentNode.getChild("Setup").getChildren(
        "Institution");
    Iterator institutionsIter = institutions.iterator();
    while (institutionsIter.hasNext()) {
      Element institution = (Element) institutionsIter.next();

      //For each institution, it creates two columns with the name of the order and its cost
      Vector vectorInstitutionWithOperationsName = new Vector();
      Vector vectorInstitutionWithOperationsCost = new Vector();
      vectorInstitutionWithOperationsName.add("Institution");
      vectorInstitutionWithOperationsCost.add("Institution");
      vectorInstitutionWithOperationsName.add(institution.getAttributeValue(
          "type"));
      vectorInstitutionWithOperationsCost.add(institution.getAttributeValue(
          "type"));
      String name = institution.getAttributeValue("name");
      vectorInstitutionWithOperationsName.add(name);
      vectorInstitutionWithOperationsCost.add(name);
      vectorInstitutionWithOperationsName.add(institution.getAttributeValue(
          "quotedAsset"));
      vectorInstitutionWithOperationsCost.add(institution.getAttributeValue(
          "quotedAsset"));
      vectorInstitutionWithOperationsName.add("Order Type");
      vectorInstitutionWithOperationsCost.add("Order Cost");

      List operations = institution.getChild("OperationsCost").getChildren(
          "Operation");
      Iterator operationsIter = operations.iterator();

      while (operationsIter.hasNext()) {
        Element operation = (Element) operationsIter.next();
        vectorInstitutionWithOperationsName.add(operation.getAttributeValue(
            "name"));
        vectorInstitutionWithOperationsCost.add(operation.getAttributeValue(
            "cost"));
      }
      vectorCollection.put("vector" + vectorNumber,
                           vectorInstitutionWithOperationsName);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber,
                           vectorInstitutionWithOperationsCost);
      vectorNumber++;

      //For each institution, it creates one column with the list of the operators
      List operators = institution.getChild("Operators").getChildren("Operator");
      Iterator operatorsIter = operators.iterator();

      Vector vectorOperatorsInInstitution = new Vector();
      vectorOperatorsInInstitution.add("Players on Institution");
      vectorOperatorsInInstitution.add(name);

      while (operatorsIter.hasNext()) {
        Element operator = (Element) operatorsIter.next();
        vectorOperatorsInInstitution.add("Player: " +
                                         operator.getAttributeValue("name"));
        vectorOperatorsInInstitution.add("Visibility of orderbook: " +
                                         operator.getAttributeValue(
                                             "orderbookVisibility"));

        List listOfGrantedOperations = operator.getChildren("GrantedOperation");
        Iterator listOfGrantedOperationsIter = listOfGrantedOperations.iterator();
        while (listOfGrantedOperationsIter.hasNext()) {
          Element grantedOperation = (Element) listOfGrantedOperationsIter.next();
          vectorOperatorsInInstitution.add(grantedOperation.getAttributeValue(
              "name"));
        }
      }

      vectorCollection.put("vector" + vectorNumber,
                           vectorOperatorsInInstitution);
      vectorNumber++;

    }
    AnalysisToolsCore.logger.debug("Exportation of the institutions done.");
  }

  /**This Method is used to export the <b>Descritpion of the Scenario</b> played.
   * Interesting caracteristics of a scenario are the name of the <b>Operators</b>
   * and the <b>Operations<b> they are allowed to pass.
   * The method writes one column per Operator.
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void scenario(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Scenario");
    Element experimentNode = doc.getRootElement();
    Element scenario = experimentNode.getChild("Setup").getChild("Scenario");

    // To align the content: The HashMap BlanksToAdd contains pieces of information about the size
    //of the blanks needed in the vectors. They save the information once we go out from the loops.
    HashMap scenarioHashMap = new HashMap();
    HashMap importantNumbers = new HashMap();
    //Some counters to enable the alignement
    int numberOfPlayertypes = 0;
    int maxNumberOfOperators = 0, numberOfOperators,
        maxNumberOfOperations = 0, numberOfOperations;

    //To Get one player type.
    List playertypes = scenario.getChildren("PlayerType");
    Iterator playertypesIter = playertypes.iterator();

    while (playertypesIter.hasNext()) {
      numberOfPlayertypes++;
      //the first part of the datas (contains the operators)
      Vector vectorScenario1 = new Vector();
      //the 2nd part of the datas (contains the operations)
      Vector vectorScenario2 = new Vector();
      //the 3rd part of the datas (the end)
      Vector vectorScenario3 = new Vector();

      vectorScenario1.add("Scenario");
      Element playertype = (Element) playertypesIter.next();
      vectorScenario1.add("Player Type '" + playertype.getAttributeValue("name") +
                          "'");
      AnalysisToolsCore.logger.debug("New column for player type " +
                                     numberOfPlayertypes + ": '" +
                                     playertype.getAttributeValue("name") + "'");

      //Get the operators played by one player type.
      Element operatorsPlayed = playertype.getChild("OperatorsPlayed");
      List operators = operatorsPlayed.getChildren("Operator");
      Iterator operatorsIter = operators.iterator();
      vectorScenario1.add("Operators played:");

      numberOfOperators = 0;

      while (operatorsIter.hasNext()) {

        numberOfOperators++;
        Element operator = (Element) operatorsIter.next();
        vectorScenario1.add(operator.getAttributeValue("name"));
      }

      if (maxNumberOfOperators < numberOfOperators) {
        maxNumberOfOperators =
            numberOfOperators;
      }
      importantNumbers.put("NbOfOperators" + numberOfPlayertypes,
                           new Integer(numberOfOperators));
      AnalysisToolsCore.logger.debug(importantNumbers.get("NbOfOperators" +
          numberOfPlayertypes));

      //Get the initial portfolio of the player type.
      Element initialPortfolio = playertype.getChild("Portfolio");
      vectorScenario2.add("Cash at the begining:");
      vectorScenario2.add(initialPortfolio.getAttributeValue("cash"));
      List owning = initialPortfolio.getChildren("Owning");
      Iterator owningIter = owning.iterator();

      numberOfOperations = 0;

      while (owningIter.hasNext()) {

        numberOfOperations++;
        Element assetInitiallyOwned = (Element) owningIter.next();
        vectorScenario2.add("Quantity of " +
                            assetInitiallyOwned.getAttributeValue("asset") +
                            " at the begining:");
        vectorScenario2.add(assetInitiallyOwned.getAttributeValue("qtty"));
      }

      if (maxNumberOfOperations < numberOfOperations) {
        maxNumberOfOperations =
            numberOfOperations;
      }
      importantNumbers.put("NbOfOperations" + numberOfPlayertypes,
                           new Integer(numberOfOperations));
      AnalysisToolsCore.logger.debug(importantNumbers.get("NbOfOperations" +
          numberOfPlayertypes));

      // Get the pieces of information about dividends for each asset.
      List listOfDividendinfo = playertype.getChildren("DividendInfo");
      Iterator listOfDividendinfoIter = listOfDividendinfo.iterator();

      while (listOfDividendinfoIter.hasNext()) {
        Element dividendInfo = (Element) listOfDividendinfoIter.next();

        vectorScenario3.add(dividendInfo.getAttributeValue("asset") +
                            "'s Dividend information:");
        vectorScenario3.add("Info about dividend: " +
                            dividendInfo.getChild(
                                "DividendDetailledProperties").
                            getAttributeValue("value"));
        vectorScenario3.add("Window's size: " +
                            dividendInfo.getChild("WindowSize").
                            getAttributeValue("value"));
        vectorScenario3.add("Info about window size: " +
                            dividendInfo.getChild("DisplayWindowSize").
                            getAttributeValue("value"));
        vectorScenario3.add("Session length displayed: " +
                            dividendInfo.getChild("DisplaySessionLength").
                            getAttributeValue("value"));
        vectorScenario3.add("HV for experiment displayed: " +
                            dividendInfo.getChild(
                                "DisplayHoldingValueForExperiment").
                            getAttributeValue("value"));
        vectorScenario3.add("HV for window displayed: " +
                            dividendInfo.getChild(
                                "DisplayHoldingValueForWindow").
                            getAttributeValue("value"));

        vectorScenario3.add("Costs of operations: " +
                            dividendInfo.getChild(
                                "DisplayOperationsCosts").getAttributeValue(
            "value"));

      }

      // Save of the vectors before begining a new loop.
      scenarioHashMap.put("vectorScenario1" + numberOfPlayertypes,
                          vectorScenario1);
      scenarioHashMap.put("vectorScenario2" + numberOfPlayertypes,
                          vectorScenario2);
      scenarioHashMap.put("vectorScenario3" + numberOfPlayertypes,
                          vectorScenario3);
      AnalysisToolsCore.logger.debug(vectorScenario1 + " , " + vectorScenario2 +
                                     " , " + vectorScenario3);
      AnalysisToolsCore.logger.debug(scenarioHashMap.get("vectorScenario1" +
          numberOfPlayertypes) + " , " +
                                     scenarioHashMap.get("vectorScenario2" +
          numberOfPlayertypes) + " , " +
                                     scenarioHashMap.get("vectorScenario3" +
          numberOfPlayertypes));

    }

    // The pieces of information are now put at the same level in only one vector (for one Player Type) by a
    // concatenation of the vector's content and some blanks well placed.
    AnalysisToolsCore.logger.debug(scenarioHashMap);
    AnalysisToolsCore.logger.debug(importantNumbers);

    int i;
    for (i = 1; i <= numberOfPlayertypes; i++) {

      int added, blanks;
      AnalysisToolsCore.logger.debug("Alignement of the datas for player type " +
                                     i);

      Vector vectorSc1;
      vectorSc1 = (Vector) scenarioHashMap.get("vectorScenario1" + i);
      AnalysisToolsCore.logger.debug("vectorScenario1" + i + " : " +
                                     (Vector) scenarioHashMap.get(
                                         "vectorScenario1" + i));
      Vector vectorSc2;
      vectorSc2 = (Vector) scenarioHashMap.get("vectorScenario2" + i);
      AnalysisToolsCore.logger.debug("vectorScenario2" + i + " : " +
                                     (Vector) scenarioHashMap.get(
                                         "vectorScenario2" + i));
      Vector vectorSc3;
      vectorSc3 = (Vector) scenarioHashMap.get("vectorScenario3" + i);
      AnalysisToolsCore.logger.debug("vectorScenario3" + i + " : " +
                                     (Vector) scenarioHashMap.get(
                                         "vectorScenario3" + i));

      blanks = (maxNumberOfOperators -
                ( (Integer) importantNumbers.get( (String) ("NbOfOperators" + i))).
                intValue());
      for (added = 0; added < blanks; added++) {

        vectorSc1.add(" ");
      }

      for (added = 0; added < vectorSc2.size(); added++) {
        vectorSc1.add(vectorSc2.get(added));
      }

      blanks = 2 *
          (maxNumberOfOperations -
           ( (Integer) importantNumbers.get("NbOfOperations" + i)).intValue());
      for (added = 0; added < blanks; added++) {
        vectorSc1.add(" ");
      }

      for (added = 0; added < vectorSc3.size(); added++) {
        vectorSc1.add(vectorSc3.get(added));
      }

      vectorCollection.put("vector" + vectorNumber, vectorSc1);
      vectorNumber++;

    }

    AnalysisToolsCore.logger.debug(
        "Exportation of the Scenario's Parameters done.");
  }

  /**This Method is used to export the <b>List of the Deals</b> done during the game.
   * The method writes, for each institution,the deals in five columns : (seller, buyer, time, cash, quantity).
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void deal(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Deal");
    Element experimentNode = doc.getRootElement();
    List periods = experimentNode.getChildren("Period");
    List institutions = experimentNode.getChild("Setup").getChildren(
        "Institution");
    Iterator institutionsIter = institutions.iterator();
    float periodDuration = Float.parseFloat(experimentNode.getChild("Setup").
                                            getChild("GeneralParameters").
                                            getChildText("PeriodDuration"));
    int sessionDuration;
    while (institutionsIter.hasNext()) {
      sessionDuration = 0;
      Vector vectorTime = new Vector();
      Vector vectorCash = new Vector();
      Vector vectorQuantity = new Vector();
      Vector vectorBuyer = new Vector();
      Vector vectorSeller = new Vector();
      Element institution = (Element) institutionsIter.next();
      String assetName = institution.getAttributeValue("name");
      Iterator periodsIter = periods.iterator();

      vectorTime.add("Deal");
      vectorCash.add("Deal");
      vectorQuantity.add("Deal");
      vectorSeller.add("Deal");
      vectorBuyer.add("Deal");
      vectorTime.add(assetName);
      vectorCash.add(assetName);
      vectorQuantity.add(assetName);
      vectorSeller.add(assetName);
      vectorBuyer.add(assetName);
      vectorSeller.add("Seller");
      vectorBuyer.add("Buyer");
      vectorTime.add("Time(s)");
      vectorCash.add("Cash");
      vectorQuantity.add("Quantity");

      while (periodsIter.hasNext()) {
        Element period = (Element) periodsIter.next();
        List deals = period.getChildren("Deal");
        Iterator dealIter = deals.iterator();

        while (dealIter.hasNext()) {
          Element deal = (Element) dealIter.next();
          if (0 ==
              assetName.compareTo(deal.getChild("Deal").getAttributeValue(
                  "institution"))) {
            float time = (Float.parseFloat(deal.getChild("Deal").
                                           getAttributeValue("timestamp")) /
                          1000) + sessionDuration;
            vectorTime.add(Float.toString(time));
            vectorCash.add( (deal.getChild("Deal").getAttributeValue("price")));
            vectorQuantity.add(deal.getChild("Deal").getAttributeValue(
                "quantity"));
            vectorSeller.add(deal.getChild("Deal").getAttributeValue("seller"));
            vectorBuyer.add(deal.getChild("Deal").getAttributeValue("buyer"));
          }
        }
        sessionDuration += periodDuration;
      }
      vectorCollection.put("vector" + vectorNumber, vectorSeller);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorBuyer);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorTime);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorCash);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorQuantity);
      vectorNumber++;

    }
    AnalysisToolsCore.logger.debug("Exportation of the Deals done.");
  }

  /**This Method is used to export the <b>Portfolio</b> of players.
   * The method writes, for each player, in two columns, his portfolio at the <b>begining<b> and the <b>end<b>.
   * For one player, there are 2 columns (Asset, Quantity) . The portfolios are presented as "blocks"
   * and they follow each other vertically taken from a precise time of the game.
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void portfolio(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Portfolio");
    Element experimentNode = doc.getRootElement();
    List periods = experimentNode.getChildren("Period");

    Iterator periodsIter = periods.iterator();
    HashMap hashMapVector = new HashMap();
    List listportfolios = experimentNode.getChild("Period").getChildren(
        "Portfolio");
    int numberofplayers = listportfolios.size();
    for (int i = 0; i < numberofplayers; i++) {
      Vector vectorAsset = new Vector();
      vectorAsset.add("Portfolio");
      vectorAsset.add( ( (Element) listportfolios.get(i)).getAttributeValue(
          "player"));
      Vector vectorQuantity = new Vector();
      vectorQuantity.add("Portfolio");
      vectorQuantity.add( ( (Element) listportfolios.get(i)).getAttributeValue(
          "type"));
      hashMapVector.put("VectorAsset" + i, vectorAsset);
      hashMapVector.put("VectorQuantity" + i, vectorQuantity);
    }
    int periodNum = 1;
    while (periodsIter.hasNext()) {
      for (int i = 0; i < numberofplayers; i++) {
        ( (Vector) hashMapVector.get("VectorAsset" + i)).add(" ");
        ( (Vector) hashMapVector.get("VectorQuantity" + i)).add(" ");
      }
      Element period = (Element) periodsIter.next();
      List portfolios = period.getChildren("Portfolio");
      Iterator portfoliosIter = portfolios.iterator();

      int compt = 0;
      while (portfoliosIter.hasNext()) {
        Element portfolio = (Element) portfoliosIter.next();
        ( (Vector) hashMapVector.get("VectorAsset" + compt)).add("Period " +
            periodNum + ": ");
        ( (Vector) hashMapVector.get("VectorQuantity" + compt)).add(" ");
        ( (Vector) hashMapVector.get("VectorAsset" + compt)).add("Cash :");
        ( (Vector) hashMapVector.get("VectorQuantity" + compt)).add(portfolio.
            getAttributeValue("cash"));
        ( (Vector) hashMapVector.get("VectorAsset" + compt)).add("Assets");
        ( (Vector) hashMapVector.get("VectorQuantity" + compt)).add("Quantity");

        List ownings = portfolio.getChildren("Owning");
        Iterator owningsIter = ownings.iterator();
        while (owningsIter.hasNext()) {
          Element owning = (Element) owningsIter.next();
          ( (Vector) hashMapVector.get("VectorAsset" + compt)).add(owning.
              getAttributeValue("asset"));
          ( (Vector) hashMapVector.get("VectorQuantity" + compt)).add(owning.
              getAttributeValue("qtty"));
        }
        if (compt < numberofplayers - 1) {
          compt++;
        }
        else {
          compt = 0;
        }
      }
      periodNum++;
    }
    for (int i = 0; i < numberofplayers; i++) {
      vectorCollection.put("vector" + vectorNumber,
                           hashMapVector.get("VectorAsset" + i));
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber,
                           hashMapVector.get("VectorQuantity" + i));
      vectorNumber++;
    }
    AnalysisToolsCore.logger.debug("Exportation of the Portfolio done.");
  }

  /**This Method is used to export the <b>List of the Operations</b> made by Players during the game.
   * The method writes, for each institution, in five columns, the operations :(Emitter, ask or bid, time, cash, quantity)
   * @param doc Document
   * @see the struture of an XML backup of the experiment
   * */

  private void operations(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Operations");
    Element experimentNode = doc.getRootElement();
    List periods = experimentNode.getChildren("Period");
    List institutions = experimentNode.getChild("Setup").getChildren(
        "Institution");
    Iterator institutionsIter = institutions.iterator();
    float periodDuration = Float.parseFloat(experimentNode.getChild("Setup").
                                            getChild("GeneralParameters").
                                            getChildText("PeriodDuration"));
    int sessionDuration;
    while (institutionsIter.hasNext()) {
      sessionDuration = 0;
      Vector vectorTime = new Vector();
      Vector vectorCash = new Vector();
      Vector vectorQuantity = new Vector();
      Vector vectorSide = new Vector();
      Vector vectorEmitter = new Vector();
      Vector vectorType = new Vector();
      Vector vectorId = new Vector();
      Element institution = (Element) institutionsIter.next();
      String assetName = institution.getAttributeValue("name");
      Iterator periodsIter = periods.iterator();
      vectorTime.add("Operation");
      vectorCash.add("Operation");
      vectorQuantity.add("Operation");
      vectorSide.add("Operation");
      vectorEmitter.add("Operation");
      vectorType.add("Operation");
      vectorId.add("Operation");
      vectorTime.add(assetName);
      vectorCash.add(assetName);
      vectorQuantity.add(assetName);
      vectorSide.add(assetName);
      vectorEmitter.add(assetName);
      vectorType.add(assetName);
      vectorId.add(assetName);
      vectorEmitter.add("Emitter");
      vectorTime.add("Time(s)");
      vectorCash.add("Cash");
      vectorQuantity.add("Quantity");
      vectorSide.add("Side");
      vectorType.add("Type");
      vectorId.add("Id");

      while (periodsIter.hasNext()) {
        Element period = (Element) periodsIter.next();
        List operations = period.getChildren("Operation");
        Iterator operationIter = operations.iterator();

        while (operationIter.hasNext()) {
          Element operation = (Element) operationIter.next();
          if (operation.getChild("LimitOrder") != null) {
            if (assetName.equals(operation.getAttributeValue("institution"))) {
              vectorType.add(operation.getAttributeValue("type"));
              vectorId.add(operation.getChild("Order").
                           getAttributeValue("id"));
              float time = (Float.parseFloat(operation.getChild("Order").
                                             getAttributeValue("timestamp")) /
                            1000) + sessionDuration;
              vectorTime.add(Float.toString(time));
              vectorEmitter.add(operation.getAttributeValue("emitter"));

              vectorCash.add( (operation.getChild("LimitOrder").
                               getAttributeValue("price")));
              vectorQuantity.add(operation.getChild("LimitOrder").
                                 getAttributeValue("quantity"));
              if ("0".equals(operation.getChild("Order").getAttributeValue(
                  "side"))) {
                vectorSide.add("ask");
              }
              else {
                vectorSide.add("bid");
              }
            }
          }
          if (operation.getChild("MarketOrder") != null) {
            if (assetName.equals(operation.getAttributeValue("institution"))) {
              vectorType.add(operation.getAttributeValue("type"));
              vectorId.add(operation.getChild("Order").
                           getAttributeValue("id"));
              float time = (Float.parseFloat(operation.getChild("Order").
                                             getAttributeValue("timestamp")) /
                            1000) + sessionDuration;
              vectorTime.add(Float.toString(time));
              vectorEmitter.add(operation.getAttributeValue("emitter"));
              vectorCash.add("");
              vectorQuantity.add(operation.getChild("MarketOrder").
                                 getAttributeValue("quantity"));
              if ("0".equals(operation.getChild("Order").getAttributeValue(
                  "side"))) {
                vectorSide.add("ask");
              }
              else {
                vectorSide.add("bid");
              }
            }
          }
          if (operation.getChild("BestLimitOrder") != null) {
            if (assetName.equals(operation.getAttributeValue("institution"))) {
              vectorType.add(operation.getAttributeValue("type"));
              vectorId.add(operation.getChild("Order").
                           getAttributeValue("id"));
              float time = (Float.parseFloat(operation.getChild("Order").
                                             getAttributeValue("timestamp")) /
                            1000) + sessionDuration;
              vectorTime.add(Float.toString(time));
              vectorEmitter.add(operation.getAttributeValue("emitter"));
              vectorCash.add("");
              vectorQuantity.add(operation.getChild("BestLimitOrder").
                                 getAttributeValue("quantity"));
              if ("0".equals(operation.getChild("Order").getAttributeValue(
                  "side"))) {
                vectorSide.add("ask");
              }
              else {
                vectorSide.add("bid");
              }
            }
          }
          if (operation.getChild("DeleteOrder") != null) {
            if (assetName.equals(operation.getAttributeValue("institution"))) {
              vectorType.add(operation.getAttributeValue("type"));
              vectorEmitter.add(operation.getAttributeValue("emitter"));
              vectorSide.add("Order Id = " +
                             operation.getChild("DeleteOrder").
                             getAttributeValue("orderId"));
              vectorId.add("");
              vectorTime.add("");
              vectorCash.add("");
              vectorQuantity.add("");
            }
          }
        }
        sessionDuration += periodDuration;
      }
      vectorCollection.put("vector" + vectorNumber, vectorId);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorType);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorEmitter);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorSide);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorTime);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorCash);
      vectorNumber++;
      vectorCollection.put("vector" + vectorNumber, vectorQuantity);
      vectorNumber++;
    }
    AnalysisToolsCore.logger.debug("Exportation of the Opertations done.");
  }

  private void dividend(Document doc) {
    AnalysisToolsCore.logger.debug("Open XML for Dividend");

    Element experimentNode = doc.getRootElement();
    List periods = experimentNode.getChildren("Period");
    Vector vectorAsset = new Vector();
    Vector vectorDividend = new Vector();
    vectorAsset.add("Dividends");
    vectorDividend.add("Dividends");
    vectorAsset.add("Asset name");
    vectorDividend.add("Dividend");
    Iterator periodsIter = periods.iterator();
    int i = 0;
    while (periodsIter.hasNext()) {
      vectorAsset.add("Period " + i);
      vectorDividend.add(" ");
      i++;
      Element period = (Element) periodsIter.next();
      List dividends = period.getChild("Dividends").getChildren("dividend");
      Iterator dividendsIter = dividends.iterator();
      while (dividendsIter.hasNext()) {
        Element dividend = (Element) dividendsIter.next();
        vectorAsset.add(dividend.getAttributeValue("asset"));
        vectorDividend.add(dividend.getAttributeValue("value"));
      }

    }
    vectorCollection.put("vector" + vectorNumber, vectorAsset);
    vectorNumber++;
    vectorCollection.put("vector" + vectorNumber, vectorDividend);
    vectorNumber++;

    AnalysisToolsCore.logger.debug("Exportation of the Portfolio done.");
  }

  private void jbInit() throws Exception {
  }

}
