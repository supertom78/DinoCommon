package fr.liksi.kpi.domain;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class ArchUnitTest {
    @Test
    void domainShouldOnlyDependOnRestrictedPackage() {
        final var domainPackageName = "fr.liksi.kpi.domain";
        JavaClasses domainPackages = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(domainPackageName);

        ArchRuleDefinition.classes()
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(
                        domainPackageName + "..",
                        "org.springframework.stereotype..",
                        "java..",
                        "fr.liksi.shared.."
                )
                .check(domainPackages);
    }
}
