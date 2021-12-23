package com.ead.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name= "TB_COURSES")
public class CourseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID courseId;
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(nullable = false, length = 250)
	private String description;
	
	@Column
	private String imageUrl;
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime creationDate;
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime lastUpdateDate;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseStatus courseStatus;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseLevel courseLevel;
	
	@Column(nullable = false)
	private UUID UserInstructor;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Com essa propriedade marcada com WRITE_ONLY, ao consultar esse model, essa Lista será ignorada, ela apenas vai 
														   //aparecer quando for uma mudança de status, como PUT e POST.
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)// esse Fetch Mode define como esse SET fará a busca no BD para trazer os dados de Modules. Caso seja select, vai fazer um select para 
							   //cada registro. Caso for JOIN, vai fazer inner join, e uma query só, e também vai ignorar o tipo fetch = FetchType.LAZY. Será sempre EAGER.
							   //caso for subselect, vai fazer os subselects de module dentro do select de course
							   //DEFAULT: JOIN
	//@OnDelete(action = OnDeleteAction.CASCADE) //delegando para o banco de dados a responsabilidade de remover -> banco vai gerar uma unica deleção, mas não vai aparecer no sistema o que de fato está sendo removido(Queries)
	private Set<ModuleModel> modules; //Hibernate lida melhor com grande volume de dados quando se utiliza set na lista, ao invés de list, 
									  //pois o Set não vai gerar duplicidade de dados e não é ordenado, diferente do List.
	
}
