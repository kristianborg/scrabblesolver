package nl.krisborg.gwt.scrabblesolver.client;

public enum TyleType {
	NORMAL("normalTile"),
	ACTIVE("activeTile");
	
	private String type;
	
	private TyleType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
}
