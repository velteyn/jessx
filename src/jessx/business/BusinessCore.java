package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jessx.business.event.AssetEvent;
import jessx.business.event.AssetListener;
import jessx.business.event.InstitutionEvent;
import jessx.business.event.InstitutionListener;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;

public abstract class BusinessCore {
  private static HashMap institutions = new HashMap<Object, Object>();
  
  private static HashMap assets = new HashMap<Object, Object>();
  
  private static Scenario scenario = new Scenario();
  
  private static Element chatNode;
  
  private static Vector assetsListeners = new Vector();
  
  private static Vector institutionsListeners = new Vector();
  
  private static GeneralParameters generalParam;
  
  public static void setGeneralParameters(GeneralParameters genParams) {
    generalParam = genParams;
  }
  
  public static GeneralParameters getGeneralParameters() {
    return generalParam;
  }
  
  public static Scenario getScenario() {
    return scenario;
  }
  
  public static Institution getInstitution(String institutionName) {
    return (Institution)institutions.get(institutionName);
  }
  
  public static HashMap getInstitutions() {
    return institutions;
  }
  
  public static Asset getAsset(String assetName) {
    return (Asset)assets.get(assetName);
  }
  
  public static HashMap getAssets() {
    return assets;
  }
  
  public static void addAsset(Asset asset) {
    if (asset != null) {
      assets.put(asset.getAssetName(), asset);
      fireAssetAdded(asset);
    } 
  }
  
  public static void removeAsset(Asset asset) {
    if (asset != null && 
      assets.containsValue(asset)) {
      assets.remove(asset.getAssetName());
      fireAssetRemoved(asset);
    } 
  }
  
  public static void addInstitution(Institution institution) {
    if (institution != null) {
      institutions.put(institution.getName(), institution);
      fireInstitutionAdded(institution);
    } 
  }
  
  public static void removeInstitution(Institution institution) {
    if (institution != null && 
      institutions.containsValue(institution)) {
      institutions.remove(institution.getName());
      fireInstitutionRemoved(institution);
    } 
  }
  
  public static void saveToXml(Element rootNode) {
    Element genParamNode = new Element("GeneralParameters");
    getGeneralParameters().saveToXml(genParamNode);
    rootNode.addContent((Content)genParamNode);
    Vector<String> assetKeys = Utils.convertAndSortMapToVector(getAssets());
    int keysCount = assetKeys.size();
    for (int i = 0; i < keysCount; i++) {
      Element asset = new Element("Asset");
      Asset.saveAssetToXml(asset, getAsset(assetKeys.get(i)));
      rootNode.addContent((Content)asset);
    } 
    Vector<String> instKeys = Utils.convertAndSortMapToVector(getInstitutions());
    keysCount = instKeys.size();
    for (int j = 0; j < keysCount; j++) {
      Element institution = new Element("Institution");
      Institution.saveInstitutionToXml(institution, 
          getInstitution(instKeys.get(j)));
      rootNode.addContent((Content)institution);
    } 
    Element scenarioNode = new Element("Scenario");
    getScenario().saveToXml(scenarioNode);
    rootNode.addContent((Content)scenarioNode);
    chatNode = new Element("Chat");
    rootNode.addContent((Content)chatNode);
  }
  
  public static Element getElementToSaveChat() {
    return chatNode;
  }
  
  public static void loadFromXml(Element root, JFrame parentFrame) {
    Element genParam = root.getChild("GeneralParameters");
    if (genParam == null) {
      Utils.logger.error("Invalid xml format: GeneralParameters nodes not found.");
      JOptionPane.showMessageDialog(parentFrame, "The file you choose is incorrect.", "Error", 2);
      return;
    } 
    getGeneralParameters().loadFromXml(genParam);
    Iterator<Element> assetNodes = root.getChildren("Asset").iterator();
    while (assetNodes.hasNext()) {
      Asset asset = Asset.loadAssetFromXml(assetNodes.next());
      addAsset(asset);
    } 
    Iterator<Element> institutionNodes = root.getChildren("Institution").iterator();
    while (institutionNodes.hasNext()) {
      Institution institution = 
        Institution.loadInstitutionFromXml(institutionNodes.next());
      addInstitution(institution);
    } 
    Element scenario = root.getChild("Scenario");
    if (scenario == null) {
      Utils.logger.error("Invalid xml files: scenario node not found.");
      return;
    } 
    getScenario().loadFromXml(scenario);
  }
  
  protected static void fireAssetAdded(Asset asset) {
    for (int i = 0; i < assetsListeners.size(); i++)
      ((AssetListener)assetsListeners.elementAt(i))
        .assetsModified(new AssetEvent(asset.getAssetName(), 
            1)); 
  }
  
  protected static void fireAssetRemoved(Asset asset) {
    for (int i = 0; i < assetsListeners.size(); i++)
      ((AssetListener)assetsListeners.elementAt(i))
        .assetsModified(new AssetEvent(asset.getAssetName(), 
            0)); 
  }
  
  protected static void fireInstitutionAdded(Institution institution) {
    for (int i = 0; i < institutionsListeners.size(); i++)
      ((InstitutionListener)institutionsListeners.elementAt(i))
        .institutionsModified(new InstitutionEvent(institution.getName(), 
            1)); 
  }
  
  protected static void fireInstitutionRemoved(Institution institution) {
    for (int i = 0; i < institutionsListeners.size(); i++)
      ((InstitutionListener)institutionsListeners.elementAt(i))
        .institutionsModified(new InstitutionEvent(institution.getName(), 
            0)); 
  }
  
  public static void addAssetListener(AssetListener listener) {
    assetsListeners.add(listener);
  }
  
  public static void addInstitutionListener(InstitutionListener listener) {
    institutionsListeners.add(listener);
  }
  
  public static void removeAssetListener(AssetListener listener) {
    assetsListeners.remove(listener);
  }
  
  public static void removeInstitutionListener(InstitutionListener listener) {
    institutionsListeners.remove(listener);
  }
}
