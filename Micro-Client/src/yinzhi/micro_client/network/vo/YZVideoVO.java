package yinzhi.micro_client.network.vo;

public class YZVideoVO extends YZBaseVO {

	private String videoId;
	private Integer isAllowed;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Integer getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(Integer isAllowed) {
		this.isAllowed = isAllowed;
	}

}
