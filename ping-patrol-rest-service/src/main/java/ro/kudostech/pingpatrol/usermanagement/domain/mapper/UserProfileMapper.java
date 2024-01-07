package ro.kudostech.pingpatrol.usermanagement.domain.mapper;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.kudostech.pingpatrol.api.server.model.UserProfile;
import ro.kudostech.pingpatrol.common.CommonMapper;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model.UserProfileDbo;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {CommonMapper.class})
public interface UserProfileMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUUID")
    UserProfile toUserProfile(UserProfileDbo candidateDbo);

    @Mapping(source = "id", target = "id", qualifiedByName = "UUIDToString")
    UserProfileDbo toUserProfileDbo(UserProfile userDetails);
    
}
