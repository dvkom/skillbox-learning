package utils;

import entity.CreditRequest;
import entity.CreditRequestView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreditRequestMapper {

  CreditRequestMapper INSTANCE = Mappers.getMapper(CreditRequestMapper.class);

  @Mapping(source = "isSalaryProject", target = "isSalaryProject")
  @Mapping(source = "isTrustedClient", target = "isTrustedClient")
  @Mapping(source = "isRetiree", target = "isRetiree")
  @Mapping(source = "isPrivacyAccepted", target = "isPrivacyAccepted")
  CreditRequestView getView(CreditRequest creditRequest);

  @Mapping(source = "isSalaryProject", target = "isSalaryProject")
  @Mapping(source = "isTrustedClient", target = "isTrustedClient")
  @Mapping(source = "isRetiree", target = "isRetiree")
  @Mapping(source = "isPrivacyAccepted", target = "isPrivacyAccepted")
  CreditRequest getRequest(CreditRequestView creditRequestView);
}
