package tools.jenkins.entity;

public enum CvsRepositoryLocation {

	TAG{
		@Override
		public String toString(){
			return "tag";
		}
	}, 
	BRANCH{
		@Override
		public String toString(){
			return "branch";
		}
	}, HEAD{
		@Override
		public String toString(){
			return "head";
		}
	};
	
}
