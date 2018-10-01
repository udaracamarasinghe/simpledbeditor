package org.uca.uies.api.simpledbeditorl.dto;

import java.io.Serializable;

import org.uca.uies.api.simpledbeditorl.apis.EditableConfigPlugin;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public class ConfigPluginWrapper implements Serializable {

	private static final long serialVersionUID = -2714461779886296036L;

	private String pluginName;

	private EditableConfigPlugin editableConfigPlugin;

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public EditableConfigPlugin getEditableConfigPlugin() {
		return editableConfigPlugin;
	}

	public void setEditableConfigPlugin(EditableConfigPlugin editableConfigPlugin) {
		this.editableConfigPlugin = editableConfigPlugin;
	}

	public boolean isSelected(String pluginName) {
		if (pluginName != null) {
			return this.pluginName.equals(pluginName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pluginName == null) ? 0 : pluginName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigPluginWrapper other = (ConfigPluginWrapper) obj;
		if (pluginName == null) {
			if (other.pluginName != null)
				return false;
		} else if (!pluginName.equals(other.pluginName))
			return false;
		return true;
	}

}
