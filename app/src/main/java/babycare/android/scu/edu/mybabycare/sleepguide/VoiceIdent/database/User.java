package babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.database;

import java.io.Serializable;
import java.util.ArrayList;

import babycare.android.scu.edu.mybabycare.sleepguide.VoiceIdent.mcm.smc.math.vq.Codebook;


public class User implements Serializable
{
	private int id = -1;
	private String username = "";//user names must be unique
	private String password = null;
	private String firstnames = "";
	private String surname = "";
	private ArrayList<Codebook> codeBook;
	private Boolean adminRights = false;
	
	public User(String firstnames, String surname, String username, String password, Boolean adminRights, int pk, ArrayList<Codebook> cb)
	{
		this.username = username;
		if(password != null)
		this.password = "" +password.hashCode();
		this.firstnames = firstnames;
		this.surname = surname;
		this.adminRights = adminRights;
		this.id = pk;
		
		if(cb != null)
			this.codeBook = new ArrayList<Codebook>(cb);
	}
	
	//------------------------------------------------------------------------getCodeBook
	public ArrayList<Codebook> getCodeBook()
	{
		ArrayList<Codebook> result = null;
		
		if(codeBook != null)
			result = new ArrayList<Codebook>(codeBook);
		
		return result;
	}
	
	//------------------------------------------------------------------------setCodeBook
	public void setCodeBook(ArrayList<Codebook> cb)
	{
		this.codeBook = new ArrayList<Codebook>(cb);
	}
	
	//------------------------------------------------------------------------getID
	public int getID()
	{
		return id;
	}
	
	//------------------------------------------------------------------------setID
	public void setID(int value)
	{
		id = value;
	}
	
	//------------------------------------------------------------------------getUsername
	public String getUsername()
	{
		return username;
	}

	//------------------------------------------------------------------------setUsername
	public void setUsername(String username)
	{
		this.username = username;
	}

	//------------------------------------------------------------------------getPassword
	public String getPassword()
	{
		return password;
	}

	//------------------------------------------------------------------------setPassword
	public void setPassword(String password)
	{
		if(password != null)
			this.password = "" +password.hashCode();
	}

	//------------------------------------------------------------------------getFirstnames
	public String getFirstnames()
	{
		return firstnames;
	}

	//------------------------------------------------------------------------setFirstnames
	public void setFirstnames(String firstnames)
	{
		this.firstnames = firstnames;
	}

	//------------------------------------------------------------------------getSurname
	public String getSurname()
	{
		return surname;
	}

	//------------------------------------------------------------------------setSurname
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	//------------------------------------------------------------------------getAdminRights
	public Boolean getAdminRights()
	{
		return this.adminRights;
	}
	
	//-------------------------------------------------------------------------setAdminRights
	public void setAdminRights(Boolean adminRights)
	{
		this.adminRights = adminRights;
	}
}
