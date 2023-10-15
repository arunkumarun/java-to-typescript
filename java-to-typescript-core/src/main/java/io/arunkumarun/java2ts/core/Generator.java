package io.arunkumarun.java2ts.core;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import io.arunkumarun.java2ts.core.ts.TsClass;
import io.arunkumarun.java2ts.core.ts.TsField;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.isNull;

public class Generator {
    private static final Logger logger = LoggerFactory.getLogger(Generator.class);
    private final GeneratorConfiguration generatorConfiguration;
    private static final Map<String, String> typescriptFieldTypeMap = Map.ofEntries(
            Map.entry(Integer.class.getName(), "number"),
            Map.entry(Long.class.getName(), "number"),
            Map.entry(String.class.getName(), "string")
    );

    public Generator(GeneratorConfiguration generatorConfiguration) {
        this.generatorConfiguration = generatorConfiguration;
    }

    public void generate(GeneratorInput generatorInput, GeneratorOutput generatorOutput) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(ObjectMapper.findModules(generatorConfiguration.getClassLoader()));
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());

        try (ScanResult scan = new ClassGraph()
                .ignoreParentClassLoaders()
                .overrideClassLoaders(generatorConfiguration.getClassLoader())
                //.overrideClasspath((Object[]) generatorConfiguration.getClassLoader().getURLs())
                .acceptPackages(generatorInput.getIncludePackages().toArray(new String[0]))
                .ignoreClassVisibility()
                .enableAllInfo()
                .scan()) {

            List<URL> classpathURLs = scan.getClasspathURLs();
            logger.info("Classpath URL Size: {}", classpathURLs.size());
            for (URL classpathURL : classpathURLs) {
                logger.info("Classpath URL: {}", classpathURL);
            }

            ClassInfoList classes = scan.getAllClasses();

            logger.info("Classes Size: {}", classes.size());

            List<TsClass> tsClasses = classes.stream()
                    .map(classInfo -> {
                        try {
                            //logger.info("Class Info: [{}]", classInfo);
                            JavaType javaType = mapper.getTypeFactory()
                                    .constructType(classInfo.loadClass());;
                            BeanDescription introspect = mapper.getSerializationConfig().introspect(javaType);
                            List<BeanPropertyDefinition> properties = introspect.findProperties();
                            return generateTSClass(classInfo, properties);
                        } catch (Exception e) {
                            logger.error("Exception", e);
                        }
                        return null;
                    })
                    .toList();

            Map<String, Object> model = new HashMap<>();
            model.put("tsClasses", tsClasses);

            for (ClassInfo clazz : classes) {
                JavaType javaType = mapper.getTypeFactory()
                        .constructType(clazz.loadClass());
                BeanDescription introspect = mapper.getSerializationConfig().introspect(javaType);
                List<BeanPropertyDefinition> properties = introspect.findProperties();

                logger.info("{} Properties: {}", clazz.getSimpleName(), properties.stream().map(property -> property.getFullName().getSimpleName()).toList());
            }

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

            configuration.setClassForTemplateLoading(Generator.class, "");

            configuration.setLocale(Locale.US);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template template = configuration.getTemplate("ts-class.ftl");


            File outputFile = new File(new File(generatorOutput.getOutputDir(), "java-to-typescript"), "output.ts");
            boolean directoryCreated = outputFile.getParentFile().mkdirs();
            if (directoryCreated) logger.info("{} is created", outputFile.getParentFile().toString());
            /*Writer consoleWriter = new OutputStreamWriter(System.out);
            template.process(model, consoleWriter);*/

            Writer fileWriter = new FileWriter(outputFile);
            template.process(model, fileWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static TsClass generateTSClass(ClassInfo clazz, List<BeanPropertyDefinition> jacksonProperties) {
        TsClass tsClass = new TsClass();
        tsClass.setClassName(clazz.getSimpleName());

        tsClass.setFields(jacksonProperties.stream()
                .map(property -> {
                    String fieldType = typescriptFieldTypeMap.get(property.getRawPrimaryType().getName());
                    TsField tsField = new TsField();
                    tsField.setFieldName(property.getName());
                    tsField.setFieldType(isNull(fieldType) ? "any" : fieldType);
                    return tsField;
                })
                .toList()
        );

        /*tsClass.setFields(clazz.getFieldInfo().stream()
                .map(field -> {
                    String fieldType = typescriptFieldTypeMap.get(field.getTypeSignatureOrTypeDescriptor().toString());
                    TsField tsField = new TsField();
                    tsField.setFieldName(field.getName());
                    tsField.setFieldType(isNull(fieldType) ? "any" : fieldType);
                    return tsField;
                })
                .toList());*/

        tsClass.setExport(true);

        return tsClass;
    }

}
