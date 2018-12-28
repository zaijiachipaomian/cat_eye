package com.example.demo.entity;

public class MovieQuery {
	private String name;
	private Long typeId;
	private String area;
	private String releaseDate;
	
	@Override
	public String toString() {
		return "MovieQuery [name=" + name + ", typeId=" + typeId + ", area=" + area + "]";
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
