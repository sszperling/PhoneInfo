package zebaszp.annotations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public final class ParcelableSpecProcessor extends AbstractProcessor {
	
	private static final String SUFFIX = "Spec";
	private static final String NAME_PATTERN = ".*" + SUFFIX + "$";
	
	private static final ClassName PARCEL = ClassName.get("android.os", "Parcel");
	private static final ClassName CREATOR = ClassName.get("android.os", "Parcelable.Creator");
	
	
	private ErrorReporter mErrorReporter;
	private Types mTypeUtils;
	
	static final class Property {
		final String fieldName;
		final VariableElement element;
		final TypeName typeName;
		final ImmutableSet<String> annotations;
		
		Property(String fieldName, VariableElement element) {
			this.fieldName = fieldName;
			this.element = element;
			this.typeName = TypeName.get(element.asType());
			this.annotations = getAnnotations(element);
		}
		
		boolean isNullable() {
			return this.annotations.contains("Nullable");
		}
		
		boolean isFinal() {
			return this.element.getModifiers().contains(Modifier.FINAL);
		}
		
		private ImmutableSet<String> getAnnotations(VariableElement element) {
			ImmutableSet.Builder<String> builder = ImmutableSet.builder();
			for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
				builder.add(annotation.getAnnotationType().asElement().getSimpleName().toString());
			}
			
			return builder.build();
		}
	}
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		mErrorReporter = new ErrorReporter(processingEnv);
		mTypeUtils = processingEnv.getTypeUtils();
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Collections.singleton(ParcelableSpec.class.getCanonicalName());
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Collection<? extends Element> annotatedElements =
				env.getElementsAnnotatedWith(ParcelableSpec.class);
		List<TypeElement> types = new ImmutableList.Builder<TypeElement>()
				.addAll(ElementFilter.typesIn(annotatedElements))
				.build();
		
		for (TypeElement type : types) {
			processType(type);
		}
		
		// We are the only ones handling ParcelableSpec annotations
		return true;
	}
	
	private void processType(TypeElement type) {
		ParcelableSpec autoParcel = type.getAnnotation(ParcelableSpec.class);
		if (autoParcel == null) {
			mErrorReporter.abortWithError("annotation processor for @ParcelableSpec was invoked with a" +
					"type annotated differently; compiler bug? O_o", type);
		}
		if (type.getKind() != ElementKind.CLASS) {
			mErrorReporter.abortWithError("@" + ParcelableSpec.class.getName() + " only applies to classes", type);
		}
		if (!type.getSimpleName().toString().matches(NAME_PATTERN)) {
			mErrorReporter.abortWithError("Name must match " + NAME_PATTERN + ".", type);
		}
		if (ancestorIsParcelableSpec(type)) {
			mErrorReporter.abortWithError("One @ParcelableSpec class shall not extend another", type);
		}
		
		checkModifiersIfNested(type);
		
		// get the fully-qualified class name
		String fqClassName = generatedClassName(type);
		// class name
		String className = TypeUtil.simpleNameOf(fqClassName);
		JavaFile source = generateClass(type, className, type.getSimpleName().toString());
		writeSourceFile(fqClassName, source);
		
	}
	
	private void writeSourceFile(String className, JavaFile file) {
		try {
			Filer filer = processingEnv.getFiler();
			file.writeTo(filer);
		} catch (IOException e) {
			// This should really be an error, but we make it a warning in the hope of resisting Eclipse
			// bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=367599. If that bug manifests, we may get
			// invoked more than once for the same file, so ignoring the ability to overwrite it is the
			// right thing to do. If we are unable to write for some other reason, we should get a compile
			// error later because user code will have a reference to the code we were supposed to
			// generate (new AutoValue_Foo() or whatever) and that reference will be undefined.
			processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
					"Could not write generated class " + className + ": " + e);
		}
	}
	
	private JavaFile generateClass(TypeElement type, String className, String classToExtend) {
		if (type == null) {
			mErrorReporter.abortWithError("generateClass was invoked with null type", null);
		}
		if (className == null) {
			mErrorReporter.abortWithError("generateClass was invoked with null class name", type);
		}
		if (classToExtend == null) {
			mErrorReporter.abortWithError("generateClass was invoked with null parent class", type);
		}
		
		List<VariableElement> classFields = ElementFilter.fieldsIn(type.getEnclosedElements());
		
		// get the properties
		ImmutableList<Property> properties = buildProperties(classFields);
		
		// Generate the Parcelable class
		String pkg = TypeUtil.packageNameOf(type);
		TypeName classTypeName = ClassName.get(pkg, className);
		TypeSpec.Builder subClass = TypeSpec.classBuilder(className)
				// Class must be always final
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				// Add the DEFAULT constructor
				.addMethod(generateConstructor(properties))
				// Add the private constructor
				.addMethod(generateConstructorFromParcel(processingEnv, properties))
				// overrides describeContents()
				.addMethod(generateDescribeContents())
				// static final CREATOR
				.addField(generateCreator(classTypeName))
				// overrides writeToParcel()
				.addMethod(generateWriteToParcel(processingEnv, properties)); // generate writeToParcel()
		
		for (VariableElement field : classFields) {
			addField(subClass, field);
		}
		
		if (!ancestorIsParcelable(processingEnv, type)) {
			// Implement android.os.Parcelable if the ancestor does not do it.
			subClass.addSuperinterface(ClassName.get("android.os", "Parcelable"));
		}
		
		
		return JavaFile.builder(pkg, subClass.build()).build();
	}
	
	
	private void addField(TypeSpec.Builder parcelableClass, VariableElement field) {
		FieldSpec.Builder classFieldBuilder = FieldSpec
				.builder(TypeName.get(field.asType()), field.getSimpleName().toString());
		
		for (Modifier modifier : field.getModifiers()) {
			classFieldBuilder.addModifiers(modifier);
		}
		for (AnnotationMirror annotation : field.getAnnotationMirrors()) {
			classFieldBuilder.addAnnotation(AnnotationSpec.get(annotation));
		}
		
		FieldSpec classField = classFieldBuilder.build();
		parcelableClass.addField(classField);
		
		if(!field.getModifiers().contains(Modifier.FINAL)) {
			TypeSpecUtils.addSetter(parcelableClass, classField);
		}
		TypeSpecUtils.addGetter(parcelableClass, classField);
	}
	
	private ImmutableList<Property> buildProperties(List<VariableElement> elements) {
		ImmutableList.Builder<Property> builder = ImmutableList.builder();
		for (VariableElement element : elements) {
			builder.add(new Property(element.getSimpleName().toString(), element));
		}
		return builder.build();
	}
	
	private MethodSpec generateConstructor(ImmutableList<Property> properties) {
		
		List<ParameterSpec> params = Lists.newArrayListWithCapacity(properties.size());
		for (Property property : properties) {
			if(property.isFinal()) {
				params.add(ParameterSpec.builder(property.typeName, property.fieldName).build());
			}
		}
		
		MethodSpec.Builder builder = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addParameters(params);
		
		for (ParameterSpec param : params) {
			builder.addStatement("this.$N = $N", param.name, param.name);
		}
		
		return builder.build();
	}
	
	private MethodSpec generateConstructorFromParcel(
			ProcessingEnvironment env,
			ImmutableList<Property> properties) {
		
		// Create the PRIVATE constructor from Parcel
		MethodSpec.Builder builder = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PRIVATE)      // private
				.addParameter(PARCEL, "in"); // input param
		
		// Iterate all properties, initialize them
		for (Property p : properties) {
			Parcelables.readValue(builder, p, env.getTypeUtils());
		}
		return builder.build();
	}
	
	private String generatedClassName(TypeElement type) {
		return type.getQualifiedName().toString().replaceAll(SUFFIX, "");
	}
	
	private MethodSpec generateWriteToParcel(
			ProcessingEnvironment env,
			ImmutableList<Property> properties) {
		ParameterSpec dest = ParameterSpec
				.builder(PARCEL, "dest")
				.build();
		ParameterSpec flags = ParameterSpec.builder(int.class, "flags").build();
		MethodSpec.Builder builder = MethodSpec.methodBuilder("writeToParcel")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.addParameter(dest)
				.addParameter(flags);
		
		// then write all the properties
		for (Property p : properties) {
			Parcelables.writeValue(builder, p, dest, flags, env.getTypeUtils());
		}
		
		return builder.build();
	}
	
	private MethodSpec generateDescribeContents() {
		return MethodSpec.methodBuilder("describeContents")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(int.class)
				.addStatement("return 0")
				.build();
	}
	
	private FieldSpec generateCreator(
			TypeName type) {
		// Method createFromParcel()
		ParameterSpec inputParcel = ParameterSpec
				.builder(PARCEL, "in").build();
		MethodSpec.Builder createFromParcel = MethodSpec.methodBuilder("createFromParcel")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(type)
				.addParameter(inputParcel)
				.addStatement("return new $T($N)", type, inputParcel);
		
		// Method newArray()
		MethodSpec.Builder newArray = MethodSpec.methodBuilder("newArray")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(ArrayTypeName.of(type))
				.addParameter(int.class, "size")
				.addStatement("return new $T[size]", type);
		
		// Creator implementation
		TypeName creator = ParameterizedTypeName.get(CREATOR, type);
		
		TypeSpec creatorImpl = TypeSpec.anonymousClassBuilder("")
				.superclass(creator)
				.addMethod(createFromParcel.build())
				.addMethod(newArray.build())
				.build();
		
		return FieldSpec
				.builder(creator, "CREATOR", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
				.initializer("$L", creatorImpl)
				.build();
	}
	
	private void checkModifiersIfNested(TypeElement type) {
		ElementKind enclosingKind = type.getEnclosingElement().getKind();
		if (enclosingKind.isClass() || enclosingKind.isInterface()) {
			if (type.getModifiers().contains(Modifier.PRIVATE)) {
				mErrorReporter.abortWithError("@ParcelableSpec class must not be private", type);
			}
			if (!type.getModifiers().contains(Modifier.STATIC)) {
				mErrorReporter.abortWithError("Nested @ParcelableSpec class must be static", type);
			}
		}
		// In principle type.getEnclosingElement() could be an ExecutableElement (for a class
		// declared inside a method), but since RoundEnvironment.getElementsAnnotatedWith doesn't
		// return such classes we won't see them here.
	}
	
	private boolean ancestorIsParcelableSpec(TypeElement type) {
		while (true) {
			TypeMirror parentMirror = type.getSuperclass();
			if (parentMirror.getKind() == TypeKind.NONE) {
				return false;
			}
			TypeElement parentElement = (TypeElement) mTypeUtils.asElement(parentMirror);
			if (parentElement.getAnnotation(ParcelableSpec.class) != null) {
				return true;
			}
			type = parentElement;
		}
	}
	
	private boolean ancestorIsParcelable(ProcessingEnvironment env, TypeElement type) {
		// TODO: 15/07/16 check recursively
		TypeMirror classType = type.asType();
		TypeMirror parcelable = env.getElementUtils().getTypeElement("android.os.Parcelable").asType();
		return TypeUtil.isClassOfType(env.getTypeUtils(), parcelable, classType);
	}
}