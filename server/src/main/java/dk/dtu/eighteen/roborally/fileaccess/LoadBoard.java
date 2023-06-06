package dk.dtu.eighteen.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.eighteen.roborally.controller.IFieldAction;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

import java.io.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "playableBoards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    static public Board loadSavedBoard(String name) {
        return LoadBoard.loadSaveState("savedBoards/" + name);
    }

    static public Board loadNewGameBoard(String name) {
        return LoadBoard.loadSaveState("playableBoards/" + name);
    }

    private static Board loadSaveState(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        //TODO Generalize
        //ClassLoader classLoader = LoadBoard.class.getClassLoader();
        String filename = "./server/src/main/resources/" + boardname;
        File f = new File(filename).getAbsoluteFile();
        String s = f.getAbsolutePath();
        System.out.println(s);
        File file = new File(filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8, 8, "default");
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder()
                .registerTypeAdapter(IFieldAction.class, new Adapter<IFieldAction>());
        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height, boardname);

            for (int i = 0; i < template.spaces.length; i++) {
                for (int j = 0; j < template.spaces[0].length; j++) {
                    Space space = new Space(template.spaces[i][j], result);
                    result.setSpace(i, j, space);
                }
            }

            for (PlayerTemplate pt : template.players) {
                result.addPlayer(new Player(pt, result));
            }


            for (Player player : result.getPlayers()) {
                player.getSpace().setPlayer(player);
            }


            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
        return null;
    }

    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate(board);

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        String filename = "src/main/resources/savedBoards/" + name;

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(IFieldAction.class, new Adapter<IFieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);

            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {
                }
            }
        }
    }

}


