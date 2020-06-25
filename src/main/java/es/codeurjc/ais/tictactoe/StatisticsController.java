package es.codeurjc.ais.tictactoe;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Controller
    public class StatisticsController {
	
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public String getPlayers(Model model) {
    	List<Player> jugadores = TicTacToeGame.getService().getPlayers();
    	model.addAttribute("players", jugadores);
    	return "stats";
    }
        
}