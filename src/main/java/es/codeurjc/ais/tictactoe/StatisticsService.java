package es.codeurjc.ais.tictactoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class StatisticsService {
	private Map<Integer,Player> jugadores = new HashMap<Integer,Player>();
	
	public StatisticsService(){};
	
	public void addPlayerSer(Player player) {
		this.jugadores.put(player.getId(), player);
	}
	
	public List<Player> getPlayers(){
		return new ArrayList<Player>(jugadores.values());
	}
}
