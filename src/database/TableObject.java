package database;

import java.util.ArrayList;

import tableModel.ObjectInfo;

public class TableObject implements ObjectInfo {

	private ArrayList<Object> info = new ArrayList<>();
	private ArrayList<Object> infoName = new ArrayList<>();
	
	
	public void addInfo(Object infoName, Object info) {
		
		addInfoName(infoName);
		addInfo(info);
	}
	
	
	private void addInfo(Object info) {
		
		this.info.add(info);
	}
	
	private void addInfoName(Object infoName) {
		
		this.infoName.add(infoName);
	}

	
	@Override
	public Object[] getInfo() {
		
		return this.info.toArray(new Object[this.info.size()]);
	}

	@Override
	public Object[] getInfoName() {
		
		return this.infoName.toArray(new Object[this.infoName.size()]);
	}
}