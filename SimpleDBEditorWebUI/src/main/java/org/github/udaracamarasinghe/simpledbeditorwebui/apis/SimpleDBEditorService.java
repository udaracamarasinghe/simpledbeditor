package org.github.udaracamarasinghe.simpledbeditorwebui.apis;

import java.util.Map;

import org.github.udaracamarasinghe.simpledbeditorwebui.dto.ModuleManager;
import org.springframework.context.annotation.ComponentScan;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
@ComponentScan(basePackages = { "org.github.udaracamarasinghe.simpledbeditorwebui" })
public abstract class SimpleDBEditorService {

	/**
	 * Returns available modules.
	 * 
	 * @return available modules
	 */
	public abstract Map<String, ModuleManager> availableModules();

}
