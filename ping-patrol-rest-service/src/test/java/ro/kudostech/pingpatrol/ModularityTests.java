package ro.kudostech.pingpatrol;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/** Tests to verify the modular structure and generate documentation for the modules. */
class ModularityTests {
  ApplicationModules modules = ApplicationModules.of(Application.class);

  @Test
  void verifiesModularStructure() {
    modules.verify();
  }

  @Test
  void createModuleDocumentation() {
    ApplicationModules modules = ApplicationModules.of(Application.class);
    new Documenter(modules)
            .writeDocumentation()
            .writeModuleCanvases();
  }
}
