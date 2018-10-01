package org.github.udaracamarasinghe.simpledbeditorwebui.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.github.udaracamarasinghe.simpledbeditorwebui.enums.FieldType;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ConfigEditorField {

	boolean isViewableOnTable() default false;

	boolean isEditable() default false;

	FieldType fieldType() default FieldType.TEXT_FIELD;

	
}
