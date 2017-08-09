package com.hoolai.texaspoker.datatypes;


public class SafeBox {
	public String pass;
	public String phone;
	public long chip;
	public boolean removeingpass = false;// 是否处于强制解除密码中
	public long cancelPassTime;// 强制解除密码的时间
	public long maxChip;
	public boolean activated = false;// 是否可用
	public boolean opended = false;// 是否打开
	public boolean authed = false;// 是否通过验证
	public long lastTime;// 最后一次获取验证码的时间
	public long lastActiveTime = 0;// 最后使用时间

	public static int WRONG = -200;// 表示输入数字违规
	public static int LESS = -201;// 表示筹码不够
	public static int MORE = -203;// 表示筹码超出上限
	public static int ERROR = -204;// 修改用户筹码数失败

	
}
