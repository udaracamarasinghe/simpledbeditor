package org.github.udaracamarasinghe.simpledbeditorwebui.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.github.udaracamarasinghe.simpledbeditorwebui.annotations.ConfigEditorField;
import org.github.udaracamarasinghe.simpledbeditorwebui.apis.SimpleDBEditorService;
import org.github.udaracamarasinghe.simpledbeditorwebui.apis.TableManager;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.ColumnValue;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.ConfigCtrlPageModel;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.EditorField;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.EditorModel;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.KeyValue;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.ModuleManager;
import org.github.udaracamarasinghe.simpledbeditorwebui.dto.EditorModel.EditType;
import org.github.udaracamarasinghe.simpledbeditorwebui.enums.ProcessFailType;
import org.github.udaracamarasinghe.simpledbeditorwebui.exceptions.EditableConfigException;
import org.github.udaracamarasinghe.simpledbeditorwebui.exceptions.HandledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author Udara Amarasinghe
 */
@Controller
@RequestMapping("${simpledbeditor.path:/simpledbeditor}")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigEditorController {

	private static final Logger logger = Logger.getLogger(ConfigEditorController.class.getName());

	@Autowired
	private SimpleDBEditorService configCtrlService;

	private String formMessage;

	private String formErrorMessage;

	private int refreshCount;

	private int formMessageSetAtRefreshCount;

	@Value("${simpledbeditor.path:/simpledbeditor}")
	private String contextPath;

	@Value("${configeditor.header-title:Simple DB Editor}")
	private String headerTitle;

	@Value("${configeditor.footer-title:Simple DB Editor by UCA}")
	private String footerTitle;

	@Value("${loingdetails.show:false}")
	private Boolean loingdetailsShow;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getConfig(@ModelAttribute("configCtrlPageModel") @Valid ConfigCtrlPageModel configCtrlPageModel,
			BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("ConfigEditor");

		model.addObject("modules", getModules());

		model.addObject("selectedModuleId", 0);

		model.addObject("formMessage", formMessage);

		model.addObject("formErrorMessage", formErrorMessage);

		model.addObject("formMessageSetAtRefreshCount", formMessageSetAtRefreshCount);

		model.addObject("headerTitle", headerTitle);

		model.addObject("footerTitle", footerTitle);

		model.addObject("loingdetailsShow", loingdetailsShow);

		model.addObject("editRow", contextPath + "/editRow/");

		model.addObject("contextPath", contextPath);

		return model;
	}

	@RequestMapping(value = "/selectmodule", method = RequestMethod.POST)
	public ModelAndView selectModule(@RequestBody(required = false) JsonNode jsonNode, Model modelll) {
		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		ModelAndView model = new ModelAndView("ConfigEditor :: top-container");

		Set<String> modules = configPlugins.keySet();

		model.addObject("modules", modules);

		if (jsonNode.has("selectedModuleId")) {
			String selectedModuleId = jsonNode.findValue("selectedModuleId").textValue();

			ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

			if (configPluginWrapper != null) {
				model.addObject("tables", configPluginWrapper.getTableManager().tableDtoTypes().keySet());

				model.addObject("selectedModuleId", selectedModuleId);
			}
		}

		model.addObject("headerTitle", headerTitle);

		model.addObject("footerTitle", footerTitle);

		model.addObject("loingdetailsShow", loingdetailsShow);

		return model;
	}

	@RequestMapping(value = "/selecttable", method = RequestMethod.POST)
	public ModelAndView selectTable(@RequestBody(required = false) JsonNode jsonNode, Model modell) {
		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		Set<String> modules = configPlugins.keySet();

		ModelAndView model = new ModelAndView("ConfigEditor :: view-table-and-editor");

		model.addObject("modules", modules);

		if (jsonNode.has("selectedTableId")) {
			String selectedModuleId = jsonNode.findValue("selectedModuleId").textValue();

			ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

			String selectedTableId = jsonNode.findValue("selectedTableId").textValue();

			loadColumnNames(selectedTableId, configPluginWrapper, model);
		}

		model.addObject("formMessage", formMessage);

		model.addObject("formErrorMessage", formErrorMessage);

		model.addObject("formMessageSetAtRefreshCount", formMessageSetAtRefreshCount);

		model.addObject("headerTitle", headerTitle);

		model.addObject("footerTitle", footerTitle);

		model.addObject("loingdetailsShow", loingdetailsShow);

		return model;
	}

	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	public ModelAndView add(@RequestBody(required = false) JsonNode jsonNode, BindingResult bindingResult) {
		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		Set<String> modules = configPlugins.keySet();

		ModelAndView model = new ModelAndView("ConfigEditor :: result-editor");

		model.addObject("modules", modules);

		if (jsonNode.has("selectedTableId")) {
			String selectedModuleId = jsonNode.findValue("selectedModuleId").textValue();

			ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

			TableManager editableConfigPlugin = configPluginWrapper.getTableManager();

			String selectedTableId = jsonNode.findValue("selectedTableId").textValue();

			Class<?> selectedConfigType = editableConfigPlugin.tableDtoTypes().get(selectedTableId);

			EditorModel editorModel = null;

			if (selectedConfigType != null) {
				try {
					editorModel = new EditorModel(null, new ArrayList<EditorField<?>>(), EditType.NEW);

					Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

					Object object = editableConfigPlugin.newRecordEntry(selectedTableId);

					for (FieldDetails field : allEditableFields.values()) {
						if (field.configEditorEditable.isEditable()) {
							Object fieldValue = field.field.get(object);
							Class<?> ss = field.field.getType();

							try {
								String stringValue = fieldValue != null ? convertToString(ss, fieldValue) : null;

								editorModel.add(new EditorField<>(field.field.getName(), ss, stringValue, field.field,
										field.configEditorEditable));
							} catch (HandledException ex) {
								ex.printStackTrace();
							}
						}
					}

				} catch (EditableConfigException | IllegalArgumentException | IllegalAccessException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);

					editorModel = null;

					loadFormMessageError(e.getMessage());
				}
			}

			model.addObject("editorModel", editorModel);
		}

		return model;

	}

	@RequestMapping(value = { "/editor/save" }, method = RequestMethod.POST)
	public ModelAndView editorSave(@RequestBody JsonNode requestBody, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("ConfigEditor :: result-editor");

		try {
			Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

			Set<String> modules = configPlugins.keySet();

			model.addObject("modules", modules);

			if (requestBody.has("selectedTableId")) {
				String selectedModuleId = requestBody.findValue("selectedModuleId").textValue();
				String selectedTableId = requestBody.findValue("selectedTableId").textValue();
				JsonNode formData = requestBody.get("formData");

				ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

				TableManager tableManager = configPluginWrapper.getTableManager();

				Class<?> selectedTableType = tableManager.tableDtoTypes().get(selectedTableId);

				Map<String, Object> records = tableManager.findRecordsFor(selectedTableId);

				Map<String, FieldDetails> fields = findAllEditableFields(selectedTableType);

				EditorModel editorModel = convertToEditorModel(formData);

				Object object = null;
				if (EditType.UPDATE.name().equals(formData.findValue("editType").textValue()))
					object = records.get(editorModel.getEditId());
				else if (EditType.NEW.name().equals(formData.findValue("editType").textValue()))
					object = tableManager.newRecordEntry(selectedTableId);

				boolean isInvalidInputData = false;
				for (EditorField<?> editorField : editorModel.getEditorFields()) {
					try {
						FieldDetails field = fields.get(editorField.getName());

						field.field.setAccessible(true);

						Object convertString = convertString(field.field.getType(), editorField.getValue());

						field.field.set(object, convertString);
					} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
						isInvalidInputData = true;
						logger.log(Level.SEVERE, e.getMessage());
					}
				}

				if (isInvalidInputData)
					throw new EditableConfigException(ProcessFailType.VALIDATION_FALIED,
							"Please check, some input data types invalid.");

				if (editorModel.getEditType() == EditType.UPDATE) {
					KeyValue saveEntry = tableManager.updateRecordEntry(selectedTableId, editorModel.getEditId(),
							object);

					model.addObject("editorModel", generateEditorModel(saveEntry.getKey(), saveEntry.getValue(),
							selectedTableType, EditType.UPDATE));

					loadColumnNames(selectedTableId, configPluginWrapper, model);

					model.setViewName("ConfigEditor :: view-table-and-editor");
				} else if (editorModel.getEditType() == EditType.NEW) {
					KeyValue saveEntry = tableManager.saveRecordEntry(selectedTableId, editorModel.getEditId(), object);

					model.addObject("editorModel", generateEditorModel(saveEntry.getKey(), saveEntry.getValue(),
							selectedTableType, EditType.UPDATE));

					loadColumnNames(selectedTableId, configPluginWrapper, model);

					model.setViewName("ConfigEditor :: view-table-and-editor");
				}
			}

		} catch (EditableConfigException e) {

			loadFormMessageError(e.getMessage());
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (Exception ex) {
			loadFormMessageError(ex.getMessage());
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}

		return model;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editRow(@RequestBody(required = false) JsonNode jsonNode, Model modell) {
		ModelAndView model = new ModelAndView("ConfigEditor :: result-editor");

		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		Set<String> modules = configPlugins.keySet();

		model.addObject("modules", modules);

		if (jsonNode.has("selectedTableId")) {
			String selectedModuleId = jsonNode.findValue("selectedModuleId").textValue();

			ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

			String selectedTableId = jsonNode.findValue("selectedTableId").textValue();

			TableManager editableConfigPlugin = configPluginWrapper.getTableManager();

			Class<?> selectedConfigType = editableConfigPlugin.tableDtoTypes().get(selectedTableId);

			if (selectedConfigType != null) {
				try {
					String editId = jsonNode.findValue("editId").textValue();

					Object object = editableConfigPlugin.findRecords(selectedTableId, editId);

					EditorModel editorModel = generateEditorModel(editId, object, selectedConfigType, EditType.UPDATE);

					model.addObject("editorModel", editorModel);
				} catch (Exception e) {
					loadFormMessageError(e.getMessage());
				}
			}
		}

		return model;
	}

	@RequestMapping(value = { "/editor/clear" }, method = RequestMethod.POST)
	public ModelAndView editorClear(@RequestBody JsonNode requestBody, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("ConfigEditor :: result-editor");

		model.addObject("editorModel", null);

		return model;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String deleteRow(@RequestBody(required = false) JsonNode jsonNode, Model model) {
		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		Set<String> modules = configPlugins.keySet();

		model.addAttribute("modules", modules);

		if (jsonNode.has("selectedTableId")) {
			String selectedModuleId = jsonNode.findValue("selectedModuleId").textValue();

			ModuleManager configPluginWrapper = configPlugins.get(selectedModuleId);

			String selectedTableId = jsonNode.findValue("selectedTableId").textValue();

			TableManager editableConfigPlugin = configPluginWrapper.getTableManager();

			Class<?> selectedConfigType = editableConfigPlugin.tableDtoTypes().get(selectedTableId);

			if (selectedConfigType != null) {
				try {
					String deleteId = jsonNode.findValue("deleteId").textValue();

					editableConfigPlugin.removeRecordEntry(selectedTableId, deleteId);

					loadFormMessageInfo("SUCESSFULLY DELETED.");

					loadColumnNames(selectedTableId, configPluginWrapper, model);
				} catch (Exception e) {
					loadFormMessageError(e.getMessage());
				}
			}
		}

		model.addAttribute("formMessage", formMessage);

		model.addAttribute("formErrorMessage", formErrorMessage);

		model.addAttribute("formMessageSetAtRefreshCount", formMessageSetAtRefreshCount);

		model.addAttribute("headerTitle", headerTitle);

		model.addAttribute("footerTitle", footerTitle);

		model.addAttribute("loingdetailsShow", loingdetailsShow);

		return "ConfigEditor :: result-view-table";
	}

	public EditorModel generateEditorModel(String editKey, Object object, Class<?> selectedConfigType,
			EditType editType) {
		EditorModel editorModel = null;

		try {
			editorModel = new EditorModel(editKey, new ArrayList<EditorField<?>>(), editType);

			Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

			for (FieldDetails field : allEditableFields.values()) {
				if (field.configEditorEditable.isEditable()) {
					Object fieldValue = field.field.get(object);
					Class<?> ss = field.field.getType();

					try {
						String stringValue = fieldValue != null ? convertToString(ss, fieldValue) : null;

						editorModel.add(new EditorField<>(field.field.getName(), ss, stringValue, field.field,
								field.configEditorEditable));
					} catch (HandledException ex) {
						ex.printStackTrace();
					}
				}
			}

		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return editorModel;
	}

	private EditorModel convertToEditorModel(JsonNode jsonNode) {
		EditorModel editorModel = new EditorModel();

		editorModel.setEditId(jsonNode.findValue("editId").textValue());
		editorModel.setEditType(EditType.valueOf(jsonNode.findValue("editType").textValue()));

		editorModel.setEditorFields(new ArrayList<>());
		for (int i = 0;; i++) {
			String prefix = "editorFields[" + i + "].";
			if (jsonNode.has("editorFields[" + i + "]." + "name")) {
				EditorField<?> editorField = new EditorField<>();
				editorField.setName(jsonNode.findValue(prefix + "name").textValue());
				editorField.setValue(jsonNode.findValue(prefix + "value").textValue());

				editorModel.add(editorField);
			} else
				break;
		}

		return editorModel;
	}

	@SuppressWarnings("unchecked")
	private <T> T convertString(Class<T> toType, String value) {
		if (value == null || value.isEmpty())
			return null;

		if (toType.equals(String.class)) {
			return toType.cast(value);
		} else if (toType.equals(Integer.class) || toType.equals(int.class)) {
			return toType.cast(Integer.valueOf(value));
			// return toType.cast(value);
		} else if (toType.equals(Long.class) || toType.equals(long.class)) {
			return toType.cast(Long.valueOf(value));
			// return toType.cast(value);
		} else if (toType.equals(Float.class) || toType.equals(float.class)) {
			return toType.cast(Float.valueOf(value));
			// return toType.cast(value);
		} else if (toType.equals(Double.class) || toType.equals(double.class)) {
			return toType.cast(Double.valueOf(value));
			// return toType.cast(value);
		} else if (toType.equals(Boolean.class)) {
			return toType.cast(Boolean.valueOf(value));
			// return toType.cast(value);
		} else if (toType.equals(boolean.class)) {
			Boolean parseBoolean = Boolean.parseBoolean(value);
			return (T) parseBoolean;
		} else {
			throw new RuntimeException("Not valid data type.");
		}
	}

	private String convertToString(Class<?> fromType, Object value) throws HandledException {
		if (fromType.equals(String.class)) {
			return value.toString();
		} else if (fromType.equals(Integer.class) || fromType.equals(int.class)) {
			return Integer.toString((Integer) value);
		} else if (fromType.equals(Long.class) || fromType.equals(long.class)) {
			return Long.toString((Long) value);
		} else if (fromType.equals(Float.class) || fromType.equals(float.class)) {
			return Float.toString((Float) value);
		} else if (fromType.equals(Double.class) || fromType.equals(double.class)) {
			return Double.toString((Double) value);
		} else if (fromType.equals(Boolean.class) || fromType.equals(boolean.class)) {
			return Boolean.toString((Boolean) value);
		} else {
			throw new HandledException("Not valid data type.");
		}
	}

	public void loadColumnNames(String selectedConfigId2, ModuleManager configPluginWrapper, Model model) {
		TableManager editableConfigPlugin = configPluginWrapper.getTableManager();

		Class<?> selectedConfigType = editableConfigPlugin.tableDtoTypes().get(selectedConfigId2);

		if (selectedConfigType != null) {
			try {
				ArrayList<String> columnNames = new ArrayList<>();
				ArrayList<ColumnValue> columnValues = new ArrayList<>();

				Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

				Map<String, Object> configDataFor = editableConfigPlugin.findRecordsFor(selectedConfigId2);

				for (Object key : configDataFor.keySet()) {
					Object object = configDataFor.get(key);

					List<Object> values = new ArrayList<Object>();
					for (FieldDetails field : allEditableFields.values()) {
						if (field.configEditorEditable.isViewableOnTable()) {
							field.field.setAccessible(true);
							Object fieldValue = field.field.get(object);

							values.add(fieldValue);
							if (!columnNames.contains(field.field.getName()))
								columnNames.add(field.field.getName());
						}
					}

					ColumnValue e = new ColumnValue();
					e.setId(key);
					e.setValues(values);

					columnValues.add(e);
				}

				model.addAttribute("columnNames", columnNames);
				model.addAttribute("columnValues", columnValues);
			} catch (EditableConfigException | IllegalArgumentException | IllegalAccessException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			}
		}

	}

	public void loadColumnNames(String selectedConfigId2, ModuleManager configPluginWrapper, ModelAndView model) {
		TableManager editableConfigPlugin = configPluginWrapper.getTableManager();

		Class<?> selectedConfigType = editableConfigPlugin.tableDtoTypes().get(selectedConfigId2);

		if (selectedConfigType != null) {
			try {
				ArrayList<String> columnNames = new ArrayList<>();
				ArrayList<ColumnValue> columnValues = new ArrayList<>();

				Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

				Map<String, Object> configDataFor = editableConfigPlugin.findRecordsFor(selectedConfigId2);

				for (Object key : configDataFor.keySet()) {
					Object object = configDataFor.get(key);

					List<Object> values = new ArrayList<Object>();
					for (FieldDetails field : allEditableFields.values()) {
						if (field.configEditorEditable.isViewableOnTable()) {
							field.field.setAccessible(true);
							Object fieldValue = field.field.get(object);

							values.add(fieldValue);
							if (!columnNames.contains(field.field.getName()))
								columnNames.add(field.field.getName());
						}
					}

					ColumnValue e = new ColumnValue();
					e.setId(key);
					e.setValues(values);

					columnValues.add(e);
				}

				model.addObject("columnNames", columnNames);
				model.addObject("columnValues", columnValues);
			} catch (EditableConfigException | IllegalArgumentException | IllegalAccessException ex) {
				logger.log(Level.SEVERE, ex.getMessage());
			}
		}

	}

	public Set<String> getModules() {
		Map<String, ModuleManager> configPlugins = configCtrlService.availableModules();

		return configPlugins.keySet();
	}

	private void loadFormMessageInfo(String message) {
		formMessageSetAtRefreshCount = refreshCount + 1;
		this.formErrorMessage = null;
		this.formMessage = message;
	}

	private void loadFormMessageError(String message) {
		formMessageSetAtRefreshCount = refreshCount + 1;
		this.formMessage = null;
		this.formErrorMessage = message;
	}

	public Map<String, FieldDetails> findAllEditableFields(Class<?> selectedConfigType) {
		Map<String, FieldDetails> fields = new HashMap<>();

		for (Class<?> cc = selectedConfigType; cc != null; cc = cc.getSuperclass()) {
			for (Field field : cc.getDeclaredFields()) {
				ConfigEditorField configEditorEditable = field.getAnnotation(ConfigEditorField.class);

				if (configEditorEditable != null
						&& (configEditorEditable.isEditable() || configEditorEditable.isViewableOnTable())) {
					field.setAccessible(true);
					FieldDetails fieldDetails = new FieldDetails();

					fieldDetails.configEditorEditable = configEditorEditable;
					fieldDetails.field = field;

					fields.put(field.getName(), fieldDetails);
				}
			}
		}

		return fields;
	}

	class FieldDetails {
		ConfigEditorField configEditorEditable;

		Field field;
	}

}
