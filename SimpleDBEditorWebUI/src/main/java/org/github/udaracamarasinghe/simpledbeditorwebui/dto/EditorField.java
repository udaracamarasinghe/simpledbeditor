package org.github.udaracamarasinghe.simpledbeditorwebui.dto;

import java.lang.reflect.Field;

import org.github.udaracamarasinghe.simpledbeditorwebui.annotations.ConfigEditorField;
import org.github.udaracamarasinghe.simpledbeditorwebui.enums.FieldType;

/**
 * 
 * @author Udara Amarasinghe
 */
public class EditorField<T> {

	private String name;
	private Class<T> t;
	private String value;
	private Field field;
	private boolean isEditable;
	private boolean isViewable;
	private boolean isViewableOnTable;
	private FieldType fieldType = FieldType.TEXT_FIELD;

	public EditorField() {
		name = null;
		t = null;
		value = null;
	}

	public EditorField(String name, Class<T> t, String value, Field field) {
		super();
		this.name = name;
		this.t = t;
		this.value = value;
		this.field = field;
	}

	public EditorField(String name, Class<T> t, String value, Field field, ConfigEditorField configEditorEditable) {
		super();
		this.name = name;
		this.t = t;
		this.value = value;
		this.field = field;

		isEditable = configEditorEditable.isEditable();
		isViewable = configEditorEditable.isEditable();
		isViewableOnTable = configEditorEditable.isViewableOnTable();

		this.fieldType = configEditorEditable.fieldType();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<T> getT() {
		return t;
	}

	public void setT(Class<T> t) {
		this.t = t;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public String getDataType() {
		if (t != null) {
			int lastIndexOf = t.getName().lastIndexOf(".");
			return t.getName().substring(++lastIndexOf);
		}
		return null;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isViewable() {
		return isViewable;
	}

	public void setViewable(boolean isViewable) {
		this.isViewable = isViewable;
	}

	public boolean isViewableOnTable() {
		return isViewableOnTable;
	}

	public void setViewableOnTable(boolean isViewableOnTable) {
		this.isViewableOnTable = isViewableOnTable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorField<?> other = (EditorField<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
