package org.github.udaracamarasinghe.simpledbeditorwebui.dto;

import java.io.Serializable;

import org.github.udaracamarasinghe.simpledbeditorwebui.apis.TableManager;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public class ModuleManager implements Serializable {

	private static final long serialVersionUID = -2714461779886296036L;

	private String moduleName;

	private TableManager tableManager;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String pluginName) {
		this.moduleName = pluginName;
	}

	public TableManager getTableManager() {
		return tableManager;
	}

	public void setTableManager(TableManager editableConfigPlugin) {
		this.tableManager = editableConfigPlugin;
	}

	public boolean isSelected(String pluginName) {
		if (pluginName != null) {
			return this.moduleName.equals(pluginName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
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
		ModuleManager other = (ModuleManager) obj;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		return true;
	}

}
