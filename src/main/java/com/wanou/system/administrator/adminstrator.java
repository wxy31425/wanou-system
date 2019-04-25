package com.wanou.system.administrator;

public class adminstrator {
	//项目名称
	private String name;
	//项目code 可以作为登录名称
	private String code;
	//项目模块code 可以作为登录密码
	private String module;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public adminstrator(String name, String code, String module) {
		super();
		this.name = name;
		this.code = code;
		this.module = module;
	}
	public adminstrator() {
	}
}
