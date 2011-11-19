import java.util.LinkedList;


public class ArbreCliques 
{
	//valeur du noeud
	protected Processus valeur = null;
	//liste des fils
	protected LinkedList<ArbreCliques> listeFils = new LinkedList<ArbreCliques>();
	//hauteur: mis à jour uniquement pour la racine de l'arbre
	protected int hauteur = 0;
	
	public ArbreCliques(Processus p) {
		this.valeur = p;
	}
	
	public ArbreCliques()
	{
		
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
	 * fonction récursive appelée par toString()
	 * @param chaine
	 * @return
	 */
	protected String toString(String chaine)
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
			for(ArbreCliques a: listeFils)
			{
				res += a.toString(local);
			}
			return res;
		}
	}
	
	/**
	 * renvoie la liste des cliques (1 clique par ligne)
	 */
	public String toString()
	{
		return toString("");
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
	 * fonction récursive calculant la profondeur maximum de l'arbre
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
	
	/**
	 * appelle addProcessus(Processus, int) pour insérer le processus au bon endroit dans l'arbre
	 * @param p
	 */
	public void addProcessus(Processus p)
	{
		if(hauteur > 0)
		{
			for(ArbreCliques a: listeFils)
			{
				a.addProcessus(p, hauteur - 1);
			}
		}
		else listeFils.add(new ArbreCliques(p));
	}
	
	/**
	 * fonction récursive ajoutant un processus dans l'arbre à une profondeur donnée,<br/>
	 * en vérifiant que le processus possède une association avec chaque noeud de la branche
	 * @param p
	 * @param profondeur
	 */
	protected void addProcessus(Processus p, int profondeur)
	{
		if(p.getListeAssociations().contains(valeur))
		{
			if(profondeur == 0) listeFils.add(new ArbreCliques(p));
			else
			{
				for(ArbreCliques a: listeFils)
				{
					a.addProcessus(p, profondeur - 1);
				}
			}
		}
	}
	
	/**
	 * supprime les branches de profondeur inférieure à la valeur passée en paramètre
	 * @param profondeur
	 */
	protected void nettoyer(int profondeur)
	{
		if(!estFeuille())
		{
			ArbreCliques[] listeSuppression = new ArbreCliques[listeFils.size()];
			int i=0;
			for(ArbreCliques a: listeFils)
			{
				if(a.getProfondeurMax()<profondeur)
				{
					listeSuppression[i] = a;
					i++;
				}
				else a.nettoyer(profondeur - 1);
			}
			int j=0;
			for(j=0; j<i; j++)
			{
				listeFils.remove(listeSuppression[j]);
			}
		}
		
	}
	
	/**
	 * appelle nettoyer(int)
	 */
	public void nettoyer()
	{
		nettoyer(hauteur);
	}
}
