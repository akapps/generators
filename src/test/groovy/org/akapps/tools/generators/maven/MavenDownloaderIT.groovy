package org.akapps.tools.generators.maven

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.nio.file.Files

class MavenDownloaderIT {

    private static final long JSON_SIZE = 45944

    @Rule
    public TemporaryFolder temp = new TemporaryFolder()

    MavenDownloader downloader

    @Before
    void setUp() {
        downloader = new MavenDownloader(temp.newFolder("download").toPath())
    }

    @Test
    void testGet_FullName() {
        def path = downloader.get("org.json:json:20090211")

        assert Files.exists(path)
        assert Files.size(path) == JSON_SIZE
    }

    @Test
    void testGet_GroupArtifactVersion() {
        def path = downloader.get("org.json", "json", "20090211")

        assert Files.exists(path)
        assert Files.size(path) == JSON_SIZE
    }

    @Test
    void get_NoDownloadIfExists() {
        def path = downloader.target.resolve("json-20090211.jar")
        Files.createFile(path)

        def result = downloader.get("org.json", "json", "20090211")
        assert result == path
        assert Files.size(path) == 0
    }
}
