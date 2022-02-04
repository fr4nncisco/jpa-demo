package demo.fariasv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.fariasv.model.Categoria;

//public interface CategoriasRepository extends CrudRepository<Categoria, Integer> {
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

}
