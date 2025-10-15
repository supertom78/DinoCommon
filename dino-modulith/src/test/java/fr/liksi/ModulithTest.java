package fr.liksi;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModulithTest {

    public static final ApplicationModules modules =
            ApplicationModules.of(DinoApp.class);

    @Test
    void verifyModularity() {
        modules.verify();
    }

    @Test
    void generateDocumentation() {
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }

    @Test
    void renderAsciidoc(){
        var canvasOptions = Documenter.CanvasOptions.defaults();

        var docOptions = Documenter.DiagramOptions.defaults()
                .withStyle(Documenter.DiagramOptions.DiagramStyle.UML);

        new Documenter(modules) //
                .writeDocumentation(docOptions, canvasOptions);
    }
}
