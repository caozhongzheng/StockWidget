package com.lenovo.leos.stocks;

public class items {
	 private String name;
	 private String city;
	    private String zuixin;
	    private String zhangdiefu;
	    private String zhangdiee;
	    private String chengjiaoliang;
			public items() {
				super();
				// TODO Auto-generated constructor stub
			}
			public items(String name, String city, String zuixin,
					String zhangdiefu, String zhangdiee, String chengjiaoliang) {
				super();
				this.name = name;
				this.zuixin = zuixin;
				this.zhangdiefu = zhangdiefu;
				this.city = city;
				this.zhangdiee = zhangdiee;
				this.chengjiaoliang = chengjiaoliang;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public String getZuixin() {
				return zuixin;
			}
			public void setZuixin(String zuixin) {
				this.zuixin = zuixin;
			}
			public String getZhangdiefu() {
				return zhangdiefu;
			}
			public void setZhangdiefu(String zhangdiefu) {
				
				this.zhangdiefu = zhangdiefu;
			}
			public String getCity() {
				return city;
			}
			public void setCity(String city) {
				this.city = city;
			}
			public String getZhangdiee() {
				return zhangdiee;
			}
			public void setZhangdiee(String zhangdiee) {
				this.zhangdiee = zhangdiee;
			}
			public String getChengjiaoliang() {
				return chengjiaoliang;
			}
			public void setChengjiaoliang(String chengjiaoliang) {
				this.chengjiaoliang = chengjiaoliang;
			}
}
