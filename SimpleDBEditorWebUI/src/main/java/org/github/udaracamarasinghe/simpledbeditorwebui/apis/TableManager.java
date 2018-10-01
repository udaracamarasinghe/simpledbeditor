package org.github.udaracamarasinghe.simpledbeditorwebui.apis;

import java.util.Map;

import org.github.udaracamarasinghe.simpledbeditorwebui.dto.KeyValue;
import org.github.udaracamarasinghe.simpledbeditorwebui.exceptions.EditableConfigException;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public interface TableManager {

	/**
	 * 
	 * @return
	 */
	Map<String, Class<?>> tableDtoTypes();

	/**
	 * 
	 * @param tableName
	 * @param key
	 * @return
	 * @throws EditableConfigException
	 */
	Object findRecords(String tableName, String key) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws EditableConfigException
	 */
	Map<String, Object> findRecordsFor(String tableName) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws EditableConfigException
	 */
	Object newRecordEntry(String tableName) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @return
	 * @throws EditableConfigException
	 */
	Object emptyRecordEntry(String tableName) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @param key
	 * @param record
	 * @return
	 * @throws EditableConfigException
	 */
	KeyValue saveRecordEntry(String tableName, String key, Object record) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @param key
	 * @param record
	 * @return
	 * @throws EditableConfigException
	 */
	KeyValue updateRecordEntry(String tableName, String key, Object record) throws EditableConfigException;

	/**
	 * 
	 * @param tableName
	 * @param key
	 * @throws EditableConfigException
	 */
	void removeRecordEntry(String tableName, String key) throws EditableConfigException;
}
