package s21.peanutsh.TicTacToe.datasource.mapper;

import org.json.JSONArray;
import s21.peanutsh.TicTacToe.datasource.model.ModelEntity;
import s21.peanutsh.TicTacToe.domain.model.Model;


public class ModelEntityMapper {


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


    public static ModelEntity convertModelToEntity(Model model) {
        return ModelEntity.builder()
                .uuidGame(model.getUuidGame())
                .field(converterFieldToJSJON(model.getField()))
                .bot(model.getPlayingWithComputer())
                .firstPlayer(model.getFirstPlayer())
                .secondPlayer(model.getSecondPlayer())
                .stateGame(model.getStateGame())
                .gameOver(model.getGameOver())
                .createDate(model.getCreateDate())
                .build();
    }


    public static Model convertEntityToModel(ModelEntity modelEntity) {
        return Model.builder()
                .uuidGame(modelEntity.getUuidGame())
                .field(converterStringToField(modelEntity.getField()))
                .firstPlayer(modelEntity.getFirstPlayer())
                .secondPlayer(modelEntity.getSecondPlayer())
                .stateGame(modelEntity.getStateGame())
                .playingWithComputer(modelEntity.getBot())
                .gameOver(modelEntity.getGameOver())
                .createDate(modelEntity.getCreateDate())
                .build();
    }


}
