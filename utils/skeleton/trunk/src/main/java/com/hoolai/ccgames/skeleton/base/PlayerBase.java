package com.hoolai.ccgames.skeleton.base;

public class PlayerBase {

	public long userID;

	public boolean isActive;
	
	public PlayerBase() {}

	public PlayerBase( long userID, boolean isActive ) {
		super();
		this.userID = userID;
		this.isActive = isActive;
	}
	
	public boolean equals( PlayerBase o ) {
		return userID == o.userID;
	}
}
