package com.arunkumarun.java2ts.core;

import com.arunkumarun.java2ts.core.ts.TsClass;
import com.arunkumarun.java2ts.core.ts.TsField;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Generator {
    private static final Logger logger = LoggerFactory.getLogger(Generator.class);
    private static final Map<String, String> typescriptFieldTypeMap = Map.ofEntries(
            Map.entry(Integer.class.getName(), "number"),
            Map.entry(Long.class.getName(), "number"),
            Map.entry(String.class.getName(), "string")
    );

    public static void generate(URLClassLoader classLoader, String outputDir) {
        try (ScanResult scan = new ClassGraph()
                .enableAllInfo()
                .overrideClasspath((Object[]) classLoader.getURLs())
                .ignoreClassVisibility()
                .scan()) {
            ClassInfoList classes = scan.getAllClasses();

            logger.info("Classes Size: {}", classes.size());

            List<TsClass> tsClasses = classes.stream().map(Generator::generateTSClass).toList();

            Map<String, Object> model = new HashMap<>();
            model.put("tsClasses", tsClasses);

            Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

            configuration.setClassForTemplateLoading(Generator.class, "");

            configuration.setLocale(Locale.US);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template template = configuration.getTemplate("ts-class.ftl");


            File outputFile = new File(new File(outputDir, "java-to-typescript"), "output.ts");
            outputFile.getParentFile().mkdirs();
            /*Writer consoleWriter = new OutputStreamWriter(System.out);
            template.process(model, consoleWriter);*/

            Writer fileWriter = new FileWriter(outputFile);
            template.process(model, fileWriter);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    private static TsClass generateTSClass(ClassInfo clazz) {
        TsClass tsClass = new TsClass();
        tsClass.setClassName(clazz.getSimpleName());
        tsClass.setFields(clazz.getFieldInfo().stream()
                .map(field -> {
                    TsField tsField = new TsField();
                    tsField.setFieldName(field.getName());
                    tsField.setFieldType(typescriptFieldTypeMap.get(field.getTypeSignatureOrTypeDescriptor().toString()));
                    return tsField;
                })
                .toList());

        tsClass.setExport(true);

        return tsClass;
    }

}
