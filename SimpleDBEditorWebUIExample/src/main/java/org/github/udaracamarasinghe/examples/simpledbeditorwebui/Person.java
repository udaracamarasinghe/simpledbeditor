package org.github.udaracamarasinghe.examples.simpledbeditorwebui;

import org.github.udaracamarasinghe.simpledbeditorwebui.annotations.ConfigEditorField;

public class Person {

	@ConfigEditorField(isEditable = true, isViewableOnTable = true)
	private String name;

	@ConfigEditorField(isEditable = true, isViewableOnTable = true)
	private Integer age;

	public Person() {
		super();
	}

	public Person(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
