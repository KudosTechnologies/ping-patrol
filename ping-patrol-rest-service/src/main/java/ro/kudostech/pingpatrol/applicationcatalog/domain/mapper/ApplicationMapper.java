package ro.kudostech.pingpatrol.applicationcatalog.domain.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.kudostech.pingpatrol.api.server.model.Application;
import ro.kudostech.pingpatrol.applicationcatalog.adapters.out.persistence.model.ApplicationDbo;
import ro.kudostech.pingpatrol.common.CommonMapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CommonMapper.class})
public interface ApplicationMapper {
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUUID")
    Application toApplication(ApplicationDbo applicationDbo);

    @Mapping(source = "id", target = "id", qualifiedByName = "UUIDToString")
    ApplicationDbo toApplicationDbo(Application application);
}
