package com.hoolai.ccgames.clientdemo.starter;

import com.hoolai.ccgames.skeleton.arch.GameThreadPool;
import com.hoolai.ccgames.skeleton.base.Finalizer;

public class DemoFinalizer implements Finalizer {

	@Override
	public void end() {
		GameThreadPool.destroy();
		
	}

}
