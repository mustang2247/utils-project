package com.hoolai.ccgames.skeleton.base;

public class SynEnvBase {

	private final long synId;
	private boolean isProcessing;

	public SynEnvBase( long id ) {
		this.synId = id;
		this.isProcessing = false;
	}

	public void setProcessing( boolean p ) {
		this.isProcessing = p;
	}

	public boolean isProcessing() {
		return this.isProcessing;
	}

	public long getSynId() {
		return this.synId;
	}
}
