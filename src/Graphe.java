import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Graphe {

	protected LinkedList<Sorte> listeSortes = new LinkedList<Sorte>();
	/*
	 * contient la liste des cliques sous forme d'arbre
	 * (nombre de cliques = nombre de feuilles)
	 */
	protected ArbreCliques arbre = new ArbreCliques();
		
	public static void main(String[] args) 
	{
		Graphe g = new Graphe();
		g.chargerGraphe("src/graphes/egfr20_tcrsig40_flat.ph");
		System.out.println("Graphe chargé. Calcul du HitlessGraph...");
		g.getHitlessGraph();
		System.out.println("HitlessGraph calculé. Nettoyage...");
		g.nettoyerGraphe();
		System.out.println("HitlessGraph nettoyé. Suppression des listes de frappes...");
		g.supprimerHits();
		System.out.println("Frappes supprimées. Recherche des n-cliques...");
		//g.reverseSortes();
		//g.trierSortes("asc");
		g.trierSortesOptimal();
		Date datedeb = new Date();
		g.rechercherCliques();
		Date datefin = new Date();
		long duree = datefin.getTime() - datedeb.getTime();
		System.out.println("cliques trouvées en: " + duree);
		System.out.println(g.afficherCliques());
	}
	/**
	 * @return the listeSortes
	 */
	public LinkedList<Sorte> getListeSortes() {
		return listeSortes;
	}
	/**
	 * @param listeSortes the listeSortes to set
	 */
	public void setListeSortes(LinkedList<Sorte> listeSortes) {
		this.listeSortes = listeSortes;
	}
	
	
	
	/**
	 * @return the arbre
	 */
	public ArbreCliques getArbre() {
		return arbre;
	}
	/**
	 * @param arbre the arbre to set
	 */
	public void setArbre(ArbreCliques arbre) {
		this.arbre = arbre;
	}
	/**
	 * affiche l'arbre sous la forme suivante:<br/>
	 * nom_sorte{noms_processus}<br/>
	 * nom_processus:<br/>
	 * liste_associations
	 */
	public String toString()
	{
		String res = "";
		for(Sorte s: listeSortes)
		{
			res += s.toString() + "\n";
			for(Processus p: s.getListeProcessus())
			{
				res += p.toString() + " :\n";
				for(Processus a: p.getListeAssociations())
				{
					res += "  " + a.toString() + "\n";
				}
			}
		}
		
		return res;
	}
	/**
	 * affiche la liste des cliques (1 clique par ligne)
	 * @return
	 */
	public String afficherCliques()
	{
		return arbre.toString();
	}
	
	/**
	 * supprime du HitlessGraph les Processus n'ayant pas une association avec chaque Sorte<br/>
	 * <b>Prérequis: </b> Le HitlessGraph a déjà été calculé
	 */
	public void nettoyerGraphe()
	{
		boolean cont = true;
		while(cont)
		{
			cont = false;
			for(Sorte s: listeSortes)
			{
				cont = cont || s.nettoyerSorte();
			}
		}
		
	}
	/**
	 * Calcule la liste des cliques<br/>
	 * <b>Prérequis:</b> le HitlessGraph a déjà été nettoyé
	 */
	public void rechercherCliques()
	{
		rechercherCliques(new LinkedList<Sorte>(this.listeSortes));
	}
	
	/**
	 * méthode appelée par par rechercherCliques()
	 * @param liste
	 */
	protected void rechercherCliques(LinkedList<Sorte> liste)
	{
		if(!liste.isEmpty())
		{
			Sorte first = liste.removeFirst();
			ajouterSorte(first);
			//System.out.println(liste.size());
			rechercherCliques(liste);	
		}
		
	}
	/**
	 * ajoute les Processus d'une Sorte dans l'arbre des cliques
	 * @param s
	 */
	public void ajouterSorte(Sorte s)
	{
		for(Processus p: s.getListeProcessus())
		{
			arbre.addProcessus(p);
		}
		arbre.setHauteur(arbre.getHauteur() + 1);
		arbre.nettoyer();
	}
	
	/**
	 * charge le graphe contenu dans le fichier .ph passé en paramètre
	 * @param nomFichier
	 */
	public void chargerGraphe(String nomFichier)
	{
		/*try
		{
			InputStream ips=new FileInputStream(nomFichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while (!(ligne=br.readLine()).equals(""))
			{
				String nom = ligne.substring(0,1);
				int taille = Integer.parseInt(ligne.substring(2, 3));
				listeSortes.add(new Sorte(nom, this, taille));
			}
			while((ligne=br.readLine()) != null)
			{
				Sorte s1 = getSorteByNom(ligne.substring(0, 1));
				Sorte s2 = getSorteByNom(ligne.substring(4, 5));
				int numero1 = Integer.parseInt(ligne.substring(2, 3));
				int numero2 = Integer.parseInt(ligne.substring(6, 7));
				Processus p1 = s1.getProcessusByNumero(numero1);
				Processus p2 = s2.getProcessusByNumero(numero2);
				p1.addAssociation(p2);
				p2.addAssociation(p1);
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
		
		try
		{
			InputStream ips=new FileInputStream(nomFichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			Pattern p1 = Pattern.compile("^process\\s(.*)\\s(\\d*)$");
			Pattern p2 = Pattern.compile("^(.*)\\s(\\d*)\\s->\\s([^\\s]*)\\s(\\d*)\\s(\\d*)");
			while ((ligne=br.readLine())!=null)
			{
				Matcher m1 = p1.matcher(ligne.trim());
				Matcher m2 = p2.matcher(ligne.trim());
				if(m1.find())
				{
					String nom = m1.group(1);
					int taille = Integer.parseInt(m1.group(2)) + 1;
					listeSortes.add(new Sorte(nom, this, taille));
				}
				else if(m2.find())
				{
					String nom1 = m2.group(1);
					Sorte s1 = this.getSorteByNom(nom1);
					int numero1 = Integer.parseInt(m2.group(2));
					Processus proc1 = s1.getProcessusByNumero(numero1);
					
					String nom2 = m2.group(3);
					Sorte s2 = this.getSorteByNom(nom2);
					int numero2 = Integer.parseInt(m2.group(4));
					Processus proc2 = s2.getProcessusByNumero(numero2);
					
					proc1.addHit(proc2);
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	/**
	 * récupère une Sorte à l'aide de son nom
	 * @param nom
	 * @return
	 */
	public Sorte getSorteByNom(String nom)
	{
		boolean cont = true;
		int i=0;
		try
		{
			while(cont)
			{
				Sorte s = listeSortes.get(i);
				if(s.getNom().equals(nom)) cont = false;
				i++;
			}
			return listeSortes.get(i-1);
		}
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("Nom: " + nom);
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Calcule le HitlessGraph à partir de la liste des frappes
	 */
	public void getHitlessGraph()
	{
		int i=0;
		for(Sorte s1: listeSortes)
		{
			for(Processus p1: s1.getListeProcessus())
			{
				Iterator<Sorte> it = listeSortes.iterator();
				/*
				 * positionnement de l'itérateur à l'indice i+1
				 */
				for(int j=0;j<i+1;j++)
				{
					it.next();
				}
				for(int j=i+1;j<listeSortes.size();j++)
				{
					Sorte s2 = it.next();
					if(s2 != s1)
					{
						for(Processus p2: s2.getListeProcessus())
						{
							
							if(!(p1.getListeHits().contains(p2)||p2.getListeHits().contains(p1)))
							{
								p1.addAssociation(p2);
								p2.addAssociation(p1);
							}
						}
					}
				}
			}
			i++;
		}
	}
	/**
	 * inverse l'ordre de listeSortes
	 */
	public void reverseSortes()
	{
		Collections.reverse(listeSortes);
	}
		
	/**
	 * Insère une Sorte au bon endroit dans une liste déjà triée<br/>
	 * fonction appelée par rechercherCliques(String, LinkedList)
	 * @param sens
	 * @param liste
	 * @param s
	 * @return
	 */
	protected LinkedList<Sorte> insererSorteDansListe(String sens, LinkedList<Sorte> liste, Sorte s)
	{
		LinkedList<Sorte> listeTemp = new LinkedList<Sorte>();
		boolean cont = true;
		int taille = s.getTotalAssociations();
		while(cont)
		{
			Sorte courant = liste.getFirst();
			
			if((sens.equals("asc") && (courant.getTotalAssociations()>=taille)) || (sens.equals("desc") && (courant.getTotalAssociations()<=taille)))
			{
				liste.addFirst(s);
				cont = false;
			}
			else
			{
				
				if(liste.size()==1)
				{
					liste.add(s);
					cont = false;
				}
				else
				{
					liste.removeFirst();
					listeTemp.addFirst(courant);
				}
			}
		}
		while(listeTemp.size()>0)
		{
			Sorte s2 = listeTemp.removeFirst();
			liste.addFirst(s2);
		}
		return liste;
	}
	
	/**
	 * Trie la liste de Sortes par ordre aléatoire (sens="rand"), croissant (sens="asc")
	 * ou décroissant (sens="desc")
	 * @param sens
	 * @throws InvalidParameterException
	 */
	public void trierSortes(String sens) throws InvalidParameterException
	{
		if(!(sens.equals("asc")||sens.equals("desc")||sens.equals("rand"))) throw new InvalidParameterException("Entrez \"rand\", \"asc\" ou \"desc\" en paramètre.");
		if(sens.equals("rand")) Collections.shuffle(listeSortes);
		else
		{
			Collections.sort(listeSortes);
			if(sens.equals("desc")) Collections.reverse(listeSortes);
		}
	}
	
	public void trierSortesOptimal()
	{
		LinkedList<Sorte> listeTemp = new LinkedList<Sorte>();
		Sorte min = Collections.min(listeSortes);
		listeTemp.add(min);
		listeSortes.remove(min);
		while(!listeSortes.isEmpty())
		{
			min = sorteAInserer(listeTemp);
			listeTemp.add(min);
			listeSortes.remove(min);
		}
		listeSortes = listeTemp;
	}
	protected Sorte sorteAInserer(LinkedList<Sorte> listeTemp) 
	{
		Sorte res = listeSortes.getFirst();
		int min = res.getNbAssociations(listeTemp.getFirst());
		for(Sorte s: listeSortes)
		{
			int minLocal = s.getNbAssociations(listeTemp.getFirst());
			for(Sorte s2: listeTemp)
			{
				if(minLocal > s.getNbAssociations(s2)) minLocal = s.getNbAssociations(s2);
			}
			if(minLocal<min)
			{
				min=minLocal;
				res = s;
			}
		}
		return res;
	}
	/**
	 * supprime les listes de frappe pour libérer de la mémoire
	 */
	public void supprimerHits()
	{
		for(Sorte s: listeSortes)
		{
			for(Processus p: s.getListeProcessus())
			{
				p.setListeHits(null);
			}
		}
	}
}
