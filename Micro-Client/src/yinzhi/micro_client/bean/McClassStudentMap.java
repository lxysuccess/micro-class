package yinzhi.micro_client.bean;

import java.util.Date;

public class McClassStudentMap {
	private Long id;
	private Long fkStudentId;
	private Long fkClassId;
	private Date createTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFkStudentId() {
		return fkStudentId;
	}

	public void setFkStudentId(Long fkStudentId) {
		this.fkStudentId = fkStudentId;
	}

	public Long getFkClassId() {
		return fkClassId;
	}

	public void setFkClassId(Long fkClassId) {
		this.fkClassId = fkClassId;
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
