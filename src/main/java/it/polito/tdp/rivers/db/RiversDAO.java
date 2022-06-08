package it.polito.tdp.rivers.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import it.polito.tdp.rivers.model.Evento;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public List<River> getAllRivers() {
		
		final String sql = "SELECT id, name FROM river";

		List<River> rivers = new LinkedList<River>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				rivers.add(new River(res.getInt("id"), res.getString("name")));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return rivers;
	}
	
	public String getfirst(int idriver) {
		
		final String sql = "SELECT MIN(DAY) AS min "
				+ "FROM flow f "
				+ "WHERE f.river=?";

		String s="";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idriver);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				s=res.getString("min");
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return s;
	}
	public String getlast(int idriver) {
			
			final String sql = "SELECT MAX(DAY) AS max "
					+ "FROM flow f "
					+ "WHERE f.river=?";
	
			String s="";
	
			try {
				Connection conn = DBConnect.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, idriver);
				ResultSet res = st.executeQuery();
	
				while (res.next()) {
					s=res.getString("max");
				}
	
				conn.close();
				
			} catch (SQLException e) {
				//e.printStackTrace();
				throw new RuntimeException("SQL Error");
			}
	
			return s;
		}
	public int getnmisurazioni(int idriver) {
		
		final String sql = "SELECT COUNT(f.id) AS c "
				+ "FROM flow f "
				+ "WHERE f.river=?";

		int s=0;

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idriver);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				s=res.getInt("c");
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return s;
	}
	
	public List<Double> getlistaflusso(int idriver) {
		
		final String sql = "SELECT flow "
				+ "FROM flow f "
				+ "WHERE f.river=?";

		List<Double> ris=new ArrayList<Double>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, idriver);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				ris.add(res.getDouble("flow"));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return ris;
	}

	public List<Evento> getvalori(int riverid) {
		final String sql = " SELECT flow "
				+ " from flow "
				+ " WHERE river=? "
				+ " ORDER BY day asc";

		List<Evento> rivers = new ArrayList<Evento>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, riverid);
			ResultSet res = st.executeQuery();
			int  i=0;
			while (res.next()) {
				i++;
				rivers.add(new Evento(i, res.getDouble("flow")));
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return rivers;
	}
}
