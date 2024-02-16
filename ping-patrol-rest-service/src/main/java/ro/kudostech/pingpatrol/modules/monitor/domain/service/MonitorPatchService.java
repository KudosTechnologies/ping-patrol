package ro.kudostech.pingpatrol.modules.monitor.domain.service;

import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.kudostech.pingpatrol.api.server.model.MonitorPatchOperation;
import ro.kudostech.pingpatrol.api.server.model.MonitorType;
import ro.kudostech.pingpatrol.api.server.model.PatchOperation;
import ro.kudostech.pingpatrol.common.exception.ConstraintViolationHelper;
import ro.kudostech.pingpatrol.common.exception.IllegalPatchArgumentException;
import ro.kudostech.pingpatrol.modules.monitor.adapter.out.persistence.MonitorDbo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static ro.kudostech.pingpatrol.api.server.model.MonitorPatchOperation.PathEnum.TYPE;

@Slf4j
@Service
public class MonitorPatchService {
  private final Map<
          PatchOperation, Map<MonitorPatchOperation.PathEnum, BiConsumer<MonitorDbo, Object>>>
      patchMap =
          Map.of(
              PatchOperation.REPLACE,
              Map.ofEntries(
                  entry(TYPE, this::setType),
                  entry(
                      MonitorPatchOperation.PathEnum.NAME,
                      (MonitorDbo m, Object o) -> m.setName(o.toString()))));

  public Set<ConstraintViolation<?>> patch(
      MonitorDbo monitorDbo, List<MonitorPatchOperation> patchOperations) {
    return patchOperations.stream()
        .map(patchOperation -> doPatch(monitorDbo, patchOperation))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  private Optional<ConstraintViolation<?>> doPatch(
      MonitorDbo monitorDbo, MonitorPatchOperation patchOperation) {

    final BiConsumer<MonitorDbo, Object> action =
        patchMap.get(patchOperation.getOp()).get(patchOperation.getPath());
    if (action == null) {
      String message =
          String.format(
              "%sing the %s is not possible.",
              patchOperation.getOp().getValue(), patchOperation.getPath().getValue());
      log.info(message);
      return Optional.of(
          ConstraintViolationHelper.buildConstraintViolation(
              message, MonitorDbo.class, patchOperation.getPath().getValue().toString()));
    }
    try {
      action.accept(monitorDbo, patchOperation.getValue());
    } catch (IllegalPatchArgumentException e) {
      return Optional.of(
          ConstraintViolationHelper.buildConstraintViolation(
              "must be a valid " + e.getTarget(),
              e.getTarget(),
              patchOperation.getPath().getValue().toString()));
    }
    log.info(String.format("%s is done", patchOperation.getPath().getValue()));
    return Optional.empty();
  }

  private void setType(MonitorDbo monitorDbo, Object o) {
    var monitorType = MonitorType.fromValue(o.toString());
    monitorDbo.setType(monitorType.name());
  }
}
