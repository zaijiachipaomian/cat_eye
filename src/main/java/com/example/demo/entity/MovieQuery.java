package com.example.demo.entity;

public class MovieQuery {
	private String name;
	private Long typeId;
	private String area;
	private String releaseDate;
	//根据猫眼电影 https://maoyan.com/films?
	//showType=1 是正在热映（过去两个月内） ， showType=2是即将上映（未来的10天内） ， showType=3是经典影片（过去的两个月外）
	private String showType; 
	//sortId = 1 是按热门排序，就是不存在的排序 ， sortId=2 是按时间排序，sortId=3 是按评价排序
	private String sortId;
	
	@Override
	public String toString() {
		return "MovieQuery [name=" + name + ", typeId=" + typeId + ", area=" + area + ", releaseDate=" + releaseDate
				+ ", showType=" + showType + ", sortId=" + sortId + "]";
	}
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	
	
}
