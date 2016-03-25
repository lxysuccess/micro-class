package yinzhi.micro_client.bean;

import java.util.Date;

public class McItemResource {
	private Long id;
	private Integer type;
	private Long fkResourceId;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getFkResourceId() {
		return fkResourceId;
	}

	public void setFkResourceId(Long fkResourceId) {
		this.fkResourceId = fkResourceId;
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
