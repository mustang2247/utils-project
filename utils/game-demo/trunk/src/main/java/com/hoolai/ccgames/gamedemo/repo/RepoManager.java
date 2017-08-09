package com.hoolai.ccgames.gamedemo.repo;

import com.hoolai.ccgames.centergate.client.CenterRepo;
import com.hoolai.ccgames.centergate.client.CenterRepoImpl;

public class RepoManager {

	public static CenterRepo centerRepo;
	
	public static void init() {
		centerRepo = new CenterRepoImpl();
	}
}
