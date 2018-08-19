package org.uca.uies.api.simpledbeditorl.dto;

import java.util.List;


/**
 * 
 * @author Udara Amarasinghe
 */
public class ColumnValue {

	private Object id;

	private List<Object> values;

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

}
