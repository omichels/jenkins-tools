package tools.jenkins;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import tools.jenkins.control.FixedPatternSubversionPathGenerator;
import tools.jenkins.control.JobMigrater;

public class TestJobMigrater {

	@Test
	public void testMigrateJob() throws IOException {
		JobMigrater migrater = new JobMigrater(new FixedPatternSubversionPathGenerator());
		
		 
		Document doc = new Document();
		Element rootElement = new Element("project");
		doc.addContent(rootElement);
		Element description = new Element("description");
		rootElement.addContent(description);
		description.setText("a description");
		Element scm = new Element ("scm");
		rootElement.addContent(scm);
		scm.setAttribute("class", "hudson.scm.CVSSCM");
		scm.setAttribute("plugin", "cvs@2.9");
		Element repositories = new Element("repositories");
		scm.addContent(repositories);
		Element hudsonScmCvsRepository = new Element("hudson.scm.CvsRepository");
		repositories.addContent(hudsonScmCvsRepository);
		Element cvsRoot = new Element("cvsRoot");
		hudsonScmCvsRepository.addContent(cvsRoot);
		cvsRoot.setText(":pserver:user@server.com:/srv/cvs_root");
		Element repositoryItems = new Element("repositoryItems");
		hudsonScmCvsRepository.addContent(repositoryItems);
		Element hudsonScmCvsRepositoryItem = new Element("hudson.scm.CvsRepositoryItem");
		repositoryItems.addContent(hudsonScmCvsRepositoryItem);
		Element modules = new Element("modules");
		hudsonScmCvsRepositoryItem.addContent(modules);
		Element hudsonScmCvsModule = new Element("hudson.scm.CvsModule");
		modules.addContent(hudsonScmCvsModule);
		Element localName = new Element("localName");
		hudsonScmCvsModule.addContent(localName);
		Element remoteName = new Element("remoteName");
		hudsonScmCvsModule.addContent(remoteName);
		remoteName.setText("cvsModuleName/componentName/subComponentName/full/path");
		Element location = new Element("location");
		location.setAttribute("class", "hudson.scm.CvsRepositoryLocation$BranchRepositoryLocation");
		hudsonScmCvsRepositoryItem.addContent(location);
		Element locationType = new Element("locationType");
		location.addContent(locationType);
		locationType.setText("BRANCH");
		Element locationName = new Element("locationName");
		location.addContent(locationName);
		locationName.setText("TRY_2015_01");
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		outputter.output(doc, baos);
		
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		
		Document migratedDocument = migrater.migrateJob(bais);
				 
		assertEquals("svn://server/projects/cvsModuleName/branches/TRY_2015_01/componentName/subComponentName/full/path", 
				migratedDocument.getDescendants(new ElementFilter("remote")).next().getText()  );

	}

}
