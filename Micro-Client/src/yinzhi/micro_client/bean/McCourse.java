package yinzhi.micro_client.bean;

import java.util.Date;

public class McCourse {
	private Long id;
	private Long fkClassId;
	private Long fkTeacherId;
	private Long fkCourseResourceId;
	private String name;;
	private String introduction;
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFkClassId() {
		return fkClassId;
	}

	public void setFkClassId(Long fkClassId) {
		this.fkClassId = fkClassId;
	}

	public Long getFkTeacherId() {
		return fkTeacherId;
	}

	public void setFkTeacherId(Long fkTeacherId) {
		this.fkTeacherId = fkTeacherId;
	}

	public Long getFkCourseResourceId() {
		return fkCourseResourceId;
	}

	public void setFkCourseResourceId(Long fkCourseResourceId) {
		this.fkCourseResourceId = fkCourseResourceId;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
