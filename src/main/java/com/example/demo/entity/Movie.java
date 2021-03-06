package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	@Column(length = 50)
	private String name;
	//海报
	private String poster;
//	//导演
//	private String director;
//	//编剧
//	private String writer;
//	//主演
//	private String mainActor;
//	类型
	//国家区域
	@Column(length = 20)
	private String area;
	//上映日期
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date releaseDate;
	//片长
	private String length;
	//票房
	//简介
	@Column(length = 1200)
	private String introduction;
	//状态
	private Long commentCount;
	
	private Float avgScore;
	@OneToMany(mappedBy = "movie" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,	
	},fetch = FetchType.LAZY ,orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<Photo> photos = new ArrayList<>();
	
	@OneToMany(mappedBy = "movie" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,			
	},fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<News> newsList = new ArrayList<>();

	@OneToMany(mappedBy = "movie" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,			
	},fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<Comment> comments = new ArrayList<>();
	
	@OneToMany(mappedBy = "movie" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,			
	},fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<Prize> prizes = new ArrayList<>();
	
	@OneToMany(mappedBy = "movie" ,cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REMOVE,			
	},fetch = FetchType.LAZY,orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	private List<MovieRole> movieRoles = new ArrayList<>();
	
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<Person> persons = new ArrayList<>();
   
	//类型
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JoinTable(joinColumns = { @JoinColumn(name = "movie_id")},
    			inverseJoinColumns = {@JoinColumn(name = "type_id")})
    private List<Type> types = new ArrayList<>();
    
	public void addPrize(Prize prize) {
		prize.setMovie(this);
		prizes.add(prize);
	}
	
	public void addMovieRole(MovieRole movieRole) {
		movieRole.setMovie(this);
		movieRoles.add(movieRole);
	}
    
	public void addComment(Comment comment) {
		comment.setMovie(this);
		comments.add(comment);
	}
    
    public void addPerson(Person person) {
    	persons.add(person);
    }
	
    public void addType(Type type) {
    	types.add(type);
    }
	
	public void addPhoto(Photo photo) {
		photo.setMovie(this);
		photos.add(photo);
	}
	
	public void addNews(News news) {
		news.setMovie(this);
		newsList.add(news);
	}
	
	

	public List<MovieRole> getMovieRoles() {
		return movieRoles;
	}

	@JsonBackReference
	public void setMovieRoles(List<MovieRole> movieRoles) {
		this.movieRoles = movieRoles;
	}

	public List<Person> getPersons() {
		return persons;
	}
	//@JsonBackReference
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<Prize> getPrizes() {
		return prizes;
	}

	@JsonBackReference
	public void setPrizes(List<Prize> prizes) {
		this.prizes = prizes;
	}

	public List<Comment> getComments() {
		return comments;
	}

	@JsonBackReference
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	


	public List<Type> getTypes() {
		return types;
	}
	//@JsonBackReference
	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public List<News> getNewsList() {
		return newsList;
	}
	@JsonBackReference
	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Float avgScore) {
		this.avgScore = avgScore;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}


	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	//@JsonBackReference
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	
	public Long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}

	public Movie(String name, String poster, String area,
			Date releaseDate, String length, String introduction, Float avgScore) {
		super();
		this.name = name;
		this.poster = poster;
		this.area = area;
		this.releaseDate = releaseDate;
		this.length = length;
		this.introduction = introduction;
		this.avgScore = avgScore;
	}

	public Movie() {
		super();
	}

	@Override
	public String toString() {
		return "Movie [Id=" + Id + ", name=" + name + ", poster=" + poster + ", area=" + area + ", releaseDate="
				+ releaseDate + ", length=" + length + ", introduction=" + introduction + ", commentCount="
				+ commentCount + ", avgScore=" + avgScore + "]";
	}	
	
	
	
}
