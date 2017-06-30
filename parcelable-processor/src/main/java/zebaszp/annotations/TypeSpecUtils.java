package zebaszp.annotations;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

class TypeSpecUtils {
	
	static void addGetter(TypeSpec.Builder type, FieldSpec field) {
		addGetter(type, field, getGetterName(field));
	}
	
	static void addGetter(TypeSpec.Builder type, FieldSpec field, String name) {
		MethodSpec getter = MethodSpec.methodBuilder(name)
				.returns(field.type)
				.addModifiers(Modifier.PUBLIC)
				.addStatement("return $N", field)
				.build();
		type.addMethod(getter);
	}
	
	static void addSetter(TypeSpec.Builder type, FieldSpec field) {
		addSetter(type, field, getSetterName(field));
	}
	
	static void addSetter(TypeSpec.Builder type, FieldSpec field, String name) {
		ParameterSpec setterParam = ParameterSpec.builder(field.type, field.name).build();
		MethodSpec setter = MethodSpec.methodBuilder(name)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(setterParam)
				.addStatement("this.$1N = $1N", field)
				.build();
		
		type.addMethod(setter);
	}
	
	static String getGetterName(FieldSpec field) {
		return "get" + capitalizeFieldName(field);
	}
	
	static String getSetterName(FieldSpec field) {
		return "set" + capitalizeFieldName(field);
	}
	
	private static String capitalizeFieldName(FieldSpec field) {
		return field.name.substring(0, 1).toUpperCase() + field.name.substring(1);
	}
	
	private TypeSpecUtils() {
		// Avoid instantiation of utils class
	}
}
