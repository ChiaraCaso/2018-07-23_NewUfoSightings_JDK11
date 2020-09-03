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
	
	public Model () {
		dao = new NewUfoSightingsDAO();
		this.grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<String, State>();
		this.vertici = new ArrayList<State>();
	}
	
	public void creaGrafo (Integer anno, String forma) {
		
		dao.loadAllStates(idMap);
		
		this.vertici = dao.getStati(anno, forma, idMap);
		
		Graphs.addAllVertices(this.grafo, vertici);
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

}
