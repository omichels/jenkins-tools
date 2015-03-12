package tools.jenkins.control;
 
import java.io.IOException;
import java.io.InputStream;
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

import tools.jenkins.entity.CvsRepository;
import tools.jenkins.entity.CvsRepositoryItem;

/**
 * migrates jenkins jobs from cvs to svn 
 *
 */
public class JobMigrater {
	
	private SubversionPathGenerator svnPathGenerator;

	public JobMigrater() {
		this.svnPathGenerator = new SubversionPathGenerator();
	}

	public JobMigrater(SubversionPathGenerator pSvnPathGenerator) {
		this.svnPathGenerator = pSvnPathGenerator;
	}
	
	public Document migrateJob(InputStream is){
		List<CvsRepository> cvsRepositories = new ArrayList<>();
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document;
		try {
			document = saxBuilder.build(is);
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		} catch (IOException ioEx) {
			throw new RuntimeException(ioEx);
		}

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
							List<CvsRepositoryItem> cvsModules =  extractCvsModules(repoChild, new ElementFilter("hudson.scm.CvsRepositoryItem"));
							repository.addItems(cvsModules);	
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
        			Element remoteElement = new Element("remote");
        			Element localElement = new Element("local");
        			Element depthOption = new Element("depthOption"); depthOption.setText("infinity");
        			Element ignoreExternalsOption = new Element("ignoreExternalsOption"); ignoreExternalsOption.setText("false");
        			String snvBasePath = MigrationProperties.getInstance().getTranslatedValue(element.getCvsRoot());
        			remoteElement.setText(svnPathGenerator.transformPath(snvBasePath, cvsRepositoryItem));
        			localElement.setText(cvsRepositoryItem.getLocalName());
        			scmModuleLocation.addContent(remoteElement);
        			scmModuleLocation.addContent(localElement);
        			scmModuleLocation.addContent(depthOption);
        			scmModuleLocation.addContent(ignoreExternalsOption);
        			locations.addContent(scmModuleLocation);
        		}
        	}
        }
			
		return document;
	}
	
	private List<CvsRepositoryItem> extractCvsModules(Element pRepoChildren,
			Filter<Element> filter) {
		List<CvsRepositoryItem> cvsRepoItems = new ArrayList<>();
		List<Element> items = pRepoChildren.getContent(filter);
		for (Element item : items) {
			for (Element modulesElem : item.getChildren()) {
				if ("modules".equals(modulesElem.getName())) {
					for (Element hudsonScmCvsModule : modulesElem
							.getChildren("hudson.scm.CvsModule")) {
						String locationtype = modulesElem.getParentElement()
								.getChildren("location").get(0)
								.getChildText("locationType");
						String locationName = modulesElem.getParentElement()
								.getChildren("location").get(0)
								.getChildText("locationName");
						if (locationName == null) {
							locationName = "";
						}
						cvsRepoItems.add(new CvsRepositoryItem(
								hudsonScmCvsModule.getChildText("localName"),
								hudsonScmCvsModule.getChildText("remoteName"),
								locationtype, locationName));
					}
				}
			}
			return cvsRepoItems;
		}
		return new ArrayList<CvsRepositoryItem>();
	}
	

}
