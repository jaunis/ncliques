import java.util.Hashtable;
import java.util.LinkedList;


public class ArbreCliques 
{
	//valeur du noeud
	protected Processus valeur = null;
	//liste des fils
	protected LinkedList<ArbreCliques> listeFils = new LinkedList<>();
	// liste des pères
	protected LinkedList<ArbreCliques> listePeres = new LinkedList<>();
	//hauteur: mis à jour uniquement pour la racine de l'arbre
	protected int hauteur = 0;
	//noeuds de l'arbre avec lesquels le noeud courant n'a pas de lien
	protected LinkedList<ArbreCliques> listeBloquants = new LinkedList<>();
	
	public ArbreCliques(Processus p) {
		this.valeur = p;
	}
	
	public ArbreCliques()
	{}
	
	public ArbreCliques dupliquer()
	{
		ArbreCliques n = new ArbreCliques(valeur);
		Hashtable<ArbreCliques, ArbreCliques> table = new Hashtable<>();
		table.put(this, n);
		if(!estFeuille()) dupliquer(listeFils, table);
		n.setListePeres(new LinkedList<ArbreCliques>(listePeres));
		return n;
	}
	
	private void dupliquer(LinkedList<ArbreCliques> niveauActuel,
							Hashtable<ArbreCliques, ArbreCliques> table)
	{
		LinkedList<ArbreCliques> niveauSuivant = new LinkedList<>();
		for(ArbreCliques a: niveauActuel)
		{
			ArbreCliques f = new ArbreCliques(a.getValeur());
			table.put(a, f);
			for(ArbreCliques p: a.getListePeres())
			{
				ArbreCliques p2 = table.get(p);
				if(p2 == null)
				{
					f.getListePeres().add(p);
					p.getListeFils().add(f);
				}
				else
				{
					f.getListePeres().add(p2);
					p2.getListeFils().add(f);
				}
			}
			for(ArbreCliques fils: a.getListeFils())
			{
				if(!niveauSuivant.contains(fils)) niveauSuivant.add(f);
			}
		}
		
		if(!niveauSuivant.isEmpty()) dupliquer(niveauSuivant, table);
	}

	/**
	 * @return the listePeres
	 */
	public LinkedList<ArbreCliques> getListePeres() {
		return listePeres;
	}

	/**
	 * @param listePeres the listePeres to set
	 */
	public void setListePeres(LinkedList<ArbreCliques> listePeres) {
		this.listePeres = listePeres;
	}

	/**
	 * @return the hauteur
	 */
	public int getHauteur() {
		return hauteur;
	}
	/**
	 * @param hauteur the hauteur to set
	 */
	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}
	/**
	 * @return the valeur
	 */
	public Processus getValeur() {
		return valeur;
	}
	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(Processus valeur) {
		this.valeur = valeur;
	}
	/**
	 * @return the listeFils
	 */
	public LinkedList<ArbreCliques> getListeFils() {
		return listeFils;
	}
	/**
	 * @param listeFils the listeFils to set
	 */
	public void setListeFils(LinkedList<ArbreCliques> listeFils) {
		this.listeFils = listeFils;
	}
	
	/**
	 * @return the listeBloquants
	 */
	public LinkedList<ArbreCliques> getListeBloquants() {
		return listeBloquants;
	}

	/**
	 * @param listeBloquants the listeBloquants to set
	 */
	public void setListeBloquants(LinkedList<ArbreCliques> listeBloquants) {
		this.listeBloquants = listeBloquants;
	}	
	
	/**
	 * fonction récursive appelée par toString();
	 * on n'affiche pas tout l'arbre, uniquement les cliques "valides"
	 * @param chaine
	 * @return
	 */
	protected String toString(String chaine, LinkedList<ArbreCliques> listeInterdits)
	{
		if(listeInterdits.contains(this))
		{
			return "";
		}
		else
		{
			if(estFeuille())
			{
				String res = chaine + ", " + valeur + "\n";
				return res.substring(2);
			}
			else
			{
				String local;
				if(valeur == null) local = chaine;
				else local = chaine + ", " + valeur;
				String res = "";
				listeInterdits.addAll(listeBloquants);
				for(ArbreCliques a: listeFils)
				{
					res += a.toString(local, new LinkedList<>(listeInterdits));
				}
				return res;
			}
		}
		
	}
	
	/**
	 * renvoie la liste des cliques (1 clique par ligne)
	 */
	public String toString()
	{
		return toString("", new LinkedList<ArbreCliques>());
	}
	
	/**
	 * teste si l'élément est une feuille
	 * @return
	 */
	public boolean estFeuille()
	{
		return listeFils.isEmpty();
	}
	
	/**
	 * fonction r�cursive calculant la profondeur maximum de l'arbre
	 * @return
	 */
	public int getProfondeurMax()
	{
		if(valeur==null && estFeuille()) return 0;
		else if(estFeuille()) return 1;
		else
		{
			int ajout = valeur == null? 0 : 1;
			int max = 0;
			int local;
			for(ArbreCliques a: listeFils)
			{
				local = a.getProfondeurMax();
				if(local>max) max = local;
			}
			return max + ajout;
		}
	}
	
	public int getProfondeurMin()
	{
		if(valeur==null && estFeuille()) return 0;
		else if(estFeuille()) return 1;
		else
		{
			int ajout = valeur == null? 0 : 1;
			int min = 10000;
			int local;
			for(ArbreCliques a: listeFils)
			{
				local = a.getProfondeurMin();
				if(local<min) min = local;
			}
			return min + ajout;
		}
	}
	/**
	 * appelle addProcessus(Processus, int) pour insérer le processus au bon endroit dans l'arbre
	 * @param p
	 */
	public void addProcessus(Processus p)
	{
		ArbreCliques nouveau = new ArbreCliques(p);
		if(hauteur > 0)
		{
			for(ArbreCliques a: listeFils)
			{
				a.addProcessus(nouveau, hauteur - 1, new LinkedList<ArbreCliques>());
			}
			
		}
		else listeFils.add(nouveau);
	}
	
	/**
	 * fonction récursive ajoutant un processus dans l'arbre à une profondeur donnée,<br/>
	 * en vérifiant que le processus possède une association avec chaque noeud de la branche
	 * @param p
	 * @param profondeur
	 */
	protected void addProcessus(ArbreCliques nouveau, 
								int profondeur,
								LinkedList<ArbreCliques> listeInterdits
								)
	{
		if(valeur.accepte(nouveau.getValeur()))
		{
			
			if(!listeInterdits.contains(this))
			{
				if(profondeur == 0)
				{
					if(!listeFils.contains(nouveau)) 
						listeFils.add(nouveau);
					if(!nouveau.getListePeres().contains(this)) 
						nouveau.getListePeres().add(this);
				}
				else
				{
					listeInterdits.addAll(listeBloquants);
					for(ArbreCliques a: listeFils)
					{
						a.addProcessus(nouveau, profondeur - 1, new LinkedList<>(listeInterdits));
					}
				}
			}
			
			
		}
		else
		{
			listeBloquants.add(nouveau);
		}
	}

	/**
	 * @param hauteur2
	 */
	public void nettoyer(int hauteur2) 
	{
		if(!estFeuille())
		{
			LinkedList<ArbreCliques> listeSuppression = new LinkedList<>();
			for(ArbreCliques a: listeFils)
			{
				if(a.getProfondeurMax() < hauteur2 - 1) listeSuppression.add(a);
			}
			for(ArbreCliques a: listeSuppression)
			{
				listeFils.remove(a);
			}
		}
	}
}
