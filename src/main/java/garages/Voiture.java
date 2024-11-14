package garages;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.PrintStream;
import java.util.*;


/**
 * Représente une voiture qui peut être stationnée dans des garages.
 */
@RequiredArgsConstructor
@ToString
public class Voiture {

	@Getter
	@NonNull
	private final String immatriculation;
	@ToString.Exclude // On ne veut pas afficher les stationnements dans toString
	private final List<Stationnement> myStationnements = new LinkedList<>();

	public void entreAuGarage(Garage g) throws IllegalStateException {
		if (estDansUnGarage()) {
			throw new IllegalStateException("Un garage contient déjà cette voiture.");
		}
		Stationnement s = new Stationnement(this, g);
		myStationnements.add(s);
	}

	public void sortDuGarage() throws IllegalStateException {
		if (!estDansUnGarage()) {
			throw new IllegalStateException("Cette voiture n'est pas dans un garage.");
		}
		myStationnements.get(myStationnements.size() - 1).terminer();
	}

	public Set<Garage> garagesVisites() {
		Set<Garage> garages = new HashSet<>();
		for (Stationnement s : myStationnements) {
			garages.add(s.getGarageVisite());
		}
		return garages;
	}

	public boolean estDansUnGarage() {
		if (myStationnements.isEmpty()) {
			return false;
		}
		return myStationnements.get(myStationnements.size() - 1).estEnCours();
	}

	public void imprimeStationnements(PrintStream out) { //pour choisir où on l'imprime.
		Map<Garage, List<Stationnement>> stationnementsParGarage = new LinkedHashMap<>();
		for (Stationnement s : myStationnements) {
			stationnementsParGarage
					.computeIfAbsent(s.getGarageVisite(), k -> new ArrayList<>())
					.add(s);
		}

		for (Map.Entry<Garage, List<Stationnement>> entry : stationnementsParGarage.entrySet()) {
			out.println("Garage(name=" + entry.getKey().getName() + "):");
			for (Stationnement s : entry.getValue()) {
				out.println("\t" + s);
			}
		}
	}
}
