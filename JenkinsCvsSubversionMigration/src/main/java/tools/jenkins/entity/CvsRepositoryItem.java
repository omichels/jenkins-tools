package tools.jenkins.entity;

public class CvsRepositoryItem {
	
	String localName;
	String remoteName;
	String locationType;
	private CvsRepositoryLocation cvsLocationType;
	private String locationName;
	
	public CvsRepositoryItem(String localName, String remoteName,
			String locationType, String locationName) {
		super();
		this.localName = localName;
		this.remoteName = remoteName;
		this.locationType = locationType;
		this.locationName = locationName;
		if ("TAG".equals(locationType)) {
			cvsLocationType = CvsRepositoryLocation.TAG;
		} else if ("BRANCH".equals(locationType)) {
			cvsLocationType = CvsRepositoryLocation.BRANCH;
		} else {
			cvsLocationType = CvsRepositoryLocation.HEAD;
		}
	}

	@Override
	public String toString() {
		return "CvsRepositoryItem [localName=" + localName + ", remoteName="
				+ remoteName + ", locationType=" + locationType
				+ ", locationName=" + getLocationName() +"]";
	}

	public String getLocalName() {
		return localName;
	}
	
	public String getRemoteName() {
		return remoteName;
	}

	public String getLocationName() {
		return locationName;
	}

	public CvsRepositoryLocation getCvsLocationType() {
		return cvsLocationType;
	}

}