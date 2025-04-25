// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import jessx.business.event.InstitutionEvent;
import jessx.business.event.InstitutionListener;
import jessx.business.event.AssetEvent;
import jessx.business.event.AssetListener;
import java.util.Iterator;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.util.Map;
import jessx.utils.Utils;
import org.jdom.Content;
import java.util.Vector;
import org.jdom.Element;
import java.util.HashMap;

public abstract class BusinessCore
{
    private static HashMap institutions;
    private static HashMap assets;
    private static Scenario scenario;
    private static Element chatNode;
    private static Vector assetsListeners;
    private static Vector institutionsListeners;
    private static GeneralParameters generalParam;
    
    static {
        BusinessCore.institutions = new HashMap();
        BusinessCore.assets = new HashMap();
        BusinessCore.scenario = new Scenario();
        BusinessCore.assetsListeners = new Vector();
        BusinessCore.institutionsListeners = new Vector();
    }
    
    public static void setGeneralParameters(final GeneralParameters genParams) {
        BusinessCore.generalParam = genParams;
    }
    
    public static GeneralParameters getGeneralParameters() {
        return BusinessCore.generalParam;
    }
    
    public static Scenario getScenario() {
        return BusinessCore.scenario;
    }
    
    public static Institution getInstitution(String institutionName) {
        return (Institution)institutions.get(institutionName);
     }
        
    public static HashMap getInstitutions() {
        return BusinessCore.institutions;
    }
    
    public static Asset getAsset(final String assetName) {
        return (Asset) BusinessCore.assets.get(assetName);
    }
    
    public static HashMap getAssets() {
        return BusinessCore.assets;
    }
    
    public static void addAsset(final Asset asset) {
        if (asset != null) {
            BusinessCore.assets.put(asset.getAssetName(), asset);
            fireAssetAdded(asset);
        }
    }
    
    public static void removeAsset(final Asset asset) {
        if (asset != null && BusinessCore.assets.containsValue(asset)) {
            BusinessCore.assets.remove(asset.getAssetName());
            fireAssetRemoved(asset);
        }
    }
    
    public static void addInstitution(final Institution institution) {
        if (institution != null) {
            BusinessCore.institutions.put(institution.getName(), institution);
            fireInstitutionAdded(institution);
        }
    }
    
    public static void removeInstitution(final Institution institution) {
        if (institution != null && BusinessCore.institutions.containsValue(institution)) {
            BusinessCore.institutions.remove(institution.getName());
            fireInstitutionRemoved(institution);
        }
    }
    
    public static void saveToXml(final Element rootNode) {
        final Element genParamNode = new Element("GeneralParameters");
        getGeneralParameters().saveToXml(genParamNode);
        rootNode.addContent(genParamNode);
        final Vector assetKeys = Utils.convertAndSortMapToVector(getAssets());
        for (int keysCount = assetKeys.size(), i = 0; i < keysCount; ++i) {
            final Element asset = new Element("Asset");
            Asset.saveAssetToXml(asset, getAsset((String) assetKeys.get(i)));
            rootNode.addContent(asset);
        }
        final Vector instKeys = Utils.convertAndSortMapToVector(getInstitutions());
        for (int keysCount = instKeys.size(), j = 0; j < keysCount; ++j) {
            final Element institution = new Element("Institution");
            Institution.saveInstitutionToXml(institution, getInstitution((String) instKeys.get(j)));
            rootNode.addContent(institution);
        }
        final Element scenarioNode = new Element("Scenario");
        getScenario().saveToXml(scenarioNode);
        rootNode.addContent(scenarioNode);
        rootNode.addContent(BusinessCore.chatNode = new Element("Chat"));
    }
    
    public static Element getElementToSaveChat() {
        return BusinessCore.chatNode;
    }
    
    public static void loadFromXml(final Element root, final JFrame parentFrame) {
        final Element genParam = root.getChild("GeneralParameters");
        if (genParam == null) {
            Utils.logger.error("Invalid xml format: GeneralParameters nodes not found.");
            JOptionPane.showMessageDialog(parentFrame, "The file you choose is incorrect.", "Error", 2);
            return;
        }
        getGeneralParameters().loadFromXml(genParam);
        final Iterator assetNodes = root.getChildren("Asset").iterator();
        while (assetNodes.hasNext()) {
            final Asset asset = Asset.loadAssetFromXml((Element) assetNodes.next());
            addAsset(asset);
        }
        final Iterator institutionNodes = root.getChildren("Institution").iterator();
        while (institutionNodes.hasNext()) {
            final Institution institution = Institution.loadInstitutionFromXml((Element) institutionNodes.next());
            addInstitution(institution);
        }
        final Element scenario = root.getChild("Scenario");
        if (scenario == null) {
            Utils.logger.error("Invalid xml files: scenario node not found.");
            return;
        }
        getScenario().loadFromXml(scenario);
    }
    
    protected static void fireAssetAdded(final Asset asset) {
        for (int i = 0; i < BusinessCore.assetsListeners.size(); ++i) {
            ((AssetListener) BusinessCore.assetsListeners.elementAt(i)).assetsModified(new AssetEvent(asset.getAssetName(), 1));
        }
    }
    
    protected static void fireAssetRemoved(final Asset asset) {
        for (int i = 0; i < BusinessCore.assetsListeners.size(); ++i) {
            ((AssetListener) BusinessCore.assetsListeners.elementAt(i)).assetsModified(new AssetEvent(asset.getAssetName(), 0));
        }
    }
    
    protected static void fireInstitutionAdded(final Institution institution) {
        for (int i = 0; i < BusinessCore.institutionsListeners.size(); ++i) {
            ((InstitutionListener) BusinessCore.institutionsListeners.elementAt(i)).institutionsModified(new InstitutionEvent(institution.getName(), 1));
        }
    }
    
    protected static void fireInstitutionRemoved(final Institution institution) {
        for (int i = 0; i < BusinessCore.institutionsListeners.size(); ++i) {
            ((InstitutionListener) BusinessCore.institutionsListeners.elementAt(i)).institutionsModified(new InstitutionEvent(institution.getName(), 0));
        }
    }
    
    public static void addAssetListener(final AssetListener listener) {
        BusinessCore.assetsListeners.add(listener);
    }
    
    public static void addInstitutionListener(final InstitutionListener listener) {
        BusinessCore.institutionsListeners.add(listener);
    }
    
    public static void removeAssetListener(final AssetListener listener) {
        BusinessCore.assetsListeners.remove(listener);
    }
    
    public static void removeInstitutionListener(final InstitutionListener listener) {
        BusinessCore.institutionsListeners.remove(listener);
    }
}
