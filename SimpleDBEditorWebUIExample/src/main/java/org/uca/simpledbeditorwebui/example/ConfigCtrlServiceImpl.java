package org.uca.simpledbeditorwebui.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.uca.uies.api.simpledbeditorl.apis.ConfigCtrlService;
import org.uca.uies.api.simpledbeditorl.dto.ConfigPluginWrapper;

@Service
public class ConfigCtrlServiceImpl extends ConfigCtrlService {

	@Override
	public Map<String, ConfigPluginWrapper> availableConfigPlugins() {
		HashMap<String, ConfigPluginWrapper> hashMap = new HashMap<>();
		ConfigPluginWrapper configPluginWrapper1 = new ConfigPluginWrapper();
		configPluginWrapper1.setPluginName("Plugin 1");
		configPluginWrapper1.setEditableConfigPlugin(new EditableConfigPluginImpl());

		hashMap.put("Config-one", configPluginWrapper1);
		return hashMap;
	}

}
