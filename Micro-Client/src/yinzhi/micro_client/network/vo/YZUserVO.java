package yinzhi.micro_client.network.vo;

public class YZUserVO extends YZBaseVO {

	private String username;
	private String token;
	private String nickname;
	private String avatarPicPath;
	private String classes;
	private String grade;
	private String college;
	private String school;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatarPicPath() {
		return avatarPicPath;
	}

	public void setAvatarPicPath(String avatarPicPath) {
		this.avatarPicPath = avatarPicPath;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

}
