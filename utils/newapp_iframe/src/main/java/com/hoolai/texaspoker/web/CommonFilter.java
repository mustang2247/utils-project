package com.hoolai.texaspoker.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonFilter implements Filter {
	
public static Map<String,String> appMap = new HashMap<>();
	
	static{
		appMap.put("100632434", "胡莱德州扑克_http://texaspoker.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("100732430", "麻将江湖_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104477408", "专业版四川麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104590323", "血拼四川麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104610684", "互乐四川麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104650326", "同城四川麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104737759", "胡莱棋牌_http://texaspoker.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104754063", "胡莱麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104787171", "翠花斗地主_http://landlords1.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104791638", "互乐二人麻将_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104791642", "互乐斗地主_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104830871", "斗地主合集_http://texaspoker.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104845917", "新街机捕鱼_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104852659", "经典斗牛_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104881083", "血拼斗牛_http://texaspoker.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104918140", "互乐斗牛_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1104947175", "精品十三张_http://texaspoker.gzopen.1251415748.clb.myqcloud.com/?");
		appMap.put("1105030733", "互乐德州扑克_http://114.255.24.37:8086/?");
		appMap.put("1105087918", "开心贪吃球_http://mahjonglobby.gzopen.1251415748.clb.myqcloud.com/?");
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String URI = request.getRequestURI();
		String appId = URI.replaceAll("\\/", "");
		appId = appId==null || "".equals(appId) ? "" : appId.trim();
		String TITLE = "";
		String URL = "";
		String appInfo= CommonFilter.appMap.get(appId);
		if(appInfo!=null){
			String[] temp = appInfo.split("\\_");
			TITLE = temp[0];
			if(temp.length>1){
				URL = temp[1];
			}
		}
		Enumeration<String> enumeration=request.getParameterNames();
		if(!"".equals(URL) && enumeration!=null && enumeration.hasMoreElements()){
			while (enumeration.hasMoreElements()) {
				String name=enumeration.nextElement();
				String value=request.getParameter(name);
				if(value!=null && !"".equals(value.trim())){
					URL += name + "=" + value.trim().replaceFirst(",", "")+ "&";
				}
			}
			URL += "appid="+appId;
		}
		request.setAttribute("URL", URL);
		request.setAttribute("TITLE", TITLE);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		return;
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
