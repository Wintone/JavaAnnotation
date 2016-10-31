package annotation.framework;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Table(name = "BeanTable")
class Bean {
	@Column(name = "firstField")
	int firstField;
	@Column(name = "secondField")
	String secondField;
}