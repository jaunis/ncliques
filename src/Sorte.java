import java.util.LinkedList;


public class Sorte 
{
	protected LinkedList<Processus> listeProcessus = new LinkedList<Processus>();
	protected Graphe graphe;
	protected String nom;
	
	public Sorte(String nom, Graphe graphe, int taille)
	{
		this.nom = nom;
		this.graphe = graphe;
		for(int i=0; i<taille; i++)
		{
			listeProcessus.add(new Processus(this, i));
		}
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
	/**
	 * @return the graphe
	 */
	public Graphe getGraphe() {
		return graphe;
	}
	/**
	 * @param graphe the graphe to set
	 */
	public void setGraphe(Graphe graphe) {
		this.graphe = graphe;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String toString()
	{
		String res = nom + " { ";
		for(Processus p: listeProcessus)
		{
			res += p.toString() + ", ";
		}
		res = res.substring(0, res.length()-2);
		res += "}";
		return res;
	}
	
	/**
	 * supprime les processus qui ne sont pas liés à toutes les autres sortes
	 * @return
	 */
	public boolean nettoyerSorte()
	{
		boolean res = false;
		Processus[] tableau = new Processus[listeProcessus.size()];
		int i=0;
		/*
		 * on supprime les processus inutiles à l'aide de 2 boucles for, pour éviter
		 * de lever une ConcurrentModificationException
		 */
		for(Processus p: listeProcessus)
		{
			LinkedList<Processus> listeAssoc = p.getListeAssociations();
			if(!(p.estValide()))
			{
				for(Processus assoc: listeAssoc)
				{
					assoc.getListeAssociations().remove(p);
				}
				tableau[i] = p;
				res = true;
			}
		}
		for(Processus p: tableau)
		{
			if(p != null) listeProcessus.remove(p);
		}
		return res;
	}
	
	/**
	 * 
	 * @param num le numéro du processus à renvoyer
	 * @return
	 */
	public Processus getProcessusByNumero(int num)
	{
		try{return listeProcessus.get(num);}
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("Nom: " + this.nom + "\n" + "Numéro essayé: " + num);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * renvoie la somme du nombre d'associations de chaque processus
	 * @return
	 */
	public int getTotalAssociations() 
	{
		int somme = 0;
		for(Processus p: listeProcessus)
		{
			somme += p.getListeAssociations().size();
		}
		return somme;
	}
}
