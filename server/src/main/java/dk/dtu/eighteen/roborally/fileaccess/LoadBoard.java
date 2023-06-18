package dk.dtu.eighteen.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

import java.io.*;

/**
 * @author Tobias Schønau, s224327@dtu.dk
 * @author Jakob Hansen, s224312@dtu.dk
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "playableBoards";
    private static final String DEFAULTBOARD = "defaultboard";
    public static File saveFolder;

    static public Board loadSavedGameFromFile(String name) throws IOException {
        String filename = getSaveFileAbsolutePath(name);
        File file = new File(filename);
        return LoadBoard.loadSaveStateFromFile(file);
    }

    static public Board loadNewGameFromFile(String name) throws IOException {
        var ins = LoadBoard.class.getResourceAsStream("/" + BOARDSFOLDER + "/" + name);
        return LoadBoard.loadSaveState(ins, name);
    }

    static public Board loadBoardFromJSONString(String data) throws IOException {
        return LoadBoard.loadSaveState(new ByteArrayInputStream(data.getBytes()), DEFAULTBOARD);
    }

    private static Board loadSaveStateFromFile(File boardfile) throws IOException {
        InputStream inputStream = new FileInputStream(boardfile);
        return loadSaveState(inputStream, boardfile.getName());
    }

    private static Board loadSaveState(InputStream inputStream, String boardname) {

        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(IFieldAction.class,
                new Adapter<IFieldAction>());
        Gson gson = simpleBuilder.create();

        Board board;
        JsonReader reader = null;
        try {
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            board = new Board(template.width, template.height, boardname);

            for (int i = 0; i < template.spaces.length; i++) {
                for (int j = 0; j < template.spaces[0].length; j++) {
                    Space space = new Space(template.spaces[i][j], board);
                    board.setSpace(i, j, space);
                }
            }

            for (PlayerTemplate pt : template.players) {
                board.addPlayer(new Player(pt, board));
            }

            board.turn = template.turn;

            board.setPhase(template.phase);

            reader.close();
            return board;
        } catch (IOException e1) {
            try {
                reader.close();
                inputStream = null;
            } catch (IOException ignored) {
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate(board);

        String filename = getSaveFileAbsolutePath(name);

        GsonBuilder simpleBuilder = new GsonBuilder()
                .registerTypeAdapter(IFieldAction.class, new Adapter<IFieldAction>()).setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        try (FileWriter fileWriter = new FileWriter(filename); JsonWriter writer = gson.newJsonWriter(fileWriter)) {
            gson.toJson(template, template.getClass(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSaveFileAbsolutePath(String name) {
        return saveFolder.getAbsolutePath() + "/" + name;
    }
}
