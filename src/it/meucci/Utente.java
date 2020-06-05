package it.meucci;

import java.util.ArrayList;

public class Utente 
{
	private int idUtente;
	private String email;
	private String password;
	private String nominativo;
	private String indirizzo;
	private String telefono;
	private int tipoUtente;
	private int idZona;
	private int oreFruite;
	private int oreErogate;
	private ArrayList<Categoria> categorie;


	public Utente(int idUtente, String email, String password, String nominativo, String indirizzo, String telefono, int tipoUtente, int idZona, ArrayList<Categoria> categorie) 
	{
		this.idUtente = idUtente;
		this.email = email;
		this.password = password;
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
		this.tipoUtente = tipoUtente;
		this.idZona = idZona;
		this.categorie = categorie;
	}


	public Utente(int idUtente, String email, String password, String nominativo, String indirizzo, String telefono, int tipoUtente, int idZona) 
	{
		this.idUtente = idUtente;
		this.email = email;
		this.password = password;
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
		this.tipoUtente = tipoUtente;
		this.idZona = idZona;
	}
	
	
	public Utente(String email, String password, String nominativo, String indirizzo, String telefono) 
	{
		this.email = email;
		this.password = password;
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
	}
	
	
	public Utente(String email, String password, String nominativo, String telefono) 
	{
		this.email = email;
		this.password = password;
		this.nominativo = nominativo;
		this.telefono = telefono;
	}
	
	
	public Utente(String nominativo, String telefono, int oreFruite, int oreErogate) 
	{
		this.nominativo = nominativo;
		this.telefono = telefono;
		this.oreFruite = oreFruite;
		this.oreErogate = oreErogate;
	}
	
	
	public Utente(int idUtente, String nominativo, String indirizzo, String telefono, String email) 
	{
		this.idUtente = idUtente;
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
		this.email = email;
	}
	
	
	public Utente(String nominativo, String indirizzo, String telefono, ArrayList<Categoria> categorie) 
	{
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
		this.categorie = categorie;
	}


	public int getIdUtente() 
	{
		return idUtente;
	}


	public void setIdUtente(int idUtente) 
	{
		this.idUtente = idUtente;
	}


	public String getEmail() 
	{
		return email;
	}


	public void setEmail(String email) 
	{
		this.email = email;
	}


	public String getPassword() 
	{
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getNominativo() {
		return nominativo;
	}


	public void setNominativo(String nominativo) 
	{
		this.nominativo = nominativo;
	}


	public String getIndirizzo() 
	{
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) 
	{
		this.indirizzo = indirizzo;
	}


	public String getTelefono()
	{
		return telefono;
	}


	public void setTelefono(String telefono) 
	{
		this.telefono = telefono;
	}


	public int getTipoUtente() 
	{
		return tipoUtente;
	}


	public void setTipoUtente(int tipoUtente) 
	{
		this.tipoUtente = tipoUtente;
	}


	public int getIdZona() 
	{
		return idZona;
	}


	public void setIdZona(int idZona) 
	{
		this.idZona = idZona;
	}
	

	public int getOreFruite() 
	{
		return oreFruite;
	}


	public void setOreFruite(int oreFruite) 
	{
		this.oreFruite = oreFruite;
	}


	public int getOreErogate() 
	{
		return oreErogate;
	}


	public void setOreErogate(int oreErogate) 
	{
		this.oreErogate = oreErogate;
	}


	public ArrayList<Categoria> getCategorie() 
	{
		return categorie;
	}


	public void setCategorie(ArrayList<Categoria> categorie) 
	{
		this.categorie = categorie;
	}


	@Override
	public String toString() 
	{
		return "Utente [idUtente=" + idUtente + ", email=" + email + ", password=" + password + ", nominativo=" + nominativo
				+ ", indirizzo=" + indirizzo + ", telefono=" + telefono + ", tipoUtente="
				+ tipoUtente + ", idZona=" + idZona + "]";
	}
}
