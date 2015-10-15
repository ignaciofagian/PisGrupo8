package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RespuestaGeneral")
@DiscriminatorValue(value="General")
public class RespuestaGeneral extends Respuesta {
	
	private static final long serialVersionUID = 1L;
	
	@OneToMany
	@JoinTable(joinColumns={@JoinColumn(name="IDResp", referencedColumnName="IDResp")},
	inverseJoinColumns={@JoinColumn(name="IDPaqCategoria", referencedColumnName="IDPaqCategoria")})

	private List<PaqueteCategoria> categorias;
	
	public RespuestaGeneral()
	{
		super("General");
	}

	public List<PaqueteCategoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<PaqueteCategoria> categorias) {
		this.categorias = categorias;
	}
	
	public RespuestaGeneral(String eng,String espa,Pregunta question, PaqueteCategoria... categorias) {
		super("General");
		this.setSentenciaESP(espa);
		this.setSentenciaENG(eng);
		this.addCategorias(categorias); 
		this.setPregunta(question);
	}
	
	public void addCategorias(PaqueteCategoria... cat){
		if (this.categorias == null) this.categorias = new ArrayList<PaqueteCategoria>();
		this.categorias.addAll(Arrays.asList(cat));
	}
}
