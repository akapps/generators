package org.akapps.tools.generators.maven

import groovy.transform.CompileStatic

import java.nio.file.Files
import java.nio.file.Path

/**
 * Component helping to download Maven artifacts.
 *
 * @author Antoine Kapps
 */
@CompileStatic
class MavenDownloader {

    private static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2"

    final String maven
    final Path target

    MavenDownloader(String maven, Path target) {
        this.maven = formatRepo(maven)
        this.target = target
    }

    /** Use MavenCentral repository */
    MavenDownloader(Path target) {
        this(MAVEN_CENTRAL, target)
    }

    private static String formatRepo(String value) {
        return value.endsWith("/") ? value.substring(0, value.length()-1) : value
    }

    private static toUrl(String id) {
        return id.replace(".", "/")
    }

    private downloadFromMaven(String group, String artifact, String version) {
        def jarName = "$artifact-${version}.jar"
        def url = "$maven/${toUrl(group)}/$artifact/$version/$jarName".toURL()

        HttpURLConnection http = url.openConnection() as HttpURLConnection
        if (http.responseCode == 200) {
            http.inputStream.withCloseable {
                Files.copy(it, target.resolve(jarName))
            }
            println "Downloaded $jarName"
        }
        else {
            throw new IllegalArgumentException("${http.responseCode} - Library $group:$artifact:$version not found on MavenCentral")
        }
    }

    /**
     * Downloads an artifacts explicitly named. Only JAR artifacts are currently supported.
     * Already present files are not overwritten.
     *
     * @return path where the artifact resides
     */
    Path get(String group, String artifact, String version) {
        def jarName = "$artifact-${version}.jar"
        Path targetFile = target.resolve(jarName)

        if (Files.exists(targetFile))
            println "$jarName already found"
        else
            downloadFromMaven(group, artifact, version)

        return targetFile
    }

    /**
     * Downloads an artifacts named using {@literal "<groupId>:<artifactId>:<version>"}. Only JAR artifacts are currently supported.
     * Already present files are not overwritten.
     *
     * @return path where the artifact resides
     */
    Path get(String fullName) {
        def split = fullName.split(":")
        return get(split[0], split[1], split[2])
    }
}
