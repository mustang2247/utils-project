/**
 * Author: guanxin@hoolai.com
 * Date: 2012-02-15 16:36
 */

package com.hoolai.texaspoker.datatype;

public final class LimitsAndConsts {
	
	// RETCODE
	public  static  final   int         TP_OK = 0;
	public  static  final   int         TP_FAIL = -1;
	public  static  final   int         TP_ERR_EXCEED = -2;
	public  static  final   int         TP_ERR_REPEAT = -3;
	public  static  final   int         TP_ERR_NOT_FOUND = -4;
	public  static  final   int         TP_ERR_FATAL = -5;
	public  static  final   int         TP_ERR_TIMEOUT = -6;
	public  static  final   int         TP_ERR_ILLEGAL = -7;
	public  static  final   int         TP_ERR_CANT_SITDOWN = -8;
	public  static  final   int			TP_ERR_NOT_MATCH = -9;
	
	public  static  final   int         TP_ERR_USER_NOT_FOUND = -10;
	public  static  final   int         TP_ERR_ITEM_NOT_FOUND = -11;
	public  static  final   int         TP_ERR_MATCH_NOT_FOUND = -12;
	public  static  final   int         TP_ERR_DESK_NOT_FOUND = -13;
	
	public  static  final   int         TP_ERR_DESK_SAME_IP = -14;             // 同一IP限制
	public  static  final   int         TP_ERR_DESK_NEWPLAY_LIMIT = -15;	   // 新手场限制
	public  static  final   int         TP_ERR_DESK_MIDDLEPLAY_LIMIT = -16;	   // 中级场限制
	public	static	final	int			TP_ERR_DESK_SUPERPLAY_LIMIT = -17;	   // 超级桌限制
	
	public	static	final	int			TP_ERR_MP_NOT_ENOUGH = -47;			   // 大师分不够
	public	static	final	int			TP_ERR_MS_NOT_ENOUGH = -48;			   // 战绩不够
	public  static  final   int         TP_ERR_GOLD_EXCEED = -49;              // 金币超限
	public  static  final   int         TP_ERR_GOLD_NOT_ENOUGH = -50;          // 金币不够
	public  static  final   int         TP_ERR_HOOLAI_NOT_ENOUGH = -51;        // 胡莱币不够
	public  static  final   int         TP_ERR_ITEM_NOT_ENOUGH = -52;          // 道具不够
	public  static  final   int         TP_ERR_LEVEL_NOT_ENOUGH = -53;         // 等级不够
	public  static  final   int         TP_ERR_ACHV_NOT_ENOUGH = -54;          // 成就不够
	public  static  final   int         TP_ERR_VIP_NOT_ENOUGH = -55;           // vip不够
	public  static  final   int         TP_ERR_LEVEL_EXCEED = -56;             // 等级超出
	public  static  final   int         TP_ERR_TOO_FREQUENT = -57;             // 太频繁操作
	public  static  final   int         TP_ERR_QQ_VIP_NOT_ENOUGH = -58;        // QQ vip不够
	public  static  final   int         TP_ERR_CON_NOT_ENOUGH = -59;           // 贡献值不够
	public  static  final   int         TP_ERR_BUFF_EXPIRED = -60;			   // 有益效果过期了
	public  static  final   int         TP_ERR_BUFF_TIMES_NOT_ENOUGH = -61;	   // 有益效果次数不够了
	public	static	final	int			TP_ERR_NEEDED_ITEM_NOT_ENOUGH = -62;   // 所需的道具不够
	public	static	final	int			TP_ERR_MATE_LEVEL_NOT_ENOUGH = -63;	   // 队友的等级不够
	public	static	final	int			TP_ERR_STATE_ERROR = -64;		   	   // 状态错误
	public	static	final	int			TP_ERR_STATE_IN_GAME = -65;			   // 在现金桌，状态错误
	public	static	final	int			TP_ERR_STATE_IN_MATCH = -66;		   // 在比赛，状态错误
	public	static	final	int			TP_ERR_STATE_IN_KILLER = -67;		   // 在扑克杀，状态错误
	public	static	final	int			TP_ERR_STATE_IN_ROBOT = -68;		   // 在试试手气，状态错误
	public	static	final	int			TP_ERR_BUFF_NOT_FOUND = -69;		   // BUFF不存在
	
	public  static  final   int         TP_ERR_SERVER_RESTART = -99;           // 服务器准备重启，无法报名比赛
	public  static  final   int         TP_ERR_PLAYER_FULL = -100;             // 牌桌玩家已满，无法加入（牌桌错误码以-100开始）
	public  static  final   int         TP_ERR_DESK_PLAYING = -101;            // 还在游戏中
	public  static  final   int         TP_ERR_MATCH_PLAYING = -102;           // 比赛已经开始
	public  static  final   int         TP_ERR_SNG_ONE = -103;                 // SNG比赛只能报名1场
	public  static  final   int         TP_ERR_MTT_CONFLICT = -104;            // MTT比赛间隔不能小于冲突提醒时间
	
	public	static	final	int			TP_ERR_IN_COOLDOWN = -200;			   // 仍然在CD时间
	public	static	final	int			TP_ERR_CARD_NOT_ENOUGH = -201;		   // 卡牌数量不够
	public	static	final	int			TP_ERR_FREE_TIMES_NOT_ENOUGH = -202;   // 免费次数没了
	
	public  static  final   int         TP_ERR_TEAM_FULL = -500;               // 战队已满，无法加入（战队错误码以-500开始）
	public  static  final   int         TP_ERR_TEAMLEADER_QUIT = -501;         // 队长未交接身份就退出战队
	public  static  final   int         TP_ERR_IN_SAME_TEAM = -502;            // 用户已经在本战队
	public  static  final   int         TP_ERR_IN_OTHER_TEAM = -503;           // 用户已经加入其它战队
	public  static  final   int         TP_ERR_SUBLEADER_FULL = -504;          // 领队人数已满，无法添加
	public  static  final   int         TP_ERR_NOT_IN_TEAM = -505;             // 用户不在任何战队中
	public  static  final   int         TP_ERR_TEAM_WAREHOUSE_HAVENT = -506;   // 战队仓库没有这个道具
	public  static  final   int         TP_ERR_TEAM_WAREHOUSE_LACK = -507;	   // 战队仓库没有足够数量的道具
	
	/**
	 * 已经领取过了
	 */
	public	static	final	int			TP_ERR_HAS_ALREADY_ACCEPTED = -600;
	/**
	 * 没有满足需求
	 */
	public	static	final	int			TP_ERR_NOT_MEET_NEEDED = -601;
	
	public 	static	final	int			TP_ERR_CDKEY_ALREADY_USED = -610;		// 本类CDKEY已经使用过一次了
	public	static	final	int			TP_ERR_CDKEY_EXCEED_USED_TIMES = -611;	// 已经超过了CDKEY的使用次数了
	
	public	static	final	int			TP_ERR_EGG_STATE_ERROR = -700;
	/**
	 * 已经体验过了
	 */
	public	static	final	int			TP_ERR_HAS_ALREADY_TRIED = -800;
	
	/**
	 * 不可替换，体验卡替换失败
	 */
	public	static	final	int			TP_ERR_VIP_CARD_CHANGE = -801;
	
	// Limits
	public  static  final   int         MAX_MATCH_RESULT_COUNT = 3;            // 我的比赛最多显示的战绩条数
	public  static  final   int         MAX_RIVAL_COUNT = 100;                 // 最大对手数量
	public  static  final   int         MAX_MESSAGE_COUNT = 100;               // 最大消息数量
	public  static  final   int         MAX_ONLOOKER_COUNT = 100;              // 一个房间最多的旁观人数
	public  static  final   int         MAX_ACTIONINDICATOR_COUNT = 20;        // 最大动作指示器数量
	public  static  final   int         MAX_WAIT_DESK_TIME = 12000;            // 最大等待用户操作时间，单位毫秒
	public  static  final   int         MAX_MANAGED_TIMES = 10000;             // 每局如果有托管到局末的行为，则记一次MAX_MANAGED_TIMES，大于这个数表示托管2局，踢出游戏
	
	// 用户状态:[大厅、旁观、游戏中、比赛中、不在线]
	public  static  final   byte        USER_STATUS_OFFLINE = 0x0;             // 不在线
	public  static  final   byte        USER_STATUS_LOBBY = 0x1;               // 在大厅
	public  static  final   byte        USER_STATUS_PLAYING = 0x2;             // 游戏中
	public  static  final   byte        USER_STATUS_ONLOOKING = 0x4;           // 旁观中
	public  static  final   byte        USER_STATUS_COMPETING = 0x8;           // 比赛中
	public  static  final   byte        USER_STATUS_ROBOT = 0x10;              // 和机器人对战中
	public	static	final	byte		USER_STATUS_KILLER = 0x20;			   // 扑克杀中
	
	public  static  final   byte        USER_STATUS_LOBBY_BACKPACK_VIEWING = 5;// 看背包中
	
	// VIP
	public  static  final   byte        VIP_NONE = 0;					       // 不是VIP
	public  static  final   byte        VIP_BRONZE = 1;					       // VIP铜卡
	public  static  final   byte        VIP_SILVER = 2;				           // VIP银卡
	public  static  final   byte        VIP_GOLD = 3;				           // VIP金卡
	
	public  static  final   double      USER_NORMAL_VIP_EXP_INCREMENT_RATIO = 1.20; // 普通VIP经验值增速
	public  static  final   double      USER_ADVANCED_VIP_EXP_INCREMENT_RATIO = 1.35; // 高级VIP经验值增速
	
	public  static  final   byte        FUNC_CALCULATOR = 1;				   // 牌力计算器
	public  static  final   byte        FUNC_DAY_GIFT = 2;	    			   // vip每日礼包
	public  static  final   byte        FUNC_EXP_ACC20 = 11;			       // 经验值增加20%
	public  static  final   byte        FUNC_EXP_ACC35 = 12;    		       // 经验值增加35%
	
	// 比赛房间状态
	/**
	 * 无意义
	 */
	public  static  final   byte        MATCH_STATUS_NULL = -1;                // 无意义
	/**
	 * 可报名
	 */
	public  static  final   byte        MATCH_STATUS_PARTICIPATE = 0;          // 可报名
	/**
	 * 准备进行
	 */
	public  static  final   byte        MATCH_STATUS_READY = 1;                // 准备进行
	/**
	 * 进行中
	 */
	public  static  final   byte        MATCH_STATUS_PROCESSING = 2;           // 进行中
	/**
	 * 已结束
	 */
	public  static  final   byte        MATCH_STATUS_FINISH = 3;               // 已结束
	/**
	 * 可加入
	 */
	public  static  final   byte        MATCH_STATUS_CANJOIN = 4;              // 可加入
	/**
	 * 可退赛
	 */
	public  static  final   byte        MATCH_STATUS_CANQUIT = 5;              // 可退赛
	
	// 比赛模式
	public  static  final   byte        MATCH_MODE_SNG = 1;                    // 坐满开赛
	public  static  final   byte        MATCH_MODE_MTT = 2;                    // 按时开赛
	public	static	final	byte		MATCH_MODE_KILLER = 3;				   // 匹配三国杀
	
	// 牌桌类型
	public  static  final   short       DESK_TYPE_NINE = 0x09;                 // 九人桌
	public  static  final   short       DESK_TYPE_SIX = 0x06;                  // 六人桌
	public  static  final   short       DESK_TYPE_GAME = 0x100;                // 普通现金桌
	public  static  final   short       DESK_TYPE_MATCH = 0x200;               // 普通比赛桌
	public  static  final   short       DESK_TYPE_FINAL = 0x400;               // 比赛最终桌
	
	// 不同ID的offset
	public  static  final   int         MATCH_ID_OFFSET = 18;                  // 比赛id的移位
	public  static  final   int         ROOM_ID_OFFSET = 13;                   // 房间id的移位
	
	// 现金桌游戏进程
	public  static  final   byte        GAME_STAGE_NONE = 0;                   // 游戏尚未开始
	public  static  final   byte        GAME_STAGE_DEALHAND = 1;               // 发手牌
	public  static  final   byte        GAME_STAGE_BETROUND1 = 2;              // 第1轮下注
	public  static  final   byte        GAME_STAGE_FLOP_BEGIN = 3;             // 发前3张公共牌开始
	public  static  final   byte        GAME_STAGE_FLOP_END = 4;               // 发前3张公共牌结束
	public  static  final   byte        GAME_STAGE_BETROUND2 = 5;              // 第2轮下注
	public  static  final   byte        GAME_STAGE_TURN_BEGIN = 6;             // 发第4张开始
	public  static  final   byte        GAME_STAGE_TURN_END   = 7;             // 发第4张结束
	public  static  final   byte        GAME_STAGE_BETROUND3 = 8;              // 第3轮下注
	public  static  final   byte        GAME_STAGE_RIVER_BEGIN = 9;            // 发第5张开始
	public  static  final   byte        GAME_STAGE_RIVER_END = 10;             // 发第5张结束
	public  static  final   byte        GAME_STAGE_BETROUND4 = 11;             // 第4轮下注
	public  static  final   byte        GAME_STAGE_COMPARE = 12;               // 比较结果
	
	// 玩家状态: []
	public  static  final   byte        PLAYER_STATE_LEAVE = 0;                // 玩家离开
	public  static  final   byte        PLAYER_STATE_EXCHANGE = 1;             // 兑换筹码
	public  static  final   byte        PLAYER_STATE_READY = 2;                // 等待下一轮开始
	public  static  final   byte        PLAYER_STATE_DEALING = 3;              // 等待发牌
	public  static  final   byte        PLAYER_STATE_PLAYING = 4;              // 正在动作
	public  static  final   byte        PLAYER_STATE_WAITING = 5;              // 等待
	public  static  final   byte        PLAYER_STATE_MANAGED = 6;              // 托管
	public  static  final   byte        PLAYER_STATE_NRFOLD = 7;               // 下把弃牌（发生在用户强行离开房间的时候）
	public  static  final   byte        PLAYER_STATE_CALCULATE = 8;            // 最后计算结果
	public  static  final   byte        PLAYER_STATE_ONLOOK = 9;               // 旁观
	
	// 玩家动作
	public  static  final   short       PLAYER_ACTION_NONE = 0x0;              // 无动作
	public  static  final   short       PLAYER_ACTION_FOLD = 0x1;              // 弃牌
	public  static  final   short       PLAYER_ACTION_CALL = 0x2;              // 跟注
	public  static  final   short       PLAYER_ACTION_RAISE = 0x4;             // 加注
	public  static  final   short       PLAYER_ACTION_CHECK = 0x8;             // 看牌
	public  static  final   short       PLAYER_ACTION_BET = 0x10;              // 下注、押注
	public  static  final   short       PLAYER_ACTION_ALLIN = 0x20;            // 全下
	public  static  final   short       PLAYER_ACTION_FACEUP = 0x40;           // 亮牌
	public  static  final   short       PLAYER_ACTION_NOFACEUP = 0x80;         // 不亮牌
	public  static  final   short       PLAYER_PRESETACTION_CHECK = 0x100;     // 预设看牌
	public  static  final   short       PLAYER_PRESETACTION_CHECKFOLD = 0x200; // 预设看牌/弃牌
	public  static  final   short       PLAYER_PRESETACTION_CALLANY = 0x400;   // 预设跟任何注
	public  static  final   short       PLAYER_PRESETACTION_CALL = 0x800;      // 预设跟注（xxx）
	public  static  final   short       PLAYER_PRESETACTION_ALLIN = 0x1000;    // 预设全下
	
	
	// 玩家角色
	public  static  final   byte        PLAYER_ROLE_BUTTON = 1;                // 庄家
	public  static  final   byte        PLAYER_ROLE_SBLIND = 2;                // 小盲注
	public  static  final   byte        PLAYER_ROLE_BBLIND = 3;                // 大盲注
	public  static  final   byte        PLAYER_ROLE_SIMPLE = 4;                // 普通玩家
	
	// 自动购买策略 auto_stragety
	public  static  final   byte        AUTO_STRATEGY_NONE = 0;                // 无自动策略
	public  static  final   byte        AUTO_STRATEGY_EMPTY_XBB = 1;           // 输光自动补充XBB
	public  static  final   byte        AUTO_STRATEGY_ALWAYS_FULL = 2;         // 低于上限立即补充到上限
	
	// 动作指示器ActionIndicator
	public  static  final   byte        ACTION_INDICATOR_DEAL_HANDCARDS = 1;   // 发手牌
	public  static  final   byte        ACTION_INDICATOR_FLOP = 2;             // 发前三张
	public  static  final   byte        ACTION_INDICATOR_TURN = 3;             // 发第四张
	public  static  final   byte        ACTION_INDICATOR_RIVER = 4;            // 发第五张
	public  static  final   byte        ACTION_INDICATOR_FOLD = 5;             // 中途弃牌
	public  static  final   byte        ACTION_INDICATOR_FACEUP = 6;           // 翻开牌面
	public  static  final   byte        ACTION_INDICATOR_FACEUP_SPECIAL = 7;   // 大家都弃牌，最后赢家选择翻拍时的动作
	public  static  final   byte        ACTION_INDICATOR_COMPARE = 8;          // 比牌（将指定的5张牌提出来，再放回去）
	public  static  final   byte        ACTION_INDICATOR_CHIPS_JUMP = 16;      // 筹码跳堆
	public  static  final   byte        ACTION_INDICATOR_CHIPS_JOIN = 17;      // 筹码合堆
	
	
	// 在桌面的位置
	public  static  final   short       POSITION_SEAT_START = 0;               // 座位开始号
	public  static  final   short       POSITION_EDGE_START = 50;              // 桌沿开始号
	public  static  final   byte        POSITION_POT = 99;                     // 底池位置
	public  static  final   byte        POSITION_SIDEPOT_START = 100;          // 边池位置开始号
	
	// 获奖资格
	public  static  final   int         QUALIFY_POT[] = 
		{ 0x1, 0x2, 0x4, 0x8, 0x10, 0x20, 0x40, 0x80 };                        // 可获得边池资格
	public  static  final   int         QUALIFY_JACKPOT = 0x1000;              // 可获得彩池
	
	
	// 在战队中身份
	public  static  final   byte        TEAM_NONE = 0;                         // 非战队成员（踢出战队）
	public  static  final   byte        TEAM_LEADER = 1;                       // 队长
	public  static  final   byte        TEAM_SUBLEADER = 2;                    // 领队（副队长）
	public  static  final   byte        TEAM_MEMBER = 3;                       // 成员
	
	// 房间难度等级level
	public  static  final   byte        DIFFICULTY_ALL = 0;                    // 全部
	public  static  final   byte        DIFFICULTY_PRIMER = 1;                 // 入门
	public  static  final   byte        DIFFICULTY_BEGINNER = 2;               // 新手
	public  static  final   byte        DIFFICULTY_MEDIUM = 3;                 // 中级
	public  static  final   byte        DIFFICULTY_PROFESSIONAL = 4;           // 专家
	public  static  final   byte        DIFFICULTY_VIP = 5;                    // VIP场
	public  static  final   byte        DIFFICULTY_COUNT = 6;                  // 注意这个数字一定是所有种类的个数
	
	// 比赛分类
	public  static  final   byte        MATCH_CLASS_ALL = 0x0;                 // 全部
	public  static  final   byte        MATCH_CLASS_FREE = 0x1;                // 免费赛
	public  static  final   byte        MATCH_CLASS_GOLD = 0x2;                // 金币赛
	public  static  final   byte        MATCH_CLASS_ITEM = 0x4;                // 实物赛
	public  static  final   byte        MATCH_CLASS_VIP = 0x8;                 // VIP赛
	
	
	public  static  final   int         UNKNOWN_ID = 0;                        // 不存在的ID
	public  static  final   byte        UNKNOWN_BYTE = -1;                     // 未确定的值
	public  static  final   short       UNKNOWN_SHORT = -1;                    // 未确定的值
	public  static  final   int         UNKNOWN_INT = -1;                      // 未确定的值
	public  static  final   long        UNKNOWN_LONG = -1L;                     // 未确定的值
	public  static  final   String      UNKNOWN_STRING = "";                   // 未确定的值
	
	public  static  final   byte        MESSAGE_ATTR_ALREADY_READ = 0x10;      // 已读消息状态
	public  static  final   byte        MESSAGE_ATTR_ALREADY_HANDLE = 0x20;    // 已处理消息状态
	public  static  final   byte        MESSAGE_ATTR_SYS = 0x00;               // 系统消息类型
	public  static  final   byte        MESSAGE_ATTR_SNS = 0x01;               // 社交消息类型
	public  static  final   byte        MESSAGE_ATTR_CONSUME = 0x02;           // 消费消息类型
	public  static  final   byte        MESSAGE_TYPE_COUNT = 0x03;             // 消息类型数量
	
	public  static  final   byte        ITEM_ATTR_NONE = 0x00;                 // 物品无属性
	public  static  final   byte        ITEM_ATTR_USE = 0x01;                  // 物品拥有使用属性
	public  static  final   byte        ITEM_ATTR_SELL = 0x02;                 // 物品拥有出售属性
	public  static  final   byte        ITEM_ATTR_GIVE = 0x04;                 // 物品拥有赠与属性
	public  static  final   byte        ITEM_ATTR_BUY = 0x08;                  // 物品拥有购买属性
	
	public  static  final   byte        ITEM_GOLD_BUY = 1;                     // 游戏币购买
	public  static  final   byte        ITEM_HOOLAI_BUY = 2;                   // 胡来币购买
	public  static  final   byte        ITEM_ITEM_BUY = 3;                     // 实物购买
	public	static 	final	byte		ITEM_CON_BUY = 4;					   // 战队贡献购买
	public	static	final	byte		ITEM_MS_BUY = 5;					   // 战绩购买
	public 	static	final	byte		ITEM_MP_BUY = 8;				       // 大师积分购买
	public	static	final	byte		ITEM_SELL_HOOLAI_BUY = 6;			   // 销售胡莱币购买
	public 	static	final	byte		ITEM_SELL_GOLD_BUY = 7;				   // 销售游戏币购买
	
	public  static  final   byte        GOLD_CHANGE_SIMPLE = 0;                // 金币普通变化
	public  static  final   byte        GOLD_CHANGE_BONUS = 1;                 // 金币奖励变化
	public	static	final	byte		GOLD_CHANGE_SVIP = 2;				   // 超级VIP卡金币奖励
	
	public 	static	final 	byte		HOOLAI_CHANGE_SIMPLE = 0;			   // 胡莱币普通变化
	public 	static	final 	byte		HOOLAI_CHANGE_BUY = 1;			   	   // 胡莱币购买变化
	public 	static	final 	byte		HOOLAI_CHANGE_BONUS = 2;			   // 胡莱币奖励变化
	
	public	static	final	byte		MP_CHANGE_SIMPLE = 0;				   // 大师分变化普通
	
	public	static	final	byte		MS_CHANGE_SIMPLE = 0;				   // 战绩变化普通
	
	public  static  final   byte        BUY_IN_GOLD_SHOP = 1;                  // 在金币商店
	public  static  final   byte        BUY_IN_HOOLAI_SHOP = 2;                // 在胡来币商店
	public  static  final   byte        BUY_IN_ITEM_SHOP = 3;                  // 在实物商城
	
	public  static  final   byte        JACKPOT_NONE = 0;                      // 未激活
	public  static  final   byte        JACKPOT_READY = 1;                     // 下把激活
	public  static  final   byte        JACKPOT_ACTIVE = 2;                    // 已经激活
	
	public  static  final   int         TOAST_TYPE_REMIND_VIP = 0;             // vip到期提示
	public  static  final   int         TOAST_TYPE_GJACKPOT = 1;               // 获得游戏场彩池
	public  static  final   int         TOAST_TYPE_LEISURE10 = 2;              // 海上休闲连翻10把
	public  static  final   int         TOAST_TYPE_MATCH_ITEM = 3;             // 比赛场获得NB道具
	public  static  final   int         TOAST_TYPE_BOX_ITEM = 4;               // 开宝箱获得NB道具
	public	static	final	int			TOAST_TYPE_TODAY_HOT_ITEM = 5;		   // 今日热销道具
	public	static	final	int			TOAST_TYPE_TODAY_HOT_ACTIVITY = 6;	   // 今日活动
	public	static	final	int			TOAST_TYPE_CLAN_HORN = 7;			   // 战队号角
	public	static	final	int			TOAST_TYPE_LUCKY_WHEEL = 8;			   // 幸运转盘
	public	static	final	int			TOAST_TYPE_INVITE_KILLER = 9;		   // 扑克杀邀请Toast
	public	static	final	int			TOAST_TYPE_DOUBLE_HLHC = 10;		   // 翻倍胡莱横财成功toast
	public	static	final	int			TOAST_TYPE_HLHC = 11;				   // 获得胡莱横财toast
	public	static	final	int			TOAST_TYPE_GOLD_EGG = 12;			   // 砸蛋Toast
	public	static	final	int			TOAST_TYPE_CHRISTMAS_GIFT = 13;		   // 圣诞炫耀Toast
	public	static	final	int			TOAST_TYPE_CARD_SHOW = 14;			   // 抽到高级卡牌Toast
	public  static  final   int         TOAST_TYPE_INVITE_NOTICE = 15;         // 主播邀请通告
	public	static	final	int			TOAST_TYPE_X_EGG = 16;				   // 极品蛋出现
	public	static	final	int			TOAST_TYPE_EASTER_X_ITEMS = 17;		   // 蛋里好东西
	public	static	final	int			TOAST_TYPE_REMIND_SUPER_VIP = 18;	   // 超级VIP即将过期提醒
	public	static	final	int			TOAST_TYPE_SUPER_VIP_EXPIRED = 19;	   // 超级VIP过期提醒
	public	static	final	int			TOAST_TYPE_X_GIFT = 20;				   // 超过50块钱则Toast
	public	static	final	int			TOAST_TYPE_SUPER_LIVE_SHOW = 21;	   // 超级桌直播
	public	static	final	int			TOAST_TYPE_SUPER_CHAMPION = 22;		   // 超级桌冠军
	  
	public  static  final   int         TOAST_TYPE_MDESK_ONLOOK = 23;          // 比赛桌最终桌邀请旁观Toast；
	
	public  static  final   int         INVITE_DAREN_SIZE = 7;				   // 邀请到人最多7人
	
	public  static  final   long        DAY_MILLIS = 24*60*60*1000;            // 每天毫秒数
	public  static  final   long        HOUR_MILLIS = 60*60*1000;              // 每小时毫秒数
	public  static  final   long        MINUTE_MILLIS = 60*1000;               // 每分钟毫秒数
	public  static  final   long        SECOND_MILLIS = 1000;                  // 每秒毫秒数
	
	public  static  final   long        TODAY_JOIN_GAME = 0x0000000000000001L; // 每天第一次加入游戏场
	public  static  final   long        TODAY_JOIN_MATCH = 0x0000000000000002L;// 每天第一次加入比赛场
	public  static  final   long        TODAY_JOIN_ROBOT = 0x0000000000000004L;// 每天第一次加入试试手气场
	public	static	final	long		TODAY_JOIN_KILLER = 0x0000000000000008L; // 每天第一次加入扑克杀
	public	static	final	long		TODAY_WIN_KILLER = 0x00000000000000010L; // 每日首胜-扑克杀
	public	static	final	long		TODAY_LUCKY_CARD = 0x00000000000000020L; // 每日抽卡
	public	static	final	long		TODAY_GOLD_BRICK_MOBILE = 0x0000000040L; // 手机每日领取金砖
	
	public  static  final   int         SHARE_JOIN_GAME = 1;				   // 进入游戏场分享
	public  static  final   int         SHARE_JOIN_MATCH = 2;				   // 进入比赛场分享
	public  static  final   int         SHARE_JOIN_ROBOT = 3;				   // 进入试试手气场分享
	public	static	final	int			SHARE_JOIN_KILLER = 4;				   // 进入扑克杀分享
	
	public	static	final	int			SERVERID = 0;						   // 手游服务器的ServerID == 0
	public	static	final	int			ROBOT_TEAM_ID = -888;				   // 机器人的战队ID
	
	public  static 	final 	int 		CLAN_MAX_LEVEL = 20;				   // 战队最高等级
}