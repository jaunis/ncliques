import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jean AUNIS
 *
 */
@SuppressWarnings("unused")
public class Graphe {

	protected LinkedList<Sorte> listeSortes = new LinkedList<Sorte>();
	protected LinkedList<Clique> listeNCliques = new LinkedList<Clique>();
	protected LinkedList<Clique> listeNmoinsUnCliques = new LinkedList<Clique>();
	
	private static String fichierEntree = "egfr20_flat.ph";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Graphe g = new Graphe();
		g.chargerGraphe("src/graphes/" + Graphe.fichierEntree);
		
		System.out.println("Graphe chargé. Calcul du HitlessGraph...");
		g.getHitlessGraph();
		
		System.out.println("HitlessGraph calculé. Nettoyage...");
		g.nettoyerGraphe();
		
		System.out.println("HitlessGraph nettoyé. Suppression des listes de frappes...");
		g.supprimerHits();
		
		System.out.println("Frappes supprimées. Tri...");
		Trieur.trierSortes("rand", g.getListeSortes());
		Trieur.trierSortesOptimal("minMinNbRelations", "doubleMin", g.getListeSortes());
		Trieur.reverseSortes(g.getListeSortes());
		
		System.out.println("Tri effectué. Recherche des n-1-cliques...");
		Date datedeb = new Date();
		g.rechercherCliques();
		Date datefin = new Date();
		long duree = datefin.getTime() - datedeb.getTime();
		
		System.out.println("cliques trouvées en: " + duree);
		
		boolean coherent = true;
		int n = g.getListeSortes().size();
		for(Clique c: g.getListeNmoinsUnCliques())
		{
			coherent &= (c.getListeProcessus().size() ==n-1);
		}
		if(coherent)
			System.out.println("Toutes les tailles sont correctes.");
		else
			System.out.println("Tailles différentes!");
		
		System.out.println("Nombre de n-1-cliques: " + g.getListeNmoinsUnCliques().size());
		System.out.println("Nombre de n-cliques:" + g.getListeNCliques().size());
		
		if(g.existeDoublons()) System.out.println("Il y a des doublons.");
		else System.out.println("Pas de doublons.");
	}
	
	/**
	 * @return the listeNmoinsUnCliques
	 */
	public LinkedList<Clique> getListeNmoinsUnCliques() {
		return listeNmoinsUnCliques;
	}
	/**
	 * @param listeNmoinsUnCliques the listeNmoinsUnCliques to set
	 */
	public void setListeNmoinsUnCliques(LinkedList<Clique> listeNmoinsUnCliques) {
		this.listeNmoinsUnCliques = listeNmoinsUnCliques;
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
	 * @return the listeCliques
	 */
	public LinkedList<Clique> getListeNCliques() {
		return listeNCliques;
	}
	/**
	 * @param listeCliques the listeCliques to set
	 */
	public void setListeNCliques(LinkedList<Clique> listeCliques) {
		this.listeNCliques = listeCliques;
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
				//for(Processus h: p.getListeHits())
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
	public String afficherNCliques()
	{
		String res = "";
		for(Clique c: listeNCliques)
		{
			res += c.toString() + "\n";
		}
		return res;
	}
	
	public String afficherNmoinsUnCliques()
	{
		String res = "";
		for(Clique c: listeNmoinsUnCliques)
		{
			res += c.toString() + "\n";
		}
		return res;
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
	 *@param liste: la liste de sortes à utiliser
	 */
	protected void rechercherCliques(LinkedList<Sorte> liste)
	{
		/*
		 * si la taille est de 1, chaque processus pris indépendamment constitue une clique
		 */
		if(liste.size() == 1)
		{
			for(Processus p: liste.getFirst().getListeProcessus())
			{
				LinkedList<Processus> l = new LinkedList<Processus>();
				l.add(p);
				listeNCliques.add(new Clique(l));
			}
			
		}
		/*
		 * sinon on dépile la première sorte, on recherche les cliques dans la liste ainsi réduite
		 * et on insère la sorte dans la liste des cliques trouvée
		 */
		else
		{
			Sorte first = liste.removeFirst();
			rechercherCliques(liste);
			ajouterSorte(first);
		}
	}
	/**
	 * Ajoute les processus contenus dans la sorte aux cliques déjà calculées
	 * @param s: la Sorte à ajouter
	 */
	public void ajouterSorte(Sorte s)
	{
		//nouvelles listes de cliques
		LinkedList<Clique> listeTempN = new LinkedList<Clique>();
		LinkedList<Clique> listeTempNMoinsUn = new LinkedList<Clique>();
		for(Processus p1: s.getListeProcessus())
		{
			for(Clique c: listeNCliques)
			{
				int nbRejets = 0;
				/*
				 * pour chaque processus de la clique, on regarde s'il a un lien
				 * avec le processus en cours de traitement
				 */
				/*
				 * quand on recherchera les n-k cliques, remplacer pRejete par 
				 * une liste de processus rejetés
				 */
				Processus pRejete = null;
				Iterator<Processus> it = c.getListeProcessus().iterator();
				while(it.hasNext() && nbRejets < 2)
				{
					Processus p2 = it.next();
					if(!p2.getListeAssociations().contains(p1))
					{
						nbRejets++;
						pRejete = p2;
					}
				}
				/*
				 * Le processus testé a un lien avec tous ceux de la clique.
				 * Donc on l'ajoute à la clique, que l'on ajoute à liste des n-cliques
				 */
				if(nbRejets == 0)
				{
					Clique cliqueAugmentee = new Clique(c);
					cliqueAugmentee.addProcessus(p1);
					listeTempN.add(cliqueAugmentee);
				}
				/*
				 * Le processus testé a un lien avec tous ceux de la clique sauf un.
				 * Donc on l'ajoute à la clique après avoir supprimé le processus bloquant,
				 * puis on ajoute la clique à la liste des n-1-cliques
				 */
				else if(nbRejets == 1)
				{
					Clique cliqueAugmentee = new Clique(c);
					cliqueAugmentee.remove(pRejete);
					cliqueAugmentee.addProcessus(p1);
					listeTempNMoinsUn.add(cliqueAugmentee);
				}
				/*
				 * Le processus testé n'a pas assez de liens avec les éléments de la clique.
				 * On garde donc la clique telle quelle, et on l'ajoute à la liste des
				 * n-1-cliques
				 */
				//TODO correction possible: ajouter c à listeTempNMoinsUn dans tous les cas
				else
				{
					if(!listeTempNMoinsUn.contains(c))
						listeTempNMoinsUn.add(c);
				}
			}
			
			for(Clique c: listeNmoinsUnCliques)
			{
				boolean convient = true;
				Iterator<Processus> it = c.getListeProcessus().iterator();
				while(it.hasNext() && convient)
				{
					Processus p2 = it.next();
					convient &= p2.getListeAssociations().contains(p1);
				}
				if(convient)
				{
					Clique cliqueAugmentee = new Clique(c);
					cliqueAugmentee.addProcessus(p1);
					if(!listeTempNMoinsUn.contains(cliqueAugmentee))
						listeTempNMoinsUn.add(cliqueAugmentee);
				}
			}
		}
		//on remplace les anciennes listes par les nouvelles
		this.listeNCliques = listeTempN;
		this.listeNmoinsUnCliques = listeTempNMoinsUn;
	}
	
	/**
	 * charge le graphe contenu dans le fichier .ph passé en param�tre

	 * @param nomFichier le nom du fichier à charger
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
			
			//expression régulière pour extraire la liste des sortes
			Pattern p1 = Pattern.compile("^process\\s(.*)\\s(\\d*)$");
			//expression régulière pour extraire la liste des frappes
			Pattern p2 = Pattern.compile("^(.*)\\s(\\d*)\\s->\\s([^\\s]*)\\s(\\d*)\\s(\\d*)");
			while ((ligne=br.readLine())!=null)
			{
				Matcher m1 = p1.matcher(ligne.trim());
				Matcher m2 = p2.matcher(ligne.trim());
				
				/*
				 * m1 correspond, on crée donc une sorte avec le bon nombre de processus
				 */
				if(m1.find())
				{
					String nom = m1.group(1);
					int taille = Integer.parseInt(m1.group(2)) + 1;
					listeSortes.add(new Sorte(nom, this, taille));
				}
				
				/*
				 * m2 correspond, on ajoute donc une frappe entre deux processus
				 */
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
	 * récupère une Sorte grâce à son nom
	 * @param nom: le nom de la Sorte qu'on veut récupérer
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
	 * calcule le HitlessGraph: on crée des associations entre les processus<br/>
	 * qui ne se "frappent" pas
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
				while(it.hasNext())
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
	 * supprime les listes de frappe pour lib�rer de la m�moire
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
	
	/**
	 * teste la présence de doublons dans la liste de cliques<br/>
	 * !!détruit listeNMoinsUnCliques
	 * @return
	 */
	public boolean existeDoublons()
	{
		boolean res = false;
		for(int i = 0; i<this.listeNmoinsUnCliques.size(); i++)
		{
			Clique c = this.listeNmoinsUnCliques.removeFirst();
			res |= this.listeNmoinsUnCliques.contains(c);
		}
		return res;
	}
}
