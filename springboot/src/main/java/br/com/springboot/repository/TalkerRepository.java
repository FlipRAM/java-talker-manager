package br.com.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.models.Talker;

public interface TalkerRepository extends JpaRepository<Talker, Long> {
  @Query("SELECT t FROM Talker t WHERE t.name LIKE %:name%")
  public List<Talker> findByNameContaining(@Param("name") String name);
}
