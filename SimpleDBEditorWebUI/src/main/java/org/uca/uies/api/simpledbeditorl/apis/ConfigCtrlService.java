package org.uca.uies.api.simpledbeditorl.apis;

import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.uca.uies.api.simpledbeditorl.dto.ConfigPluginWrapper;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
@ComponentScan(basePackages = { "org.uca.uies.api.simpledbeditorl" })
public abstract class ConfigCtrlService {

	public abstract Map<String, ConfigPluginWrapper> availableConfigPlugins();

}
