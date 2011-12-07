import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: rechercher les n-1 cliques et pas les n cliques
/**
 * @author Jean AUNIS
 *
 */
public class Graphe {

	protected LinkedList<Sorte> listeSortes = new LinkedList<Sorte>();
	protected LinkedList<Clique> listeCliques = new LinkedList<Clique>();
		
	/**
	 * essai changement
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Graphe g = new Graphe();
		g.chargerGraphe("src/graphes/tcrsig40_flat.ph");
		System.out.println("Graphe chargé. Calcul du HitlessGraph...");
		g.getHitlessGraph();
		System.out.println("HitlessGraph calculé. Nettoyage...");
		g.nettoyerGraphe();
		System.out.println("HitlessGraph nettoyé. Suppression des listes de frappes...");
		g.supprimerHits();
		//System.out.println(g);
		System.out.println("Frappes supprimées. Tri...");
		Trieur.trierSortes("rand", g.getListeSortes());
		Trieur.trierSortesOptimal("minMinNbRelations", "doubleMin", g.getListeSortes());
		Trieur.reverseSortes(g.getListeSortes());
		System.out.println("Tri effectué. Recherche des n-cliques...");
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
	 * @return the listeCliques
	 */
	public LinkedList<Clique> getListeCliques() {
		return listeCliques;
	}
	/**
	 * @param listeCliques the listeCliques to set
	 */
	public void setListeCliques(LinkedList<Clique> listeCliques) {
		this.listeCliques = listeCliques;
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
	public String afficherCliques()
	{
		String res = "";
		for(Clique c: listeCliques)
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
	 * <b>Pr�requis:</b> le HitlessGraph a d�j� �t� nettoy�
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
				listeCliques.add(new Clique(l));
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
		//nouvelle liste contenant les cliques augmentées d'un processus
		LinkedList<Clique> listeTemp = new LinkedList<Clique>();
		for(Processus p1: s.getListeProcessus())
		{
			for(Clique c: listeCliques)
			{
				boolean convient = true;
				/*
				 * pour chaque processus de la clique, on regarde s'il a un lien
				 * avec le processus en cours de traitement
				 */
				for(Processus p2: c.getListeProcessus())
				{
					convient &= p2.getListeAssociations().contains(p1);
				}
				/*
				 * le processus testé convient s'il a un lien avec chacun des processus de la clique
				 * Dans ce cas on l'insère, et on ajoute la clique ainsi générée dans listeTemp
				 */
				if(convient)
				{
					Clique cliqueAugmentee = new Clique(c);
					cliqueAugmentee.addProcessus(p1);
					listeTemp.add(cliqueAugmentee);
				}
			}
		}
		//on remplace l'ancienne liste par la nouvelle
		this.listeCliques = listeTemp;
	}
	
	/**
	 * charge le graphe contenu dans le fichier .ph pass� en param�tre

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
}
