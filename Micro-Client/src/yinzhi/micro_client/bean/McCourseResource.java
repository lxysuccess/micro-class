package yinzhi.micro_client.bean;

import java.util.Date;

public class McCourseResource {
	private Long id;
	private String name;
	private String introduction;
	private Long fkTeacherId;
	private String photo;
	private String keyword;
	private Integer videoPlayTimes;
	private Long videoDuration;
	private Integer subscribedTimes;
	private Integer paymentType;
	private Integer probationTimes;
	private Double price;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Long getFkTeacherId() {
		return fkTeacherId;
	}

	public void setFkTeacherId(Long fkTeacherId) {
		this.fkTeacherId = fkTeacherId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getVideoPlayTimes() {
		return videoPlayTimes;
	}

	public void setVideoPlayTimes(Integer videoPlayTimes) {
		this.videoPlayTimes = videoPlayTimes;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}

	public Integer getSubscribedTimes() {
		return subscribedTimes;
	}

	public void setSubscribedTimes(Integer subscribedTimes) {
		this.subscribedTimes = subscribedTimes;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getProbationTimes() {
		return probationTimes;
	}

	public void setProbationTimes(Integer probationTimes) {
		this.probationTimes = probationTimes;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
