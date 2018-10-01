package org.github.udaracamarasinghe.simpledbeditorwebui.dto;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public class ConfigType {

	private String name;

	private Class<?> typeDefinition;

	public ConfigType() {

	}

	public ConfigType(String name, Class<?> typeDefinition) {
		super();
		this.name = name;
		this.typeDefinition = typeDefinition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getTypeDefinition() {
		return typeDefinition;
	}

	public void setTypeDefinition(Class<?> typeDefinition) {
		this.typeDefinition = typeDefinition;
	}

}
