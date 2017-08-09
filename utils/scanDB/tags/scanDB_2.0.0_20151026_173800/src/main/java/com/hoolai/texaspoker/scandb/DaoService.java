package com.hoolai.texaspoker.scandb;

public final class DaoService {
	
	private  static  DaoInterface  INSTANCE = null;
	
	public  static  void  setInstance( DaoInterface inst ) { DaoService.INSTANCE = inst; }
	
	public  static  DaoInterface  getInstance() { return INSTANCE; }
	
}
