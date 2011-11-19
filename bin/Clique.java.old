import java.util.LinkedList;


public class Clique 
{
	protected LinkedList<Processus> listeProcessus = new LinkedList<Processus>();
	
	public Clique(LinkedList<Processus> liste)
	{
		this.listeProcessus = liste;
	}
	public Clique(Clique c)
	{
		this.listeProcessus = new LinkedList<Processus>();
		for(Processus p: c.getListeProcessus())
		{
			this.listeProcessus.add(p);
		}
	}
	
	public String toString()
	{
		String res = "";
		for(Processus p: listeProcessus)
		{
			res += ", " + p.toString();
		}
		res = res.substring(2);
		return res;
	}

	/**
	 * @return the listeProcessus
	 */
	public LinkedList<Processus> getListeProcessus() {
		return listeProcessus;
	}

	/**
	 * @param listeProcessus the listeProcessus to set
	 */
	public void setListeProcessus(LinkedList<Processus> listeProcessus) {
		this.listeProcessus = listeProcessus;
	}
	
	public void addProcessus(Processus p)
	{
		listeProcessus.add(p);
	}
}
