/**
 * Author: guanxin@hoolai.com
 * Data: 2012-02-16 10:30
 */

package com.hoolai.texaspoker.datatype;

/**
 * 如果购买了多个VIP，那么优先使用最高级的，较低级的暂时存储，等高级的使用完了，再使用
 * 低级的。
 */
public final class VIPInfo {
	public  volatile  byte    vip_now;         // 当前VIP类型
	public  volatile  long    expire_time;     // 当前VIP过期时间
	public  class  VIPPackage {
		public  volatile  byte    vip_type;          // VIP类型
		public  volatile  long    milliseconds;      // VIP可使用时间长度
		public  VIPPackage( byte v, long ms ) { vip_type = v; milliseconds = ms; }
	}
	public  VIPPackage[]  storing;         // 储存的VIP（购买了多个VIP，只能使用一个）
	
	public  class  Function {
		public  volatile  byte    func_type;       // 功能类型
		public  volatile  long    expire_time;     // 功能过期时间
		public  Function( byte f, long e ) { func_type = f; expire_time = e; }
	}
	public  Function[]  functions;           // 目前用户身上的附加功能
	
	public  volatile  byte    vip_remind;     // 登录提醒vip等级，只有从vip掉落到0时才提醒
}