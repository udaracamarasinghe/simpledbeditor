package org.github.udaracamarasinghe.simpledbeditorwebui.dto;

public class User {

	private Integer id;

	private String name;

	public User() {

	}

	public User(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected(Integer userId) {
		if (userId != null) {
			return userId.equals(id);
		}
		return false;
	}

}
