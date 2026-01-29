package s21.peanutsh.TicTacToe.datasource.mapper;

import org.json.JSONArray;
import s21.peanutsh.TicTacToe.datasource.model.EntityGame;
import s21.peanutsh.TicTacToe.datasource.model.EntityModel;
import s21.peanutsh.TicTacToe.domain.model.GameField;
import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.controller.Controller;

import javax.swing.text.html.parser.Entity;
import java.util.UUID;
import java.util.logging.Logger;

public class Mapper {
    private static final Logger logger =  Logger.getLogger(Controller.class.getName());
    public static EntityGame convertGameToEntity(GameField gameField, UUID uuid) {
        return new EntityGame(uuid, converterFieldToJSJON(gameField.getData()), gameField.getStatus(), gameField.getWinner());
    }

    public static GameField convertEntityToGame(EntityGame entityGame) {
        return new GameField(converterStringToField(entityGame.getField()), entityGame.getGame_status(), entityGame.getGame_won());
    }


    private static String converterFieldToJSJON(int[][] field) {

        JSONArray jsonArray = new JSONArray();
        for (int[] i : field) {
            JSONArray f = new JSONArray();
            for (int j : i) {
                f.put(j);
            }
            jsonArray.put(f);
        }
        return jsonArray.toString();
    }

    private static int[][] converterStringToField(String json) {
        int[][] field = new int[3][3];
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray js = jsonArray.getJSONArray(i);
            for (int j = 0; j < js.length(); j++) {
                field[i][j] = js.getInt(j);
            }
        }
        return field;
    }


    public static EntityModel convertModelToEntity(Model model) {
        return EntityModel.builder()
                .uuidGame(model.getUuidGame())
                .field(converterFieldToJSJON(model.getField()))
                .bot(model.getPlayingWithComputer())
                .firstPlayer(model.getFirstPlayer())
                .secondPlayer(model.getSecondPlayer())
                .stateGame(model.getStateGame())
                .gameOver(model.getGameOver())
                .build();
    }


    public static Model convertEntityToModel(EntityModel entityModel) {
        return Model.builder()
                .uuidGame(entityModel.getUuidGame())
                .field(converterStringToField(entityModel.getField()))
                .firstPlayer(entityModel.getFirstPlayer())
                .secondPlayer(entityModel.getSecondPlayer())
                .stateGame(entityModel.getStateGame())
                .playingWithComputer(entityModel.getBot())
                        .gameOver(entityModel.getGameOver())
        .build();
    }


}
