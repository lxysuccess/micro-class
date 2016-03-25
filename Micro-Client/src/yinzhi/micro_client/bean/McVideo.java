package yinzhi.micro_client.bean;

import java.util.Date;

public class McVideo {
	private Long id;
	private String name;
	private String introduction;
	private Long duration;
	private Long fkItemResourceId;
	private Long fkChapterId;
	private Integer praise;
	private Integer sequence;
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
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getFkItemResourceId() {
		return fkItemResourceId;
	}
	public void setFkItemResourceId(Long fkItemResourceId) {
		this.fkItemResourceId = fkItemResourceId;
	}
	public Long getFkChapterId() {
		return fkChapterId;
	}
	public void setFkChapterId(Long fkChapterId) {
		this.fkChapterId = fkChapterId;
	}
	public Integer getPraise() {
		return praise;
	}
	public void setPraise(Integer praise) {
		this.praise = praise;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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
