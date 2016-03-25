package yinzhi.micro_client.network.vo;

import java.util.List;

public class YZChapterVO {

	private String chapterTitle;
	private List<YZItemResourceVO> resourceList;

	public String getChapterTitle() {
		return chapterTitle;
	}

	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}

	public List<YZItemResourceVO> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<YZItemResourceVO> resourceList) {
		this.resourceList = resourceList;
	}

}
