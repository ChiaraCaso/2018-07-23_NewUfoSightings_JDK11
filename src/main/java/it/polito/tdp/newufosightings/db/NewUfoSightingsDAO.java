package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Arco;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				idMap.put(rs.getString("id"), state);
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	
	public List <String> getForme (Integer anno) {
		
		String sql = "SELECT DISTINCT shape " + 
				"FROM sighting " +
				"WHERE YEAR(DATETIME) = ? " +
				"ORDER BY shape asc ";
		
		List <String> result = new ArrayList<String>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(res.getString("shape"));
			}
			
			conn.close();
			return result;
			
		}  catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List <Arco> getArchi(String forma, Integer anno, Map<String, State> idMap) {
		String sql = "SELECT n.state1 AS s1, n.state2 AS s2 , COUNT(DISTINCT(si1.id)) AS peso " + 
				"FROM neighbor n, state s1, state s2, sighting si1, sighting si2 " + 
				"WHERE n.state1 = s1.id " + 
				"AND n.state2 = s2.id " + 
				"AND si1.state = s1.id " + 
				"AND si2.state = s2.id " + 
				"AND si1.id = si2.id " + 
				"AND si1.shape = ? " + 
				"AND si2.shape = ? " + 
				"AND YEAR(si1.datetime) = ? " + 
				"AND YEAR(si2.datetime) = ? " + 
				"GROUP BY s1, s2 ";
		
		List <Arco> result = new ArrayList<Arco>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setString(2, forma);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				State s1 = idMap.get(res.getString("s1"));
				State s2 = idMap.get(res.getString("s2"));
				
				if (s1 != null && s2 != null) {
					result.add(new Arco(s1, s2, res.getInt("peso")));
				}
			}
			
			conn.close();
			return result;
			
		}  catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}

