package tools.jenkins.entity;

import org.jdom2.Document;

public class JenkinsJob {
	
	private String url;
	private Document document;

	
	public JenkinsJob (String pUrl) {
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
	
	
}
