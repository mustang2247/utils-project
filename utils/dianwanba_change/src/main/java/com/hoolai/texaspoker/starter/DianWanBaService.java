package com.hoolai.texaspoker.starter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.hoolai.ccgames.center.protocol.Constants;
import com.hoolai.centersdk.item.ItemIdUtils;
import com.hoolai.centersdk.repo.DaoManager;
import com.hoolai.centersdk.sdk.ItemSdk;
import com.hoolai.centersdk.sdk.UserSdk;
import com.hoolai.centersdk.utils.CommonUtil;
import com.hoolai.centersdk.utils.JsonUtil;
import com.hoolai.centersdk.utils.TimeUtil;
import com.hoolai.texaspoker.bi.NewBICollector;
import com.hoolai.texaspoker.config.Config;
import com.hoolai.texaspoker.dao.DianWanBaDao;
import com.hoolai.texaspoker.vo.Item;
import com.hoolai.texaspoker.vo.Player;

@Service
public class DianWanBaService {
	
	@Autowired
	public DianWanBaDao dianWanBaDao;
	
	private static final Logger logger = LoggerFactory.getLogger(DianWanBaService.class);
	//电玩吧道具ID对应德州道具ID表
	private static final Map<Integer,Integer> itemMap = new HashMap<Integer, Integer>();
	
	static{
		//小金条(ID=21) → 青铜戒指
		//玉镯(ID=9)  → 钻石戒指
		//玛瑙杯(ID=10) → 魅惑紫晶
		//夜明珠(ID=11) → 红宝石王冠
		//1元话费卡(ID=22) → 1元话费卡
		//5元话费(ID=23) → 5元话费卡
		//10元话费卡(ID=16) → 10元话费卡
		//50元话费卡(ID=18) → 50元话费卡
		//兑换券(ID=12) → 藏宝图
		itemMap.put(21, ItemIdUtils.ITEM_ID_10501);
		itemMap.put(9, ItemIdUtils.ITEM_ID_10502);
		itemMap.put(10, ItemIdUtils.ITEM_ID_10503);
		itemMap.put(11, ItemIdUtils.ITEM_ID_10504);
		itemMap.put(22, ItemIdUtils.ITEM_ID_10100);
		itemMap.put(23, ItemIdUtils.ITEM_ID_10102);
		itemMap.put(16, ItemIdUtils.ITEM_ID_10103);
		itemMap.put(18, ItemIdUtils.ITEM_ID_10105);
		itemMap.put(12, ItemIdUtils.ITEM_ID_10301);
	}
	
	public void change(){
		//根据转换APPID获取
		List<Player> players = dianWanBaDao.getPlayers();
		if(players==null || players.isEmpty()){
			logger.error("players empty appId="+Config.appId);
			return;
		}
		final int playerSize = players.size();
		long startTime = TimeUtil.timeMS();
		logger.info("change start players appId="+Config.appId+" size="+playerSize+" startTime="+TimeUtil.convertToDateBYYYYYMMDDHHMMSS(startTime));
		String onePlayerInfo = players.get(0).toString();
		String lastPlayerInfo = players.get(playerSize-1).toString();
		logger.info("onePlayerInfo="+onePlayerInfo);
		logger.info("lastPlayerInfo="+lastPlayerInfo);
		String platform = "TENCENT";
		for (Player player : players) {
			try {
				//生成新的userId
				long userId = DaoManager.centerRepo.getUserId( platform, player.playerAppId, player.playerOpenId);
				if(userId>0){
					logger.error("change get user error userId>0 (userId="+userId+") player="+player.toString());
					continue;
				}
				userId = DaoManager.centerRepo.newUser( platform, player.playerAppId, player.playerOpenId);
				if( userId == Constants.INVALID_UID ) {
					logger.error("change new user error player="+player.toString());
					continue;
				}
				//按比例转换筹码
				long gold = ((long)player.haveMoney/10);
				if(gold>0){
					UserSdk.addGameGold(userId, gold);
					logger.info("user addGameGold userId="+userId+" player="+player.playerId+" gold="+gold);
				}
				//转换道具
				if(CommonUtil.isNoBlank(player.props)){
					TypeToken<List<Item>> type = new TypeToken<List<Item>>(){};
					List<Item> items = JsonUtil.fromJson(player.props, type.getType());
					if(items!=null && !items.isEmpty()){
						logger.info("user item userId="+userId+" player="+player.playerId+" old items="+JsonUtil.toJson(items));
						for (Item item : items) {
							Integer dzItemId = itemMap.get(item.id);
							if(dzItemId==null){
								continue;
							}
							ItemSdk.addItems(userId, "-1", dzItemId, item.nowNum);
							logger.info("user item userId="+userId+" player="+player.playerId+" itemId="+dzItemId+" count="+item.nowNum);
						}
					}
				}
				//体力初始化
				UserSdk.addHP(userId, 15);
				//报送BI
				NewBICollector.onInstall(player.playerAppId, userId, player.playerOpenId, platform);
				logger.info("new user userId="+userId+" player="+player.toString());
			} catch (Exception e) {
				logger.error("change player error e="+e+" player="+player,e);
			}
			logger.info("=====================================================");
		}
		long endTime = TimeUtil.timeMS();
		logger.info("change "+playerSize+" player end endTime="+TimeUtil.convertToDateBYYYYYMMDDHHMMSS(endTime)+" useTime="+((endTime-startTime)/1000)+"秒");
	
	}
	
	public static void main(String[] args) {
		String json = "[{\"bagDescription\":\"参加各个游戏的日赛，在每日登陆和活动中获得\",\"buyWay\":\"无\",\"couldEquip\":false,\"couldOverlay\":true,\"couldSale\":true,\"couldUse\":false,\"couleSend\":false,\"giveDiamond\":0,\"giveGold\":1,\"hasEquip\":false,\"id\":47,\"isBindingAfterSend\":true,\"maxOverlayNum\":9999,\"openLevel\":0,\"rebate\":0,\"salePrise\":1,\"shopDescription\":\"参加各个游戏的日赛，在每日登陆和活动中获得\",\"shopIconUrl\":\"http://scmj01.dl.hoolaigames.com/Icons/shopIcons/200010.png\",\"shopId\":\"200010\",\"shopName\":\"黄金入场券\",\"shopNoteUrl\":\"\",\"shopPrise\":1,\"shopQuality\":0,\"shopType\":-3,\"type\":\"普通道具\",\"nowNum\":9},{\"bagDescription\":\"可以在【商城】中的【实物商城】兑换实物奖励的神奇道具，可以在【娱乐场】的各个玩法中免费获取哦~\",\"buyWay\":\"score\",\"couldEquip\":false,\"couldOverlay\":true,\"couldSale\":true,\"couldUse\":true,\"couleSend\":true,\"giveDiamond\":0,\"giveGold\":0,\"hasEquip\":false,\"id\":12,\"isBindingAfterSend\":true,\"maxOverlayNum\":99999,\"openLevel\":0,\"rebate\":0,\"salePrise\":10,\"shopDescription\":\"实物商城兑换券\",\"shopIconUrl\":\"http://scmj01.dl.hoolaigames.com/Icons/shopIcons/200003.png\",\"shopId\":\"200003\",\"shopName\":\"兑换券\",\"shopNoteUrl\":\"\",\"shopPrise\":0,\"shopQuality\":0,\"shopType\":-3,\"type\":\"普通道具\",\"nowNum\":19}]";
		TypeToken<List<Item>> type = new TypeToken<List<Item>>(){};
		List<Item> items = JsonUtil.fromJson(json, type.getType());
		if(items!=null && !items.isEmpty()){
			for (Item item : items) {
				Integer dzItemId = itemMap.get(item.id);
				if(dzItemId==null){
					continue;
				}
				System.out.println("user addGameGold itemId="+dzItemId+" count="+item.nowNum);
			}
		}
	}
}
