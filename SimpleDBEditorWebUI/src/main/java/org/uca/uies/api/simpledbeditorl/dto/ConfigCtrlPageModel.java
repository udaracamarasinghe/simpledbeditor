package org.uca.uies.api.simpledbeditorl.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.uca.uies.api.simpledbeditorl.apis.EditableConfigPlugin;

/*
 * 
 * @Auther: Udara C Amarasinghe
 */
public class ConfigCtrlPageModel implements Serializable {

	private static final long serialVersionUID = 887570221514256795L;

	public String selectedConfigPluginWrapperId;

	public String selectedConfigId;

	private EditableConfigPlugin currentConfigPlugin;

	private Set<String> configs;

	private List<String> columnNames;

	private List<ColumnValue> columnValues;

	private String selectedRowId;

	private EditorModel editorModel;

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

	public EditableConfigPlugin getCurrentConfigPlugin() {
		return currentConfigPlugin;
	}

	public void setCurrentConfigPlugin(EditableConfigPlugin currentConfigPlugin) {
		this.currentConfigPlugin = currentConfigPlugin;
	}

	public Set<String> getConfigs() {
		return configs;
	}

	public void setConfigs(Set<String> configs) {
		this.configs = configs;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public List<ColumnValue> getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(List<ColumnValue> columnValues) {
		this.columnValues = columnValues;
	}

	public String getSelectedRowId() {
		return selectedRowId;
	}

	public void setSelectedRowId(String selectedRowId) {
		this.selectedRowId = selectedRowId;
	}

	public EditorModel getEditorModel() {
		return editorModel;
	}

	public void setEditorModel(EditorModel editorModel) {
		this.editorModel = editorModel;
	}

	private String name;

	private Integer age;

	private String status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
