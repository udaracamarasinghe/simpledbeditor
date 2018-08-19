package org.uca.uies.api.simpledbeditorl.dto;

import java.util.List;

/**
 * 
 * @author Udara Amarasinghe
 */
public class EditorModel {

	private EditType editType;

	private String editId;

	private List<EditorField<?>> editorFields;

	public EditorModel() {
		editorFields = null;
	}

	public EditorModel(String editId, List<EditorField<?>> editorFields, EditType editType) {
		this.editId = editId;
		this.editorFields = editorFields;
		this.editType = editType;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public List<EditorField<?>> getEditorFields() {
		return editorFields;
	}

	public void setEditorFields(List<EditorField<?>> editorFields) {
		this.editorFields = editorFields;
	}

	public void add(EditorField<?> editorField) {
		editorFields.add(editorField);
	}

	public EditType getEditType() {
		return editType;
	}

	public void setEditType(EditType editType) {
		this.editType = editType;
	}

	public enum EditType {
		NEW, UPDATE
	}

}
