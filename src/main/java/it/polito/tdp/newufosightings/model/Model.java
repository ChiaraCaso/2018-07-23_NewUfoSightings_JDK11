package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	
	NewUfoSightingsDAO dao ;
	Graph<State, DefaultWeightedEdge> grafo;
	Map <String, State> idMap;
	List<State> vertici ;
	List <Arco> archi;
	
	public Model () {
		dao = new NewUfoSightingsDAO();
		this.grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<String, State>();
		this.vertici = new ArrayList<State>();
		this.archi = new ArrayList<Arco>();
	}
	
	public void creaGrafo (Integer anno, String forma) {
		
		dao.loadAllStates(idMap);
		
		this.vertici = dao.loadAllStates(idMap);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		this.archi = dao.getArchi(forma, anno, idMap);
		
		for (Arco a : archi) {
			if (this.grafo.containsVertex(a.getS1()) && this.grafo.containsVertex(a.getS2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), a.getPeso());
			}
		}
	}
	
	public List <String> getForme (Integer anno) {
		return dao.getForme(anno);
	}
	
	public int nVertici () {
		return this.grafo.vertexSet().size();
	}
		
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List <Arco> getConnessione() {
		return archi;
	}

}
