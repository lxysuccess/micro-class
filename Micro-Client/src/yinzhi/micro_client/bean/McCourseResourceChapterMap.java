package yinzhi.micro_client.bean;

import java.util.Date;

public class McCourseResourceChapterMap {
	private Long id;
	private String name;
	private Long fkCourseResourceId;
	private Integer squence;
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

	public Long getFkCourseResourceId() {
		return fkCourseResourceId;
	}

	public void setFkCourseResourceId(Long fkCourseResourceId) {
		this.fkCourseResourceId = fkCourseResourceId;
	}

	public Integer getSquence() {
		return squence;
	}

	public void setSquence(Integer squence) {
		this.squence = squence;
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
