package com.skwibble.skwibblebook.story_group;

public class Actors {

	
	public String title,description,created_date,picture,comnt,username,child_pic,user,buddies;
	public int likes,id,likes2,cmnt_count;
	public boolean para_1;
	public Actors() {}

	public Actors( boolean para_1, int cmnt_count, String buddies,String user,String child_pic,int id,int likes,String title,String description,String created_date,String picture,int likes2,String comnt,String username) {
		super();
		
		this.id = id;
		this.likes=likes;
		this.title=title;
		this.description=description;
		this.created_date=created_date;
		this.child_pic=child_pic;
		this.id=id;
		this.picture=picture;
		this.user=user;
		this.likes2=likes2;
		this.comnt=comnt;
		this.username=username;
		this.buddies=buddies;
		this.para_1=para_1;
		this.cmnt_count=cmnt_count;
		
	}

	public boolean getpara_1() {
		return para_1;
	}

	public void setPara_1(boolean para_1) {
		this.para_1 = para_1;
	}

	public String getchild_pic() {
		return child_pic;
	}

	public void setchild_pic(String child_pic) {
		this.child_pic = child_pic;
	}
	
	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}
	public String getuser() {
		return user;
	}

	public void setuser(String user) {
		this.user = user;
	}
	
	public String getcomnt() {
		return comnt;
	}

	public void setcomnt(String comnt) {
		this.comnt = comnt;
	}
	
	public int getcmnt_count() {
		return cmnt_count;
	}

	public void setcmnt_count(int cmnt_count) {
		this.cmnt_count = cmnt_count;
	}

	public String getbuddies() {
		return buddies;
	}

	public void setbuddies(String buddies) {
		this.buddies = buddies;
	}
	
	public String getpicture() {
		return picture;
	}

	public void setpicture(String picture) {
		this.picture = picture;
	}
	
	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}
	
	
	
	public String getcreated_date() {
		return created_date;
	}

	public void setcreated_date(String created_date) {
		this.created_date = created_date;
	}
	
	public String getdescription() {
		return description;
	}

	public void setdescription(String description) {
		this.description = description;
	}
	
	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}
	
	public int getlikes() {
		return likes;
	}

	public void setlikes(int likes) {
		this.likes = likes;
	}

	public int getlikes2() {
		return likes2;
	}

	public void setlikes2(int likes2) {
		this.likes2 = likes2;
	}



}
