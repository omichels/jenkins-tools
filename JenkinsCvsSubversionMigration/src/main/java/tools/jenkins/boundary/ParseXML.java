package tools.jenkins.boundary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.ElementFilter;
import org.jdom2.filter.Filter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import tools.jenkins.control.MigrationProperties;
import tools.jenkins.entity.CvsRepository;
import tools.jenkins.entity.CvsRepositoryItem;

public class ParseXML {
	
	static List<CvsRepository> cvsRepositories = new ArrayList<>();

	public static void main(String[] args) throws JDOMException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		//File xmlFile = new File(args[0]);
		File xmlFile = new File("CVS_DMBRS_GesamtMig_Automigration_Generate_JC_config.xml");
		Document document = saxBuilder.build(xmlFile);

		Element rootElement = document.getRootElement();
		Element scm = rootElement.getChild("scm");
 
        List<Element> repositories = scm.getChildren("repositories");
        for (Element child : repositories) {
				List<Element> hudsonScmCvsRepository = child.getChildren("hudson.scm.CvsRepository");
				for (Element hudsonScmCvsRepo : hudsonScmCvsRepository) {
					CvsRepository repository = null;
					for (Element repoChild : hudsonScmCvsRepo.getChildren()) {
						if ("cvsRoot".equals(repoChild.getName())) {
							repository = new CvsRepository(repoChild.getText());
						} else if ("repositoryItems".equals(repoChild.getName())) {
							List<CvsRepositoryItem> l =  extractCvsModules(repoChild, new ElementFilter("hudson.scm.CvsRepositoryItem"));
							repository.addItems(l);	
							cvsRepositories.add(repository);
						}
					}
				}
				
        }
        
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        if (scm.getAttribute("class").getValue().endsWith("NullSCM")) {
        	// do nothing
        } else {
        	scm.removeContent();
        	scm.setAttribute("class", "hudson.scm.SubversionSCM");
        	scm.setAttribute("plugin", "subversion@1.45");
        	Element locations = new Element("locations");
        	scm.addContent(locations);
        	for (CvsRepository element : cvsRepositories) {
        		for (CvsRepositoryItem cvsRepositoryItem : element.getItems()) {
        			Element scmModuleLocation = new Element("hudson.scm.SubversionSCM_-ModuleLocation");
        			Element remote = new Element("remote");
        			Element local = new Element("local");
        			Element depthOption = new Element("depthOption"); depthOption.setText("infinity");
        			Element ignoreExternalsOption = new Element("ignoreExternalsOption"); ignoreExternalsOption.setText("false");
        			String baseURL = MigrationProperties.getInstance().getTranslatedValue(element.getCvsRoot());
        			String path = MigrationProperties.getInstance().getTranslatedValue(cvsRepositoryItem.getRemoteName());
        			remote.setText(baseURL + path);
        			local.setText(cvsRepositoryItem.getLocalName());
        			scmModuleLocation.addContent(remote);
        			scmModuleLocation.addContent(local);
        			scmModuleLocation.addContent(depthOption);
        			scmModuleLocation.addContent(ignoreExternalsOption);
        			locations.addContent(scmModuleLocation);
        		}
        	}
        }
        System.out.println(outputter.outputString(document));
			
		
		
	}

	private static List<CvsRepositoryItem> extractCvsModules(Element element2,
			Filter<Element> filter) {
		List<CvsRepositoryItem> cvsRepoItems = new ArrayList<>();
		List<Element> items = element2.getContent(filter);
		for (Element item : items) {
			for (Element modulesElem : item.getChildren()) {
				if ("modules".equals(modulesElem.getName())) {
					for (Element hudsonScmCvsModule : modulesElem.getChildren("hudson.scm.CvsModule")) {
						String locationtype = modulesElem.getParentElement().getChildren("location").get(0).getChildText("locationType");
						String locationName = modulesElem.getParentElement().getChildren("location").get(0).getChildText("locationName");
						if (locationName == null)
							locationName = "";
						cvsRepoItems.add(new CvsRepositoryItem(hudsonScmCvsModule.getChildText("localName"),
								hudsonScmCvsModule.getChildText("remoteName"),locationtype, locationName));

					}
				}
			}
			return cvsRepoItems;
		}
		return new ArrayList<CvsRepositoryItem>();
	}

}
