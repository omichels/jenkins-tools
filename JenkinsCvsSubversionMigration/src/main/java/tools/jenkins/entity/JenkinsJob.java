package tools.jenkins.entity;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class JenkinsJob {
	
	private String url;
	private Document document;

	
	public JenkinsJob (String pUrl) {
		this.url = pUrl;
	}


	public String getUrl() {
		return url;
	}


	public Document getDocument() {
		return document;
	}


	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public String toString() {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return "JenkinsJob [url=" + url + ", document=" + outputter.outputString(document);
	}
	
	
}
