package org.uca.uies.api.simpledbeditorl.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.uca.uies.api.simpledbeditorl.enums.FieldType;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ConfigEditorField {

	boolean isViewableOnTable() default false;

	boolean isEditable() default false;

	FieldType fieldType() default FieldType.TEXT_FIELD;

	
}
