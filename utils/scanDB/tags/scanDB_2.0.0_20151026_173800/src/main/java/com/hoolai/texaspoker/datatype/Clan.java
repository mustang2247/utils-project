/**
 * 
 */
package com.hoolai.texaspoker.datatype;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 某战队。
 * 
 * @author Cedric(TaoShuang)
 * @create 2012-3-20
 */
public class Clan {
	/**
	 * 战队昵称
	 */
	public String clanName;
	/**
	 * 战队ID
	 */
	public Integer clanID;

	/**
	 * 队长ID
	 */
	public Integer leaderID;

	/**
	 * 副队长ID
	 */
	public TreeSet<Integer> captainIDs;

	/**
	 * TODO 战队等级，等级是靠什么提升的？
	 * 
	 * 只有该对象可被更换。
	 */
	public int level;
	/**
	 * TODO 战队积分，积分是所有人一起增加吗？
	 */
	public long score;
	
	/**
	 * 上周积分
	 */
	public long lastWeekScore;
	
	/**
	 * 本周积分
	 */
	public long thisWeekScore;
	
	/**
	 * 分子积分
	 */
	public long currentScore;
	
	/**
	 * 战队排名，一周一次排名？
	 */
	public int rank;
	
	/**
	 * 战队所拥有的道具。
	 * 
	 * id -> amount
	 */
	public ConcurrentHashMap<Integer, KindItemBean> items;

	/**
	 * 官方支持的金币数。
	 */
	public long officialSupport;
	
	/**
	 * 队长的每周福利
	 */
	public long leaderWeeklyWelfare;
	
	/**
	 * 队长领取的每周福利
	 */
	public long acceptedLeaderWeeklyWelfare;
	
	/**
	 * 是否已领取每周福利
	 */
	public boolean alreadyAcceptWeeklyWelfare;

	/**
	 * 战队成员。
	 */
	public ConcurrentHashMap<Integer, ClanMember> members;

	/**
	 * 战队宣言。
	 */
	public String declaration;

	/**
	 * 所有的申请者。
	 */
	public ConcurrentHashMap<Integer, Integer> alreadyApplier;
	
	/**
	 * 已经邀请过谁
	 */
	public ConcurrentHashMap<Integer, Integer> alreadyInvitees;
	
	/**
	 * false 说明没有锁定
	 * true 说明已锁定
	 */
	public boolean locker;
	
	public Clan() {
		this.clanName = LimitsAndConsts.UNKNOWN_STRING;
		this.clanID = LimitsAndConsts.UNKNOWN_ID;
		this.leaderID = LimitsAndConsts.UNKNOWN_ID;
		this.captainIDs = new TreeSet<Integer>();
		this.level = 1;
		this.score = 0L;
		this.lastWeekScore = 0;
		this.thisWeekScore = 0;
		this.currentScore = 0;
		this.rank = 0;
		this.items = new ConcurrentHashMap<Integer, KindItemBean>();
		this.officialSupport = 0L;
		this.leaderWeeklyWelfare = 0L;
		this.acceptedLeaderWeeklyWelfare = 0L;
		this.alreadyAcceptWeeklyWelfare = false;
		this.members = new ConcurrentHashMap<Integer, ClanMember>();
		this.declaration = LimitsAndConsts.UNKNOWN_STRING;
		this.alreadyApplier = new ConcurrentHashMap<Integer, Integer>();
		this.alreadyInvitees = new ConcurrentHashMap<Integer, Integer>();
		this.locker = false;
	}
}
