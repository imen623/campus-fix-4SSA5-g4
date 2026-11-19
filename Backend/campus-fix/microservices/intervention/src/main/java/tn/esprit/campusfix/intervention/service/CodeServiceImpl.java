package tn.esprit.campusfix.intervention.service;

import tn.esprit.campusfix.intervention.domain.*;
import tn.esprit.campusfix.intervention.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeRepository codeRepository;
    private final ElementInspectionRepository elementInspectionRepository;

    @Override
    public List<Code> retrieveAllCodes() {
        return codeRepository.findAll();
    }

    @Override
    public Code retrieveCode(Long id) {
        return codeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code introuvable"));
    }

    @Override
    @Transactional
    public Code addCode(Code valeur) {
        valeur.setId(null);
        valeur.setDateCreation(LocalDateTime.now());
        valeur.setDateMiseAJour(LocalDateTime.now());

        return codeRepository.save(valeur);
    }

    @Override
    @Transactional
    public Code modifyCode(Long id, Code valeur) {
        Code existant = retrieveCode(id);
        existant.setDateMiseAJour(LocalDateTime.now());
        existant.setValeur(valeur.getValeur());
        existant.setDescription(valeur.getDescription());
        return codeRepository.save(existant);
    }

    @Override
    @Transactional
    public void removeCode(Long id) {
        retrieveCode(id);

        if (elementInspectionRepository.existsByCode_Id(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Code utilise dans une inspection");
        }

        codeRepository.deleteById(id);
    }


}
