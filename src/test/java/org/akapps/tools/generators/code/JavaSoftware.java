package org.akapps.tools.generators.code;

import java.math.BigDecimal;
import java.util.Set;

/**
 * A class used for tests
 *
 * @author Antoine Kapps
 */
public class JavaSoftware {

    private String group;
    private String artifact;

    private BigDecimal annualLicence;

    private Set<String> knownReferences;

    public JavaSoftware(String group, String artifact, BigDecimal annualLicence, Set<String> knownReferences) {
        this.group = group;
        this.artifact = artifact;
        this.annualLicence = annualLicence;
        this.knownReferences = knownReferences;
    }
}
