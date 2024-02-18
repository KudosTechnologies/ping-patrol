package ro.kudostech.pingpatrol.modules.monitor;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.kudostech.pingpatrol.api.server.model.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.server.model.PatchOperation;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;
import ro.kudostech.pingpatrol.modules.monitor.domain.service.MonitorPatchService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MonitorPatchServiceTest {

  @Test
  void whenPatchOperationSupported_thenApplyPatchSuccessfully() {
    MonitorPatchService service = new MonitorPatchService();
    MonitorDbo monitorDbo = new MonitorDbo();
    monitorDbo.setName("Original Name");

    MonitorPatchOperation patchOperation = new MonitorPatchOperation();
    patchOperation.setOp(PatchOperation.REPLACE);
    patchOperation.setPath(MonitorPatchOperation.PathEnum.NAME);
    patchOperation.setValue("New Name");

    Set<ConstraintViolation<?>> violations = service.patch(monitorDbo, List.of(patchOperation));

    assertThat(violations).isEmpty();
    assertThat(monitorDbo.getName()).isEqualTo("New Name");
  }

    @Test
    void whenPatchOperationWithInvalidValue_thenGenerateConstraintViolation() {
      MonitorPatchService service = new MonitorPatchService();
      MonitorDbo monitorDbo = new MonitorDbo();

      MonitorPatchOperation patchOperation = new MonitorPatchOperation();
      patchOperation.setOp(PatchOperation.REPLACE);
      patchOperation.setPath(
          MonitorPatchOperation.PathEnum.TYPE); // Assume URL patching is not supported
      patchOperation.setValue("unknown type");

      Set<ConstraintViolation<?>> violations = service.patch(monitorDbo, List.of(patchOperation));

      assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("must be a valid MonitorType.");
    }

  @Test
  void whenPatchOperationUnsupported_thenGenerateConstraintViolation() {
    MonitorPatchService service = new MonitorPatchService();
    MonitorDbo monitorDbo = new MonitorDbo();

    MonitorPatchOperation patchOperation = new MonitorPatchOperation();
    patchOperation.setOp(PatchOperation.REPLACE);
    patchOperation.setPath(
        MonitorPatchOperation.PathEnum.MONITORTIMEOUT); // Assume URL patching is not supported
    patchOperation.setValue(0);

    Set<ConstraintViolation<?>> violations = service.patch(monitorDbo, List.of(patchOperation));

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getMessage())
        .isEqualTo("REPLACE-ing the /monitorTimeout is not possible.");
  }
}
