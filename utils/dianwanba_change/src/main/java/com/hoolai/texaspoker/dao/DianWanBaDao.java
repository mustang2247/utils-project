package com.hoolai.texaspoker.dao;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hoolai.centersdk.utils.TimeUtil;
import com.hoolai.texaspoker.config.Config;
import com.hoolai.texaspoker.vo.Player;

@Repository
public class DianWanBaDao {
	
	private static final Logger logger = LoggerFactory.getLogger(DianWanBaDao.class);
	
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
	public List<Player> getPlayers() {
		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -1);
			String loginTime = TimeUtil.formatDateForYYYYMMDD(c.getTime().getTime())+" 00:00:00";
			String[] params = new String[]{Config.appId,loginTime};
			String sql = "SELECT * FROM player WHERE playerAppId = ? AND loginTime>? ORDER BY loginTime ASC";
			List<Player> players = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<Player>(Player.class));
			return players;
		} catch (Exception e) {
			logger.info("DianWanBaDao::getPlayers e="+e.getMessage(),e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String appId = "1104477408";
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		String loginTime = TimeUtil.formatDateForYYYYMMDD(c.getTime().getTime())+" 00:00:00";
		String sql = "SELECT count(*) FROM player WHERE playerAppId = '"+appId+"' AND loginTime>'"+loginTime+"' ORDER BY loginTime ASC";
		System.out.println(sql);
	}
}
