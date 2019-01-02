package com.example.demo.util;


import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

public class MaoYanBoxOfficeApi {
	
	public static Response getMaoYanBoxOfficeApi() throws Exception {
		//猫眼票房API
		String url = "https://box.maoyan.com/promovie/api/box/second.json";
        Connection con = Jsoup.connect(url);
        //请求头设置             
        con.header("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"); 
        con.header("cookie", "_lxsdk_cuid=167c07d1c60c8-0745fa3808eb37-3c604504-e1000-167c07d1c61c8; _lxsdk=3291D8C002A111E9ABC733D765948D5E4BC8501DCBB44EBE9F0FCD522EC10600; __guid=211801990.2374386180731500500.1545270584963.1387; _lx_utm=utm_source%3DBaidu%26utm_medium%3Dorganic; __mta=55335700.1545722263468.1545724799982.1545725759233.5; monitor_count=2"); 
        con.ignoreContentType(true); 
        //解析请求结果
        Response resp = con.execute();
		return resp;
	}
	
	public static JSONArray parseMaoYanBoxOfficeResp(Response resp) {
		try {
			String respBody = resp.body();
			JSONObject jsonObject = new JSONObject(respBody);
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
			System.out.println(jsonArray);
			return jsonArray;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
