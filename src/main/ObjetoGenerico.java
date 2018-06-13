package main;

import tableModel.ObjectInfo;

public class ObjetoGenerico implements ObjectInfo {

	private Object[] info;
	private Object[] infoName;
	
	private int indInfo = 0;
	private int indInfoName = 0;
	
	public ObjetoGenerico(int length) {
		
		this.info = new Object[length];
		this.infoName = new Object[length];
	}
	
	
	public void addInfo(Object infoName, Object info) {
		
		addInfoName(infoName);
		addInfo(info);
	}
	
	
	private void addInfo(Object info) {
		
		this.info[indInfo] = info;
		
		indInfo++;
	}
	
	private void addInfoName(Object infoName) {
		
		this.infoName[indInfoName] = infoName;
		
		indInfoName++;
	}

	
	@Override
	public Object[] getInfo() {
		
		return this.info;
	}

	@Override
	public Object[] getInfoName() {
		
		return this.infoName;
	}
}