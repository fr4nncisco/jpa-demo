package demo.fariasv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.fariasv.model.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

}
