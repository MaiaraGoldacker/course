package com.ead.course.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ead.course.models.ModuleModel;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

	@EntityGraph(attributePaths = {"course"}) //atributo anotado com LAZY dentro de Module, não vai carregar quando for chamado, mas para 'alterar' isso em tempo de execução, 
	ModuleModel findByTitle(String title);	  //podemos utilizar o @EntityGraph que vai carregar o então objeto quando chamado. Como se só nesse determinado momento ele  'se tornasse' EAGER para 
											  //essa única consulta

	@Query(value="select * from tb_modules where course_course_id = :courseId", nativeQuery = true)
	List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

	@Query(value="select * from tb_modules where course_course_id = :courseId "
			+ "and module_id = :moduleId", nativeQuery = true)
	Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, 
											   @Param("moduleId") UUID moduleId);
}
