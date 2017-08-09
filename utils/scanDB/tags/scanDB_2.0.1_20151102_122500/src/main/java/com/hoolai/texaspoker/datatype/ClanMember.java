/**
 * 
 */
package com.hoolai.texaspoker.datatype;


/**
 * 战队成员，成员是由UserInfo对象构建的（仅限于第一次，以后都是从数据库里面load出来的）。
 * 
 * @author Cedric(TaoShuang)
 * @create 2012-3-20
 */
public class ClanMember {
	/**
	 * 成员ID
	 */
	public Integer memberID;
	/**
	 * 自己的战队ID
	 */
	public Integer clanID;

	/**
	 * 当前拥有的贡献 
	 */
	public long contribution;
	
	/**
	 * 身份
	 */
	public Byte position;
	
	/**
	 * 这周总贡献
	 * 
	 * 这个值是个净增长的统计值
	 */
	public long thisWeekContribution;
	
	/**
	 * 上周总净增长贡献
	 */
	public long lastWeekContribution;
	
	/**
	 * 本周的净捐献贡献值
	 */
	public long thisWeekDonation;
	
	/**
	 * 所在的俱乐部索引
	 */
	public int No = LimitsAndConsts.UNKNOWN_INT;
	
	private ClanMember() {
	}
}
