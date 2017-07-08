package org.akapps.tools.generators.code;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 * A class used for tests
 *
 * @author Antoine Kapps
 */
public class JavaSoftware {

    private String group;
    private String artifact;
    private LocalDate releaseDate;

    private BigDecimal annualLicence;

    private Set<String> knownReferences;

    public JavaSoftware() {
        this.knownReferences = Collections.emptySet();
    }

    public JavaSoftware(String group, String artifact, LocalDate releaseDate, BigDecimal annualLicence,
                        Set<String> knownReferences) {
        this.group = group;
        this.artifact = artifact;
        this.releaseDate = releaseDate;
        this.annualLicence = annualLicence;
        this.knownReferences = knownReferences;
    }
}
