package io.arunkumarun.java2ts.maven.plugin;

import io.arunkumarun.java2ts.core.Generator;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Mojo(
        name = "java-to-typescript-generate",
        defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true
)
public final class JavaToTypescriptGenerateMojo extends AbstractMojo {
    private static final Logger logger = LoggerFactory.getLogger(JavaToTypescriptGenerateMojo.class);

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true, required = true)
    private String projectBuildDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        logger.info("==============> Start JavaToTypescriptGenerateMojo Execute <==============");
        try {
            List<URL> urls = new ArrayList<>();

            for (String compileClasspathElement : project.getCompileClasspathElements()) {
                urls.add(new File(compileClasspathElement).toURI().toURL());
            }

            try (URLClassLoader urlClassLoader = new URLClassLoader(urls.toArray(new URL[0]), Thread.currentThread().getContextClassLoader())) {
                Generator.generate(urlClassLoader, projectBuildDirectory);
            }

        } catch (DependencyResolutionRequiredException | IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("==============> End JavaToTypescriptGenerateMojo Execute <==============");
    }
}
