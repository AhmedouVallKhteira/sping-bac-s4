package com.ahmedou.bibliotheque.repository;

import com.ahmedou.bibliotheque.model.Achat;
import com.ahmedou.bibliotheque.model.StatutAchat;
import com.ahmedou.bibliotheque.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AchatRepository extends JpaRepository<Achat, Long> {

    List<Achat> findAllByOrderByDateDesc();

    List<Achat> findByClientOrderByDateDesc(Utilisateur client);

    List<Achat> findByAdminOrderByDateDesc(Utilisateur admin);

    List<Achat> findByStatusOrderByDateDesc(StatutAchat status);

    List<Achat> findByClientAndStatusOrderByDateDesc(Utilisateur client, StatutAchat status);

    List<Achat> findByAdminAndStatusOrderByDateDesc(Utilisateur admin, StatutAchat status);

    List<Achat> findByClientIdOrderByDateDesc(Long clientId);

    List<Achat> findByAdminIdOrStatusOrderByDateDesc(Long adminId, String status);

    List<Achat> findByAdminIdOrderByDateDesc(Long userId);

}
