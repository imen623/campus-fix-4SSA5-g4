package tn.esprit.campusfix.intervention.service;
import tn.esprit.campusfix.intervention.domain.Code;

import java.util.List;

public interface CodeService {
    List<Code> retrieveAllCodes();
    Code retrieveCode(Long id);
    Code addCode(Code valeur);
    Code modifyCode(Long id, Code valeur);
    void removeCode(Long id);

}
