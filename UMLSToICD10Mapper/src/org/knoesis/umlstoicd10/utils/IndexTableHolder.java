package org.knoesis.umlstoicd10.utils;

import java.util.HashMap;
import java.util.Map;

public class IndexTableHolder {
	private Map<String, String> icdCodesIndex;
	
	public IndexTableHolder() {	
		setIcdCodesIndex(new HashMap<String, String>());
	}
	
	public void loadIndex(){
		icdCodesIndex.put("diabetes mellitus", "E08");
		icdCodesIndex.put("chemical induced diabetes mellitus", "E09");
		icdCodesIndex.put("type 1 diabetes mellitus", "E10");
		icdCodesIndex.put("type 2 diabetes mellitus", "E11");
		icdCodesIndex.put("Atherosclerosis", "I70");
		
	}
	
	public Map<String, String> getIcdCodesIndex() {
		return icdCodesIndex;
	}

	public void setIcdCodesIndex(Map<String, String> icdCodesIndex) {
		this.icdCodesIndex = icdCodesIndex;
	}
}
