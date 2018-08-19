package org.uca.uies.api.simpledbeditorl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

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
import org.uca.uies.api.simpledbeditorl.annotations.ConfigEditorField;
import org.uca.uies.api.simpledbeditorl.apis.ConfigCtrlService;
import org.uca.uies.api.simpledbeditorl.apis.EditableConfigPlugin;
import org.uca.uies.api.simpledbeditorl.dto.ColumnValue;
import org.uca.uies.api.simpledbeditorl.dto.ConfigCtrlPageModel;
import org.uca.uies.api.simpledbeditorl.dto.ConfigPluginWrapper;
import org.uca.uies.api.simpledbeditorl.dto.EditorField;
import org.uca.uies.api.simpledbeditorl.dto.EditorModel;
import org.uca.uies.api.simpledbeditorl.dto.KeyValue;
import org.uca.uies.api.simpledbeditorl.dto.EditorModel.EditType;
import org.uca.uies.api.simpledbeditorl.enums.ProcessFailType;
import org.uca.uies.api.simpledbeditorl.exceptions.EditableConfigException;
import org.uca.uies.api.simpledbeditorl.exceptions.HandledException;

/**
 * 
 * @author Udara Amarasinghe
 */
@Controller
@RequestMapping("/config")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConfigEditorController {

	private static final Logger logger = Logger.getLogger(ConfigEditorController.class.getName());

	@Autowired
	private ConfigCtrlService configCtrlService;

	private ConfigCtrlPageModel configCtrlPageModel;

	private String selectedConfigPluginWrapperId;

	private String selectedConfigId;

	private Set<String> configs;

	private List<String> columnNames;

	private List<ColumnValue> columnValues;

	private Object selectedRowId;

	private ConfigPluginWrapper selectedConfigPluginWrapper;

	private EditorModel editorModel;

	private EditableConfigPlugin currentConfigPlugin;

	private String formMessage;

	private String formErrorMessage;

	private int refreshCount;

	private int formMessageSetAtRefreshCount;

	@Value("${configeditor.header-title}")
	private String headerTitle;

	@Value("${configeditor.footer-title}")
	private String footerTitle;
	
	@Value("${loingdetails.show}")
	private Boolean loingdetailsShow;

	@PostConstruct
	private void postConstruct() {
		configCtrlPageModel = new ConfigCtrlPageModel();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getConfig() {
		ModelAndView model = new ModelAndView("ConfigEditor");

		model.addObject("refreshCount", refreshCount++);

		model.addObject("modules", getModules());

		model.addObject("configCtrlPageModel", configCtrlPageModel);

		model.addObject("selectedConfigPluginWrapperId", selectedConfigPluginWrapperId);
		model.addObject("selectedConfigPluginWrapper", selectedConfigPluginWrapper);
		model.addObject("currentConfigPlugin", currentConfigPlugin);
		model.addObject("configs", configs);

		model.addObject("selectedConfigId", selectedConfigId);
		model.addObject("columnNames", columnNames);
		model.addObject("columnValues", columnValues);

		model.addObject("selectedRowId", selectedRowId);

		model.addObject("editorModel", editorModel);

		if (refreshCount != formMessageSetAtRefreshCount) {
			formMessage = null;
			formErrorMessage = null;
		}

		model.addObject("formMessage", formMessage);

		model.addObject("formErrorMessage", formErrorMessage);

		model.addObject("formMessageSetAtRefreshCount", formMessageSetAtRefreshCount);

		model.addObject("headerTitle", headerTitle);

		model.addObject("footerTitle", footerTitle);
		
		model.addObject("loingdetailsShow", loingdetailsShow);

		return model;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView setConfig(@ModelAttribute("configCtrlPageModel") @Valid ConfigCtrlPageModel configCtrlPageModel,
			BindingResult bindingResult) {
		try {
			if (configCtrlPageModel.getSelectedConfigPluginWrapperId() != null
					&& !configCtrlPageModel.getSelectedConfigPluginWrapperId().equals("0")
					&& !configCtrlPageModel.getSelectedConfigPluginWrapperId()
							.equals(this.configCtrlPageModel.getSelectedConfigPluginWrapperId())) {
				this.configCtrlPageModel
						.setSelectedConfigPluginWrapperId(configCtrlPageModel.getSelectedConfigPluginWrapperId());

				Map<String, ConfigPluginWrapper> configPlugins = configCtrlService.availableConfigPlugins();
				this.selectedConfigPluginWrapper = configPlugins
						.get(this.configCtrlPageModel.getSelectedConfigPluginWrapperId());

				this.currentConfigPlugin = this.selectedConfigPluginWrapper.getEditableConfigPlugin();
				this.configs = this.currentConfigPlugin != null ? this.currentConfigPlugin.configTypes().keySet()
						: new HashSet<String>(0);
			} else if (configCtrlPageModel.getSelectedConfigPluginWrapperId() != null
					&& (configCtrlPageModel.getSelectedConfigPluginWrapperId().equals("0")
							&& !"0".equals(this.configCtrlPageModel.getSelectedConfigPluginWrapperId()))) {
				this.configCtrlPageModel.setSelectedConfigPluginWrapperId("0");
				this.selectedConfigPluginWrapper = null;
				this.configs = null;

				this.configCtrlPageModel.setSelectedConfigId("0");
				this.currentConfigPlugin = null;
				this.columnNames = null;
				this.columnValues = null;

				this.editorModel = null;
			}

			if (configCtrlPageModel.getSelectedConfigId() != null
					&& !configCtrlPageModel.getSelectedConfigId().equals("0") && !configCtrlPageModel
							.getSelectedConfigId().equals(this.configCtrlPageModel.getSelectedConfigId())) {
				this.configCtrlPageModel.setSelectedConfigId(configCtrlPageModel.getSelectedConfigId());

				// loadColumnNames(this.configCtrlPageModel.getSelectedConfigId());

				this.columnNames = null;
				this.columnValues = null;

				this.editorModel = null;
			} else {
				this.configCtrlPageModel.setSelectedConfigId("0");
				this.currentConfigPlugin = null;
				this.columnNames = null;
				this.columnValues = null;

				this.editorModel = null;
			}

			updateViewModel();
		} catch (Exception ex) {
			loadFormMessageError(ex.getMessage());
		}

		ModelAndView modelv = new ModelAndView("redirect:/config");
		return modelv;
	}

	@RequestMapping(value = { "/editRow/{editRowId}" }, method = RequestMethod.POST)
	public ModelAndView editRow(@PathVariable("editRowId") String editRowId) {
		loadEditorModel(editRowId);

		ModelAndView modelv = new ModelAndView("redirect:/config");
		return modelv;
	}

	@RequestMapping(value = { "/deleteRow/{editRowId}" }, method = RequestMethod.POST)
	public ModelAndView deleteRow(@PathVariable("editRowId") String editRowId) {
		delete(editRowId);

		ModelAndView modelv = new ModelAndView("redirect:/config");
		return modelv;
	}

	private void delete(String editRowId) {
		if (selectedConfigPluginWrapper != null) {
			EditableConfigPlugin editableConfigPlugin = this.selectedConfigPluginWrapper.getEditableConfigPlugin();

			Class<?> selectedConfigType = editableConfigPlugin.configTypes()
					.get(this.configCtrlPageModel.getSelectedConfigId());

			if (selectedConfigType != null) {
				try {
					editableConfigPlugin.removeConfigEntry(this.configCtrlPageModel.getSelectedConfigId(), editRowId);

					loadFormMessageInfo("SUCESSFULLY DELETED.");

					updateViewModel();
				} catch (Exception e) {
					loadFormMessageError(e.getMessage());
				}
			}
		}
	}

	@RequestMapping(value = { "/editor/save" }, method = RequestMethod.POST)
	public ModelAndView editorSave(@ModelAttribute("editorModel") @Valid EditorModel editorModel,
			@RequestBody String requestBody, BindingResult bindingResult) {
		int from = requestBody.indexOf("saveFormReq=");
		int to = requestBody.indexOf("&", from);
		String substring = requestBody.substring(from, to < 0 ? requestBody.length() : to);

		this.editorModel = editorModel;

		if ("saveFormReq=cancel".equalsIgnoreCase(substring)) {
			this.editorModel = null;
		} else {
			try {

				EditableConfigPlugin editableConfigPlugin = this.selectedConfigPluginWrapper.getEditableConfigPlugin();

				Map<String, Object> configDataFor;

				configDataFor = editableConfigPlugin.findConfigDataFor(this.configCtrlPageModel.getSelectedConfigId());
				Class<?> class1 = editableConfigPlugin.configTypes()
						.get(this.configCtrlPageModel.getSelectedConfigId());

				Map<String, FieldDetails> fields = findAllEditableFields(class1);

				Object object = null;
				if (editorModel.getEditType() == EditType.UPDATE)
					object = configDataFor.get(editorModel.getEditId());
				else if (editorModel.getEditType() == EditType.NEW)
					object = editableConfigPlugin.newConfigEntry(this.configCtrlPageModel.getSelectedConfigId());

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
					editableConfigPlugin.updateConfigEntry(this.configCtrlPageModel.getSelectedConfigId(),
							editorModel.getEditId(), object);

					loadFormMessageInfo("SUCESSFULLY UPDATED.");
				} else if (editorModel.getEditType() == EditType.NEW) {
					KeyValue saveEntry = editableConfigPlugin.saveConfigEntry(
							this.configCtrlPageModel.getSelectedConfigId(), editorModel.getEditId(), object);

					this.editorModel = generateEditorModel(saveEntry.getKey(), saveEntry.getValue(), class1,
							EditType.NEW);

					loadFormMessageInfo("SUCESSFULLY SAVED.");
				}

			} catch (EditableConfigException e) {

				loadFormMessageError(e.getMessage());
				logger.log(Level.SEVERE, e.getMessage(), e);
			} catch (Exception ex) {
				loadFormMessageError(ex.getMessage());
				logger.log(Level.SEVERE, ex.getMessage(), ex);
			}

			updateViewModel();

		}

		ModelAndView modelv = new ModelAndView("redirect:/config");
		return modelv;
	}

	@RequestMapping(value = { "/newEntry" }, method = RequestMethod.POST)
	public ModelAndView newEntry(@RequestBody String body, BindingResult bindingResult) {
		if (selectedConfigPluginWrapper != null) {
			EditableConfigPlugin editableConfigPlugin = this.selectedConfigPluginWrapper.getEditableConfigPlugin();

			Class<?> selectedConfigType = editableConfigPlugin.configTypes()
					.get(this.configCtrlPageModel.getSelectedConfigId());

			if (selectedConfigType != null) {
				try {
					editorModel = new EditorModel(null, new ArrayList<EditorField<?>>(), EditType.NEW);

					Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

					Object object = editableConfigPlugin.newConfigEntry(this.configCtrlPageModel.getSelectedConfigId());

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
		}

		ModelAndView modelv = new ModelAndView("redirect:/config");
		return modelv;
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

	public void loadColumnNames(String selectedConfigId2) {
		if (selectedConfigPluginWrapper != null) {
			EditableConfigPlugin editableConfigPlugin = selectedConfigPluginWrapper.getEditableConfigPlugin();

			Class<?> selectedConfigType = editableConfigPlugin.configTypes().get(selectedConfigId2);

			if (selectedConfigType != null) {
				try {
					columnNames = new ArrayList<>();

					Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

					columnValues = new ArrayList<>();

					Map<String, Object> configDataFor = editableConfigPlugin.findConfigDataFor(selectedConfigId2);

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
				} catch (EditableConfigException | IllegalArgumentException | IllegalAccessException ex) {
					logger.log(Level.SEVERE, ex.getMessage());
				}
			}
		}
	}

	private void loadEditorModel(String editRowId) {
		if (editRowId != null) {
			if (selectedConfigPluginWrapper != null) {
				EditableConfigPlugin editableConfigPlugin = this.selectedConfigPluginWrapper.getEditableConfigPlugin();

				Class<?> selectedConfigType = editableConfigPlugin.configTypes()
						.get(this.configCtrlPageModel.getSelectedConfigId());

				if (selectedConfigType != null) {
					try {
						editorModel = new EditorModel(editRowId, new ArrayList<EditorField<?>>(), EditType.UPDATE);

						Map<String, FieldDetails> allEditableFields = findAllEditableFields(selectedConfigType);

						Map<String, Object> configDataFor = editableConfigPlugin
								.findConfigDataFor(this.configCtrlPageModel.getSelectedConfigId());

						Object object = configDataFor.get(editRowId);

						if (object == null) {
							logger.log(Level.WARNING, "No config record found for " + editRowId);
							return;
						}

						for (FieldDetails field : allEditableFields.values()) {
							Object fieldValue = field.field.get(object);
							Class<?> ss = field.field.getType();

							try {
								String stringValue = fieldValue != null ? convertToString(ss, fieldValue) : null;

								editorModel.add(new EditorField<>(field.field.getName(), ss, stringValue, field.field,
										field.configEditorEditable));
							} catch (HandledException ex) {
								logger.log(Level.SEVERE, ex.getMessage());
							}
						}

					} catch (EditableConfigException | IllegalArgumentException | IllegalAccessException e) {
						logger.log(Level.SEVERE, e.getMessage());
					}
				}
			} else {
				editorModel = null;
			}
		} else {
			if (editorModel != null && editorModel.getEditType() == EditType.NEW) {
				editorModel = new EditorModel(null, editorModel.getEditorFields(), EditType.NEW);
			}
		}
	}

	private void updateViewModel() {
		if (this.configCtrlPageModel != null)
			loadColumnNames(this.configCtrlPageModel.getSelectedConfigId());

		if (this.configCtrlPageModel != null && this.editorModel != null && this.editorModel.getEditId() != null
				&& !this.editorModel.getEditId().isEmpty())
			loadEditorModel(this.editorModel.getEditId());
		else
			loadEditorModel(null);
	}

	public Set<String> getModules() {
		Map<String, ConfigPluginWrapper> configPlugins = configCtrlService.availableConfigPlugins();

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

				if (configEditorEditable != null && configEditorEditable.isEditable()) {
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

	public Map<String, FieldDetails> findAllViewOnlyFields(Class<?> selectedConfigType) {
		Map<String, FieldDetails> fields = new HashMap<>();

		for (Class<?> cc = selectedConfigType; cc != null; cc = cc.getSuperclass()) {
			for (Field field : cc.getDeclaredFields()) {
				ConfigEditorField configEditorEditable = field.getAnnotation(ConfigEditorField.class);

				if (configEditorEditable != null) {
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

	// @RequestMapping(value = "/config", method = RequestMethod.POST)
	// public String saveStudent(@RequestAttribute(name
	// ="selectedConfigPluginWrapperId") String selectedConfigPluginWrapperId,
	// @ModelAttribute(value = "this") ConfigEditorController oo,
	// @ModelAttribute(value = "Sex") ConfigEditorController oo1, Model model,
	// BindingResult errors) {
	// Map<String, Object> asMap = model.asMap();
	// asMap.get("dfs");
	// if (oo.selectedConfigPluginWrapperId != null &&
	// !oo.selectedConfigPluginWrapperId.equals("0")
	// &&
	// !oo.selectedConfigPluginWrapperId.equals(selectedConfigPluginWrapperId))
	// {
	// Map<String, ConfigPluginWrapper> configPlugins =
	// configCtrlService.availableConfigPlugins();
	//
	// selectedConfigPluginWrapper =
	// configPlugins.get(oo.selectedConfigPluginWrapperId);
	//
	// EditableConfigPlugin configPlugin =
	// selectedConfigPluginWrapper.getEditableConfigPlugin();
	//
	// selectedConfigPluginWrapperId = oo.selectedConfigPluginWrapperId;
	// this.configs = configPlugin.configTypes() != null ?
	// configPlugin.configTypes().keySet()
	// : new HashSet<String>(0);
	//
	// model.addAttribute("selectedConfigPluginWrapperId",
	// oo.selectedConfigPluginWrapperId);
	// model.addAttribute("selectedConfigPluginWrapper",
	// selectedConfigPluginWrapper);
	// model.addAttribute("configs",
	// configPlugin.configTypes() != null ? configPlugin.configTypes().keySet()
	// : new HashSet<String>(0));
	//
	// selectedConfigId = "0";
	// model.addAttribute("selectedConfigId", "0");
	// oo.selectedConfigId = "0";
	//
	// model.addAttribute("this", oo);
	// }
	//
	// if (oo.selectedConfigId != null &&
	// !oo.selectedConfigId.equals(selectedConfigId)) {
	// selectedConfigId = oo.selectedConfigId;
	// model.addAttribute("selectedConfigId", oo.selectedConfigId);
	//
	// loadColumnNames(model, oo.selectedConfigId);
	//
	// model.addAttribute("this", oo);
	// }
	//
	// return "ConfigEditor";
	// }

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String saveeStudent(Model model) {

		return "ConfigEditor";
	}

	// @ModelAttribute("selectedConfigPluginWrapperId")
	// public String getSelectedConfigPluginWrapperId() {
	// return selectedConfigPluginWrapperId;
	// }
	//
	// public void setSelectedConfigPluginWrapperId(
	// @ModelAttribute("selectedConfigPluginWrapperId") String
	// selectedConfigPluginWrapperId) {
	//
	// }

	// @ModelAttribute("configs")
	// public Set<String> getConfigs() {
	// return configs;
	// }
	//
	// // @ModelAttribute("selectedConfigPluginWrapperId")
	// // public String selectedConfigPluginWrapperId() {
	// // return selectedConfigPluginWrapperId;
	// // }
	// //
	// // @ModelAttribute("selectedConfigId")
	// // public String selectedConfigId() {
	// // return selectedConfigId;
	// // }
	//
	// @ModelAttribute("modules")
	// public Set<String> modules() {
	// Map<String, ConfigPluginWrapper> configPlugins =
	// configCtrlService.availableConfigPlugins();
	//
	// return configPlugins.keySet();
	// }
	//
	// public boolean isSelectedConfigTypeModel(String userId) {
	// if (selectedConfigId != null) {
	// return selectedConfigId.equals(userId);
	// }
	// return false;
	// }
	//
	// public boolean isSelectedConfigModel(String userId) {
	// if (selectedConfigPluginWrapperId != null) {
	// return selectedConfigPluginWrapperId.equals(userId);
	// }
	// return false;
	// }
	//
	// @ModelAttribute("smokeTests")
	// public Map<String, String> allUsers1() {
	// Map<String, String> map = new HashMap<String, String>();
	//
	// map.put("key1", "value1");
	// map.put("key2", "value2");
	// map.put("key3", "value3");
	//
	// return map;
	// }
	//
	// @ModelAttribute("values")
	// public List<ColumnValue> values() {
	// List<ColumnValue> values = new ArrayList();
	//
	// for (int i = 0; i < 10; i++) {
	// ColumnValue value = new ColumnValue();
	// value.setId("" + i);
	//
	// List<Object> userList = new ArrayList();
	//
	// userList.add("Value1");
	// userList.add("Value2");
	//
	// value.setValues(userList);
	//
	// values.add(value);
	// }
	//
	// return values;
	// }
	//
	// public String getSelectedModule() {
	// return this.selectedModule;
	// }
	//
	// public String setSelectedModule(@ModelAttribute("selectedModule") String
	// selectedModule) {
	// this.selectedModule = selectedModule;
	//
	// return this.selectedModule;
	// }
	//
	// @RequestMapping(value = "/saveStudent", method = RequestMethod.POST)
	// public String saveStudent(@ModelAttribute User student, BindingResult
	// errors, Model model) {
	// return "Saved";
	// }
	//
	// @RequestMapping(value = "/showForm", method = RequestMethod.GET)
	// public String showForm(Model model) {
	// Foo foo = new Foo();
	// foo.setBar("bar");
	// foo.setBoo("boo1");
	//
	// model.addAttribute("foo", foo);
	//
	// return "ConfigEditor2";
	// }
	//
	// @RequestMapping(value = "/processForm", method = RequestMethod.POST)
	// public String processForm(@ModelAttribute(value = "foo") Foo foo) {
	// foo.getBar();
	//
	// return "ConfigEditor2";
	// }
	//
	// class DTO {
	// public String name;
	// }
	//
	// @ModelAttribute("selectedConfigPluginWrapperId")
	// public String getSelectedConfigPluginWrapperId() {
	// return selectedConfigPluginWrapperId;
	// }
	//
	// public void
	// setSelectedConfigPluginWrapperId(@ModelAttribute("selectedConfigPluginWrapperId")
	// String selectedConfigPluginWrapperId) {
	// this.selectedConfigPluginWrapperId = selectedConfigPluginWrapperId;
	// }
	//
	// @ModelAttribute("selectedConfigId")
	// public String getSelectedConfigId() {
	// return selectedConfigId;
	// }
	//
	// public void setSelectedConfigId(String selectedConfigId) {
	// this.selectedConfigId = selectedConfigId;
	// }
	//
	// public Object getSelectedRowId() {
	// return selectedRowId;
	// }
	//
	// public void setSelectedRowId(Object selectedRowId) {
	// this.selectedRowId = selectedRowId;
	// }
	//
	// @ModelAttribute("columnNames")
	// public List<String> getColumnNames() {
	// return columnNames;
	// }
	//
	// public void setColumnNames(List<String> columnNames) {
	// this.columnNames = columnNames;
	// }
	//
	// @ModelAttribute("columnValues")
	// public Set<ColumnValue> getColumnValues() {
	// return columnValues;
	// }
	//
	// public void setColumnValues(Set<ColumnValue> columnValues) {
	// this.columnValues = columnValues;
	// }

}
