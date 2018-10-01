package org.uca.uies.api.simpledbeditorl.apis;

import java.util.Map;

import org.uca.uies.api.simpledbeditorl.dto.KeyValue;
import org.uca.uies.api.simpledbeditorl.exceptions.EditableConfigException;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public interface TableManager {

	Map<String, Class<?>> configTypes();

	Object findConfig(String configName, String key) throws EditableConfigException;

	Map<String, Object> findRecordsFor(String configName) throws EditableConfigException;

	Object newRowEntry(String configName) throws EditableConfigException;

	Object emptyConfigEntry(String configName) throws EditableConfigException;

	KeyValue saveConfigEntry(String configName, String key, Object record) throws EditableConfigException;

	KeyValue updateConfigEntry(String configName, String key, Object record) throws EditableConfigException;

	void removeConfigEntry(String configName, String key) throws EditableConfigException;
}
