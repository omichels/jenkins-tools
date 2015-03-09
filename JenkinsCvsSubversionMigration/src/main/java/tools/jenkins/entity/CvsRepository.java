package tools.jenkins.entity;

import java.util.ArrayList;
import java.util.List;

public class CvsRepository {
	
	String cvsRoot;
	private List<CvsRepositoryItem> items;
	
	public CvsRepository(String pCvsRoot) { 
		this.cvsRoot = pCvsRoot;
		items = new ArrayList<CvsRepositoryItem>();
	}
	
	public void addItem (CvsRepositoryItem pItem) {
		getItems().add(pItem);
	}

	@Override
	public String toString() {
		return "CvsRepository [cvsRoot=" + cvsRoot + ", items=" + getItems() + "]";
	}

	public void addItems(List<CvsRepositoryItem> l) {
		for (CvsRepositoryItem cvsRepositoryItem : l) {
			getItems().add(cvsRepositoryItem);
		}
	}

	public String getCvsRoot() {
		return cvsRoot;
	}

	public List<CvsRepositoryItem> getItems() {
		return items;
	}

 
}
