package yinzhi.micro_client.network.vo;

import java.util.List;

public class YZRecommendCourseVO {

	private String recommendTitle;

	private List<YZCourseVO> courseList;

	public String getRecommendTitle() {
		return recommendTitle;
	}

	public void setRecommendTitle(String recommendTitle) {
		this.recommendTitle = recommendTitle;
	}

	public List<YZCourseVO> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<YZCourseVO> courseList) {
		this.courseList = courseList;
	}

}
