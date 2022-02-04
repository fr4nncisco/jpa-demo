package demo.fariasv;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import demo.fariasv.model.Categoria;
import demo.fariasv.model.Perfil;
import demo.fariasv.model.Usuario;
import demo.fariasv.model.Vacante;
import demo.fariasv.repository.CategoriasRepository;
import demo.fariasv.repository.PerfilesRepository;
import demo.fariasv.repository.UsuariosRepository;
import demo.fariasv.repository.VacantesRepository;

@SpringBootApplication
public class JpaDemoApplication implements CommandLineRunner{

	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Autowired
	private VacantesRepository repoVacantes;
	
	@Autowired
	private UsuariosRepository repoUsuarios;
	
	@Autowired
	private PerfilesRepository repoPerfiles;
	
	public static void main(String[] args) {
		SpringApplication.run(JpaDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		buscarVacantesVariosEstatus();
	}
	
	/**
	 * Query Method: Buscar Vacantes por varios Estatus (In)
	 */
	private void buscarVacantesVariosEstatus() {
		String[] estatus = new String[] {"Eliminada", "Creada"};
		List<Vacante> lista = repoVacantes.findByEstatusIn(estatus);
		System.out.println("Registros encontrados: " + lista.size());
		
		for(Vacante v : lista) {
			System.out.println(v.getId() + " : " + v.getNombre() + " : " + v.getStatus());
		}
	}
	
	/**
	 * Query Method: Buscar Vacantes rango de Salario (Between)
	 */
	public void buscarVacantesSalario() {
		List<Vacante> lista = repoVacantes.findBySalarioBetweenOrderBySalarioDesc(7000, 14000);
		System.out.println("Registros encontrados: " + lista.size());
		
		for(Vacante v : lista) {
			System.out.println(v.getId() + " : " + v.getNombre() + " : $ " + v.getSalario());
		}
	}
	
	/**
	 * Query Method: Buscar Vacantes por Destacado y Estatus Ordenado por Id Desc
	 */
	public void buscarVacantesPorDestacadoEstatus() {
		List<Vacante> lista = repoVacantes.findByDestacadoAndEstatusOrderByIdDesc(1, "Aprobada");
		System.out.println("Registros encontrados: " + lista.size());
		
		for(Vacante v : lista) {
			System.out.println(v.getId() + " : " + v.getNombre() + " : " + v.getStatus() + " : " + v.getDestacado());
		}
	}
	
	public void buscarVacantesPorEstatus() {
		List<Vacante> lista = repoVacantes.findByEstatus("Aprobada");
		System.out.println("Registros encontrados: " + lista.size());
		
		for(Vacante v : lista) {
			System.out.println(v.getId() + " : " + v.getNombre() + " : " + v.getStatus());
		}
		
	}
	
	/**
	 * Método para buscar usuario y desplegar sus perfiles asociados
	 */
	public void buscarUsuario() {
		Optional<Usuario> optional = repoUsuarios.findById(1);
		if(optional.isPresent()) {
			Usuario u = optional.get();
			System.out.println("Usuario: " + u.getNombre());
			System.out.println("Perfiles asignados");
			
			for(Perfil p: u.getPerfiles()) {
				System.out.println(p.getPerfil());
			}
		} else {
			System.out.println("Usuario no encontrado");
		}
	}
	
	/**
	 * Crear un usuario con dos Perfiles ("ADMINISTRADOR", "USUARIO")
	 */
	private void crearUsuarioConDosPerfil() {
		Usuario user = new Usuario();
		user.setNombre("Francisco Arias");
		user.setEmail("fjariasvilela@gmail.com");
		user.setFechaRegistro(new Date());
		user.setUsername("FARIASV");
		user.setPassword("12345");
		user.setEstatus(1);
		
		Perfil p1 = new Perfil();
		p1.setId(2);
		
		Perfil p2 = new Perfil();
		p2.setId(3);
		
		user.agregar(p1);
		user.agregar(p2);
		
		repoUsuarios.save(user);
	}
	
	private void crearPerfilesAplicacion() {
		repoPerfiles.saveAll(getPerfilesAplicacion());
	}
	
	/**
	 * Guardar una vacante
	 */
	private void guardarVacante() {
		Vacante v = new Vacante();
		v.setNombre("Profesor de Matemáticas");
		v.setDescripcion("Las características para el puesto");
		v.setFecha(new Date());
		v.setSalario(5000.0);
		v.setStatus("Aprobada");
		v.setDestacado(0);
		v.setImagen("escuela.png");
		v.setDetalles("<h1>Los requisitos para Profesor de Matematicas</h1>");
		Categoria c = new Categoria();
		c.setId(15);
		v.setCategoria(c);
		
		repoVacantes.save(v);
	}
	
	private void buscarVacantes() {
		List<Vacante> lista = repoVacantes.findAll();
		for(Vacante tmp : lista) {
			System.out.println(tmp.getId() + " " + tmp.getNombre() + " -> " + tmp.getCategoria().getNombre());
		}
	}
	
	/**
	 * Método findAll [Con Paginación y Ordenados]- Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosPaginacionOrdenados() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5, Sort.by("nombre")));
		
		System.out.println("Total registros: " + page.getTotalElements());
		System.out.println("Total páginas: " + page.getTotalPages());
		
		for(Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	/**
	 * Método findAll [Con Paginación]- Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosPaginacion() {
		Page<Categoria> page = repoCategorias.findAll(PageRequest.of(0, 5));
		
		System.out.println("Total registros: " + page.getTotalElements());
		System.out.println("Total páginas: " + page.getTotalPages());
		
		for(Categoria c : page.getContent()) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	/**
	 * Método findAll [Ordenados por un campo]- Interfaz PagingAndSortingRepository
	 */
	private void buscarTodosOrdenados() {
		List<Categoria> cat = repoCategorias.findAll(Sort.by("nombre").descending());
		for(Categoria c : cat) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	
	/**
	 * Método deleteAllInBatch - Interfaz JpaRepository
	 */
	private void borrarTodoEnBloque() {
		repoCategorias.deleteAllInBatch();
	}
	
	/**
	 * Método findAll - Interfaz JpaRepository
	 */
	private void buscarTodosJpa() {
		List<Categoria> cat = repoCategorias.findAll();
		for(Categoria c : cat) {
			System.out.println(c.getId() + " " + c.getNombre());
		}
	}
	
	/**
	 * Método saveAll - Interfaz CrudRepository
	 */
	private void guardarTodas() {
		List<Categoria> categorias = getListaCategorias();
		repoCategorias.saveAll(categorias);
	}
	
	/**
	 * Método existsById - Interfaz CrudRepository 
	 */
	private void existeId() {
		boolean existe = repoCategorias.existsById(5);
		System.out.println("¿La categoria existe? :" + existe);
		
	}
	
	/**
	 * Método findAll - Interfaz CrudRepository
	 */
	private void buscarTodos() {
		Iterable<Categoria> categorias = repoCategorias.findAll();
		
		for(Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	/**
	 * Método findAllById - Interfaz CrudRepository
	 */
	private void encontrarPorIds() {
		List<Integer> ids = new LinkedList<Integer>();
		ids.add(1);
		ids.add(4);
		ids.add(10);
		
		Iterable<Categoria> categorias = repoCategorias.findAllById(ids);
		
		for(Categoria cat : categorias) {
			System.out.println(cat);
		}
	}
	
	/**
	 *  Método deleteAll - Interfaz CrudRepository
	 */
	private void eliminarTodos() {
		repoCategorias.deleteAll();
	}
	
	/**
	 * Método count - Interfaz CrudRepository
	 */
	private void conteo() {
		long count = repoCategorias.count();
		System.out.println("Total categorias:" + count);
	}
	
	/**
	 * Método deleteById - Interfaz CrudRepository
	 */
	public void eliminar() {
		int idCat = 1;
		repoCategorias.deleteById(idCat);
	}
	
	private void modificar() {
		Optional<Categoria> optional = repoCategorias.findById(2);
		if(optional.isPresent()) {
			Categoria catTmp = optional.get();
			catTmp.setNombre("INGENIERIA DE SOFTWARE");
			catTmp.setDescripcion("Desarrollo de sistemas");
			repoCategorias.save(catTmp);
			System.out.println(optional.get());
		} else {
			System.out.println("Categoría no encontrada");
		}
	}
	
	/**
	 * Método findById - Interfaz CrudRepository
	 */
	public void buscarPorID() {
		Optional<Categoria> optional = repoCategorias.findById(5);
		if(optional.isPresent()) {
			System.out.println(optional.get());
		} else {
			System.out.println("Categoría no encontrada");
		}
	}
	
	/**
	 * Método save - Interfaz CrudRepository
	 */
	private void guardar() {
		Categoria cat = new Categoria();
		cat.setNombre("FINANZAS");
		cat.setDescripcion("Trabajos relacionados con Finanzas y Contabilidas");
		
		repoCategorias.save(cat);
		System.out.println(cat);
	}
	
	private List<Categoria> getListaCategorias(){
		
		List<Categoria> list = new LinkedList<Categoria>();
		
		Categoria c1 = new Categoria();
		c1.setNombre("Programador de BlockChain");
		c1.setDescripcion("Trabajos relacionados con Bitcoin y Criptomonedas");
		
		Categoria c2 = new Categoria();
		c2.setNombre("Soldador/Pintura");
		c2.setDescripcion("Trabajos relacionados con soldadura, pintura y enderezado");
		
		Categoria c3 = new Categoria();
		c3.setNombre("Ing. Industrial");
		c3.setDescripcion("Trabajos relacionados con Ing. Industrial");
		
		list.add(c1);
		list.add(c2);
		list.add(c3);
		
		return list;
	}
	
	private List<Perfil> getPerfilesAplicacion(){
		List<Perfil> lista = new LinkedList<Perfil>();
		Perfil p1 = new Perfil();
		p1.setPerfil("SUPERVISOR");
		
		Perfil p2 = new Perfil();
		p2.setPerfil("ADMINISTRADOR");
		
		Perfil p3 = new Perfil();
		p3.setPerfil("USUARIO");
		
		lista.add(p1);
		lista.add(p2);
		lista.add(p3);
		
		return lista;
	}

}
