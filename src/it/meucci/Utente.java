package it.meucci;

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
	
	
	public Utente(int idUtente, String email, String password, String nominativo, String indirizzo, String telefono, int tipoUtente, int idZona) 
	{
		super();
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
		super();
		this.email = email;
		this.password = password;
		this.nominativo = nominativo;
		this.indirizzo = indirizzo;
		this.telefono = telefono;
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
	

	@Override
	public String toString() 
	{
		return "Utente [idUtente=" + idUtente + ", email=" + email + ", password=" + password + ", nominativo=" + nominativo
				+ ", indirizzo=" + indirizzo + ", telefono=" + telefono + ", tipoUtente="
				+ tipoUtente + ", idZona=" + idZona + "]";
	}
}
