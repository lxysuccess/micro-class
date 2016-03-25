package yinzhi.micro_client.bean;

import java.util.Date;

public class McCourseItemResourceMap {
	private Long id;
	private Long fkCourseResourceId;
	private Long fkItemResourceId;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFkCourseResourceId() {
		return fkCourseResourceId;
	}

	public void setFkCourseResourceId(Long fkCourseResourceId) {
		this.fkCourseResourceId = fkCourseResourceId;
	}

	public Long getFkItemResourceId() {
		return fkItemResourceId;
	}

	public void setFkItemResourceId(Long fkItemResourceId) {
		this.fkItemResourceId = fkItemResourceId;
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
