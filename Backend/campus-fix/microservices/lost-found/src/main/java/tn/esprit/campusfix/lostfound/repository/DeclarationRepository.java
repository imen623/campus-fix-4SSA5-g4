package tn.esprit.campusfix.lostfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tn.esprit.campusfix.lostfound.entity.Declaration;

import java.util.List;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long>, JpaSpecificationExecutor<Declaration> {

    List<Declaration> findByUserId(Long userId);
}
