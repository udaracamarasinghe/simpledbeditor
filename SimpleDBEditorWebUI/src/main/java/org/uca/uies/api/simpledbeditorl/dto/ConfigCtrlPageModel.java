package org.uca.uies.api.simpledbeditorl.dto;

import java.io.Serializable;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public class ConfigCtrlPageModel implements Serializable {

	private static final long serialVersionUID = 887570221514256795L;

	public String selectedConfigPluginWrapperId;

	public String selectedConfigId;

	public String getSelectedConfigPluginWrapperId() {
		return selectedConfigPluginWrapperId;
	}

	public void setSelectedConfigPluginWrapperId(String selectedConfigPluginWrapperId) {
		this.selectedConfigPluginWrapperId = selectedConfigPluginWrapperId;
	}

	public String getSelectedConfigId() {
		return selectedConfigId;
	}

	public void setSelectedConfigId(String selectedConfigId) {
		this.selectedConfigId = selectedConfigId;
	}

}
