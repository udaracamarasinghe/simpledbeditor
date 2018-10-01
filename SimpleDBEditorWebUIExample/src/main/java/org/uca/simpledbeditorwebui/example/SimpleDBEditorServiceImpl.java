package org.uca.simpledbeditorwebui.example;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uca.uies.api.simpledbeditorl.apis.SimpleDBEditorService;
import org.uca.uies.api.simpledbeditorl.dto.ModuleManager;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
@Service
public class SimpleDBEditorServiceImpl extends SimpleDBEditorService {

	private HashMap<String, ModuleManager> modulesHashMap;

	@Autowired
	private PersonsTableManagerImpl personsTableManger;

	@PostConstruct
	private void postConstruct() {
		modulesHashMap = new HashMap<>();

		ModuleManager moduleManager = new ModuleManager();
		moduleManager.setModuleName("Persons");
		moduleManager.setTableManager(personsTableManger);

		modulesHashMap.put("Module-one", moduleManager);
	}

	@Override
	public Map<String, ModuleManager> availableModules() {
		return modulesHashMap;
	}

}
