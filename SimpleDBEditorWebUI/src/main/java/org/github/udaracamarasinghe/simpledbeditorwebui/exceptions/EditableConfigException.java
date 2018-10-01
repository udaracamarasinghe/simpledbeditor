package org.github.udaracamarasinghe.simpledbeditorwebui.exceptions;

import org.github.udaracamarasinghe.simpledbeditorwebui.enums.ProcessFailType;

/**
 * 
 * @author Udara Amarasinghe
 */
public class EditableConfigException extends Exception {

	private static final long serialVersionUID = -8273557768372219863L;

	private ProcessFailType processFailType = ProcessFailType.ERROR;

	public EditableConfigException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EditableConfigException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public EditableConfigException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public EditableConfigException(ProcessFailType processFailType, String arg0) {
		super(arg0);
		this.processFailType = processFailType;
	}

	public ProcessFailType getProcessFailType() {
		return processFailType;
	}

	public void setProcessFailType(ProcessFailType processFailType) {
		this.processFailType = processFailType;
	}

}
