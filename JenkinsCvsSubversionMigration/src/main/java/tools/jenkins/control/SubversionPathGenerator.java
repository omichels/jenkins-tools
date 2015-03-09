package tools.jenkins.control;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

import tools.jenkins.entity.CvsRepositoryItem;
import tools.jenkins.entity.CvsRepositoryLocation;

public class SubversionPathGenerator {
 
	/**
	 * 
	 * @param pattern - e.g. svn://server/projects/${projectName}/${branchesTagsTrunkFolders}/
	 * @param item CvsRepositoryItem
	 * @return the transformed path for Subversion
	 */
	public String transformPath(String pattern, CvsRepositoryItem item) {
		String tagsBranchesTrunk = "";
		if (item.getCvsLocationType().equals(CvsRepositoryLocation.BRANCH)) {
			tagsBranchesTrunk = "branches" + "/" + item.getLocationName();
		}
		if (item.getCvsLocationType().equals(CvsRepositoryLocation.HEAD)) {
			tagsBranchesTrunk = "trunk";
		}
		if (item.getCvsLocationType().equals(CvsRepositoryLocation.TAG)) {
			tagsBranchesTrunk = "tags" + "/" + item.getLocationName();
		}
		Map<String, String> values = new HashMap<>();
		values.put("branchesTagsTrunkFolders", tagsBranchesTrunk);
		if (getNumberOfDelimiters(item.getRemoteName()) > 2) {
			String projectName = item.getRemoteName().split("/")[0];
			String componentName = item.getRemoteName().substring(item.getRemoteName().indexOf("/"));
			values.put("projectName", projectName);
			StrSubstitutor sub = new StrSubstitutor(values);
			return sub.replace(pattern) + componentName;
		}
		else {
			if (item.getLocalName() == null || item.getLocalName().length() == 0)  {
				values.put("projectName", item.getRemoteName() );
			} else {
				values.put("projectName", item.getLocalName());
			}
			StrSubstitutor sub = new StrSubstitutor(values);
			return sub.replace(pattern);	
		}
	}

	private int getNumberOfDelimiters(String remoteName) {
		return remoteName.split("/").length;
	}

}
