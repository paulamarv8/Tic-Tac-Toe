package es.codeurjc.ais.tictactoe;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TicTacToeGame {

	enum EventType {
		JOIN_GAME, GAME_READY, MARK, SET_TURN, GAME_OVER, RESTART, RECONNECT
	}

	static class Cell {
		volatile boolean active = false;
		volatile String value;
	}

	static class WinnerResult {
		boolean win;
		int[] pos;
	}

	static class CellMarkedValue implements Serializable {
		int cellId;
		Player player;
	}

	static class WinnerValue implements Serializable {
		Player player;
		int[] pos;
	}

	static class Event implements Serializable {
		EventType type;
		Object data;
	}

	private List<Connection> connections = new CopyOnWriteArrayList<>();
	private List<Player> players = new CopyOnWriteArrayList<>();
	private Board board = new Board();
	private static StatisticsService service = new StatisticsService();

	private int currentTurn = 0;
	private int otherTurn = 1;
	private boolean ready = false;

	public void disableAll() {
		board.disableAll();
	}

	public void enableAll() {
		board.enableAll();
	}

	public boolean mark(int cellId) {

		Cell cell = this.board.getCell(cellId);

		if (cell == null) {
			return false;
		}

		if (this.ready && cell.active) {

			Player player = this.players.get(this.currentTurn);
			Player opponent = this.players.get(this.otherTurn);

			cell.value = player.getLabel();
			cell.active = false;

			CellMarkedValue value = new CellMarkedValue();
			value.cellId = cellId;
			value.player = player;

			this.sendEvent(EventType.MARK, value);

			WinnerResult res = this.checkWinner();

			if (res.win) {

				int ganadas = player.getPartidasGanadas();
				int perdidas = opponent.getPartidasPerdidas();
				player.setPartidasGanadas(ganadas + 1);
				opponent.setPartidasPerdidas(perdidas + 1);
				
				int puntosGanador = player.getPuntos();
				int puntosPerdedor = opponent.getPuntos();
				int puntosApuestaGanador = player.getPuntosApostados();
				int puntosApuestaPerdedor = opponent.getPuntosApostados();
				player.setPuntos(puntosGanador + 100 + (puntosApuestaGanador * 2));
				opponent.setPuntos(puntosPerdedor - 50 - puntosApuestaPerdedor);
				
				this.disableAll();

				WinnerValue winner = new WinnerValue();
				winner.player = this.players.get(this.currentTurn);
				winner.pos = res.pos;

				this.sendEvent(EventType.GAME_OVER, winner);
				restart();

			} else if (this.checkDraw()) {

				int empatadasPlayer = player.getPartidasEmpatadas();
				int empatadasOpponent = opponent.getPartidasEmpatadas();
				player.setPartidasEmpatadas(empatadasPlayer + 1);
				opponent.setPartidasEmpatadas(empatadasOpponent + 1);
				
				this.sendEvent(EventType.GAME_OVER, null);
				restart();

			} else {

				changeTurn();
			}
		}

		return true;
	}

	private void changeTurn() {
		this.currentTurn = (this.currentTurn + 1) % 2;
		this.sendEvent(EventType.SET_TURN, this.players.get(this.currentTurn));
	}

	public boolean checkTurn(int playerId) {
		return this.players.get(this.currentTurn).getId() == playerId;
	}

	public WinnerResult checkWinner() {
		
		Player player = this.players.get(this.currentTurn);

		int[] winPos = board.getCellsIfWinner(player.getLabel());
		
		WinnerResult result = new WinnerResult();
		result.win = (winPos != null);
		result.pos = winPos;
		
		return result;
	}

	public boolean checkDraw() {

		return board.checkFull();
	}

	public void addPlayer(Player player) {

		service.addPlayerSer(player);

		if (this.players.size() < 2) {

			if (this.players.isEmpty() || players.get(0).getId() != player.getId()) {

				this.players.add(player);
				this.ready = this.players.size() == 2;
				
				this.sendEvent(EventType.JOIN_GAME, players);

				if (this.ready) {
					this.enableAll();
					this.sendEvent(EventType.SET_TURN, this.players.get(this.currentTurn));
				}
			}
		}
	}

	public static StatisticsService getService() {
		return service;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void addConnection(Connection connection) {
		this.connections.add(connection);
	}
	
	public void restart() {

		board = new Board();
		board.enableAll();

		sendEvent(EventType.RESTART, this.players);

		changeTurn();
	}

	private void sendEvent(EventType type, Object value) {

		for(Connection c : connections) {
			c.sendEvent(type, value);
		}
	}
	
}
