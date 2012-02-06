import java.util.ConcurrentModificationException;
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
	//protected LinkedList<ArbreCliques> listeBloquants = new LinkedList<>();
	
	//lors de la remontée, indique par quel fils on est arrivé
	protected ArbreCliques filsPrecedent;
	
	/**
	 * @return the filsPrecedent
	 */
	public ArbreCliques getFilsPrecedent() {
		return filsPrecedent;
	}

	/**
	 * @param filsPrecedent the filsPrecedent to set
	 */
	public void setFilsPrecedent(ArbreCliques filsPrecedent) {
		this.filsPrecedent = filsPrecedent;
	}


	public ArbreCliques(Processus p) {
		this.valeur = p;
	}
	
	public ArbreCliques()
	{}
	
	/**
	 * duplique l'arbre courant (copie récursive des fils, copie <b>uniquement des références</b>
	 * des pères
	 * @return une copie de l'arbre courant
	 */
	public ArbreCliques dupliquer()
	{
		//System.out.println("appel de dupliquer");
		ArbreCliques n = new ArbreCliques(valeur);
		
		//la table de hachage permet d'associer les anciens noeuds aux nouveaux (ceux qui
		//seront contenus dans la copie
		Hashtable<ArbreCliques, ArbreCliques> table = new Hashtable<>();
		table.put(this, n);
		//dupliquer(List, Hashtable) fonctionne par effet de bord
		if(!estFeuille()) dupliquer(listeFils, table);
		n.setListePeres(new LinkedList<ArbreCliques>(listePeres));
		return n;
	}
	
	/**
	 * méthode récursive fonctionnant par effet de bord</br>
	 * L'arbre est parcouru en largeur d'abord. Le niveau de noeuds courant est contenu dans
	 * niveauActuel.
	 * @param niveauActuel liste d'arbres à dupliquer
	 * @param table table de hachage associant les anciens noeuds aux nouveaux
	 */
	protected void dupliquer(LinkedList<ArbreCliques> niveauActuel,
							Hashtable<ArbreCliques, ArbreCliques> table)
	{
		//System.out.println("entrée dans la récursive, taille du niveau: " + niveauActuel.size());
		LinkedList<ArbreCliques> niveauSuivant = new LinkedList<>();
		for(ArbreCliques a: niveauActuel)
		{
			ArbreCliques f = new ArbreCliques(a.getValeur());
			//maj de la table avec l'arbre a et sa copie
			table.put(a, f);
			//recopie de la liste des pères; la table permet d'éviter d'associer les anciens pères 
			//à un nouveau noeud
			for(ArbreCliques p: a.getListePeres())
			{
				ArbreCliques p2 = table.get(p);
				//si le père de a n'est pas dans la table, c'est qu'il n'a pas été dupliqué auparavant
				//dans ce cas on ne cherche pas à dupliquer les pères récursivement
				//on se contente d'ajouter le père de a à la liste des pères de sa copie
				if(p2 == null)
				{
					f.getListePeres().add(p);
					p.getListeFils().add(f);
				}
				//sinon on associe la copie (p2) du père de a à f
				else
				{
					f.getListePeres().add(p2);
					p2.getListeFils().add(f);
				}
			}
			//on crée le niveau suivant de noeuds à dupliquer
			for(ArbreCliques fils: a.getListeFils())
			{
				if(!niveauSuivant.contains(fils)) niveauSuivant.add(f);
			}
		}
		// si le niveau suivant est vide, on a terminé, sinon on continue
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
//	public LinkedList<ArbreCliques> getListeBloquants() {
//		return listeBloquants;
//	}

	/**
	 * @param listeBloquants the listeBloquants to set
	 */
//	public void setListeBloquants(LinkedList<ArbreCliques> listeBloquants) {
//		this.listeBloquants = listeBloquants;
//	}	
	
	/**
	 * fonction récursive appelée par toString();
	 * on n'affiche pas tout l'arbre, uniquement les cliques "valides"
	 * à utiliser si on se sert de la liste des "bloquants"
	 * @param chaine
	 * @return
	 */
//	protected String toString(String chaine, LinkedList<ArbreCliques> listeInterdits)
//	{
//		if(listeInterdits.contains(this))
//		{
//			return "";
//		}
//		else
//		{
//			if(estFeuille())
//			{
//				String res = chaine + ", " + valeur + "\n";
//				return res.substring(2);
//			}
//			else
//			{
//				String local;
//				if(valeur == null) local = chaine;
//				else local = chaine + ", " + valeur;
//				String res = "";
//				listeInterdits.addAll(listeBloquants);
//				for(ArbreCliques a: listeFils)
//				{
//					res += a.toString(local, new LinkedList<>(listeInterdits));
//				}
//				return res;
//			}
//		}
//		
//	}
	
	/**
	 * renvoie la liste des cliques (1 clique par ligne)
	 */
	public String toString()
	{
		//return toString("", new LinkedList<ArbreCliques>());
		return toString("");
	}
	
	/**
	 * fonction récursive appelée par toString() quand on utilise un BDD "normal"
	 * @param prefixe
	 * @return
	 */
	protected String toString(String prefixe)
	{
		if(estFeuille())
			return prefixe + this.valeur + "\n";
		else
		{
			String retour = "";
			for(ArbreCliques f: listeFils)
			{
				retour += f.toString(prefixe + this.valeur + ", ");
			}
			return retour;
		}
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
			int min = Integer.MAX_VALUE;
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
				//si on utilise la liste des "bloquants", décommenter cette ligne et commenter la suivante
				//a.addProcessus(nouveau, hauteur - 1, new LinkedList<ArbreCliques>());
				a.addProcessus(nouveau, hauteur-1);
			}
			
		}
		else listeFils.add(nouveau);
	}
	
	/**
	 * fonction récursive ajoutant un processus dans l'arbre à une profondeur donnée,<br/>
	 * en vérifiant que le processus possède une association avec chaque noeud de la branche
	 * à utiliser quand on a recours à la liste des "bloquants"
	 * @param p
	 * @param profondeur
	 */
//	protected void addProcessus(ArbreCliques nouveau, 
//								int profondeur,
//								LinkedList<ArbreCliques> listeInterdits
//								)
//	{
//		if(valeur.accepte(nouveau.getValeur()))
//		{
//			
//			if(!listeInterdits.contains(this))
//			{
//				if(profondeur == 0)
//				{
//					if(!listeFils.contains(nouveau)) 
//						listeFils.add(nouveau);
//					if(!nouveau.getListePeres().contains(this)) 
//						nouveau.getListePeres().add(this);
//				}
//				else
//				{
//					listeInterdits.addAll(listeBloquants);
//					for(ArbreCliques a: listeFils)
//					{
//						a.addProcessus(nouveau, profondeur - 1, new LinkedList<>(listeInterdits));
//					}
//				}
//			}
//			
//			
//		}
//		else
//		{
//			listeBloquants.add(nouveau);
//		}
//	}

	protected void addProcessus(ArbreCliques nouveau, int profondeur)
	{
		
		if(valeur.accepte(nouveau.getValeur()))
		{
			if(profondeur == 0)
			{
				//2 vérifications valent mieux qu'une; la structure étant complexe, on risque
				//d'avoir des problèmes inattendus
				if(!listeFils.contains(nouveau))
				{
					listeFils.add(nouveau);
					//System.out.println("nouveau ajouté");
					if(!nouveau.listePeres.contains(this))
					{
						nouveau.listePeres.add(this);
						//System.out.println("père ajouté");
					}
					
					ArbreCliques[] copiePeres = new ArbreCliques[listePeres.size()];
					copiePeres = listePeres.toArray(copiePeres);
//					for(ArbreCliques pere: listePeres)
//					{
//						//System.out.println("addProcessus:" + this.hashCode() + ", "+ pere.hashCode());
//						pere.remonter(nouveau.getValeur(), this);
//					}
					for(ArbreCliques pere: copiePeres)
					{
						pere.remonter(nouveau.getValeur(), this);
					}
				}
			}
			else
			{
				ArbreCliques[] copieFils = new ArbreCliques[listeFils.size()];
				copieFils = listeFils.toArray(copieFils);
//				for(ArbreCliques fils: listeFils)
//				{
//					fils.addProcessus(nouveau, profondeur-1);
//				}
				for(ArbreCliques fils: copieFils)
				{
					fils.addProcessus(nouveau, profondeur - 1);
				}
			}
		}
			
	}
	/**
	 * @param nouveau
	 */
	protected void remonter(Processus nouveau, ArbreCliques filsPrecedent) 
	{
		if(valeur != null)
		{
			this.filsPrecedent = filsPrecedent;
			if(this.valeur.accepte(nouveau))
			{
				try
				{
					ArbreCliques[] copiePeres = new ArbreCliques[listePeres.size()];
					copiePeres = listePeres.toArray(copiePeres);

					//la copie évite les pbl de modification concurrente
					for(ArbreCliques pere: copiePeres)
					{
						//System.out.println("remonter:" + this.hashCode() + ", " + pere.hashCode());
						pere.remonter(nouveau, this);
					}
				}
				catch(ConcurrentModificationException e)
				{
					System.err.println("Erreur dans remonter: this = " + this.valeur + 
							", nouveau = " + nouveau);
					System.err.println("stacktrace:");
					e.printStackTrace();
					System.exit(1);
				}
			}
			else
			{
				//System.out.println("recherche du croisement");
				rechercherCroisement(nouveau);
			}
		}
	}

	/**
	 * @param nouveau
	 */
	protected void rechercherCroisement(Processus nouveau) 
	{
		if(filsPrecedent.listePeres.size() > 1)
		{
			ArbreCliques copie = filsPrecedent.dupliquer();
			copie.remove(nouveau);
			//on "casse" le lien avec le fils d'origine
			filsPrecedent.listePeres.remove(this);
			listeFils.remove(filsPrecedent);
			
			//on crée le lien avec la copie
			copie.listePeres = new LinkedList<ArbreCliques>();
			copie.listePeres.add(this);
			listeFils.add(copie);
		}
		else
			filsPrecedent.rechercherCroisement(nouveau);
	}

	/**
	 * supprime les noeuds de l'arbre dont la valeur vaut nouveau
	 * @param nouveau
	 */
	protected void remove(Processus nouveau) 
	{
		if(!estFeuille())
		{
			ArbreCliques[] listeSuppression = new ArbreCliques[listeFils.size()];
			int i = 0;
			for(ArbreCliques fils: listeFils)
			{
				if(fils.valeur == nouveau)
				{
					listeSuppression[i] = fils;
					i++;
				}
			}
			for(ArbreCliques a: listeSuppression)
			{
				listeFils.remove(a);
			}
			for(ArbreCliques a: listeFils)
			{
				a.remove(nouveau);
			}
		}
	}

	/**
	 * supprime les branches de profondeur inférieure à la valeur passée en param�tre
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
