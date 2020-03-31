package pl.obiektowe.projekt1.simulator.DataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class StartParameters {
    private static StartParameters instance = new StartParameters();
    private static String filename = "parameters.json";
    private int width;
    private int height;
    private int jungleRatio;
    private int moveEnergy;
    private int startEnergy;
    private int plantEnergy;
    private int numberOfAnimalsToSpawn;
    private int refreshingTime;

    public StartParameters() {
    }

    public static StartParameters getInstance() {
        return instance;
    }

    public void loadStartParameters() throws IOException, ParseException {
        File file = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        Object obj = (new JSONParser()).parse(new FileReader(file));
        JSONObject jo = (JSONObject) obj;
        this.width = (int) (long) jo.get("width");
        this.height = (int) (long) jo.get("height");
        this.jungleRatio = (int) (long) jo.get("jungleRatio");
        this.moveEnergy = (int) (long) jo.get("moveEnergy");
        this.startEnergy = (int) (long) jo.get("startEnergy");
        this.plantEnergy = (int) (long) jo.get("plantEnergy");
        this.numberOfAnimalsToSpawn = (int) (long) jo.get("numberOfAnimalsToSpawn");
        this.refreshingTime = (int) (long) jo.get("refreshingTime");
    }

    public void storeStartParameters() throws FileNotFoundException {
        File file = new File(this.getClass().getClassLoader().getResource(filename).getFile());
        JSONObject jo = new JSONObject();
        jo.put("width", this.width);
        jo.put("height", this.height);
        jo.put("jungleRatio", this.jungleRatio);
        jo.put("moveEnergy", this.moveEnergy);
        jo.put("startEnergy", this.startEnergy);
        jo.put("plantEnergy", this.plantEnergy);
        jo.put("numberOfAnimalsToSpawn", this.numberOfAnimalsToSpawn);
        jo.put("refreshingTime", this.refreshingTime);
        PrintWriter pw = new PrintWriter(file);
        pw.write(jo.toJSONString());
        pw.flush();
        pw.close();
    }

    public void setParameter(int value, String name) {
        switch (name) {
            case "width":
                this.setWidth(value);
                break;
            case "height":
                this.setHeight(value);
                break;
            case "jungleRatio":
                this.setJungleRatio(value);
                break;
            case "moveEnergy":
                this.setMoveEnergy(value);
                break;
            case "startEnergy":
                this.setStartEnergy(value);
                break;
            case "plantEnergy":
                this.setPlantEnergy(value);
                break;
            case "numberOfAnimalsToSpawn":
                this.setNumberOfAnimalsToSpawn(value);
                break;
            case "refreshingTime":
                this.setRefreshingTime(value);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getJungleRatio() {
        return this.jungleRatio;
    }

    public int getMoveEnergy() {
        return this.moveEnergy;
    }

    public int getStartEnergy() {
        return this.startEnergy;
    }

    public int getPlantEnergy() {
        return this.plantEnergy;
    }

    public int getNumberOfAnimalsToSpawn() {
        return this.numberOfAnimalsToSpawn;
    }

    public int getRefreshingTime() {
        return this.refreshingTime;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setJungleRatio(int jungleRatio) {
        this.jungleRatio = jungleRatio;
    }

    public void setMoveEnergy(int moveEnergy) {
        this.moveEnergy = moveEnergy;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public void setPlantEnergy(int plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public void setNumberOfAnimalsToSpawn(int numberOfAnimalsToSpawn) {
        this.numberOfAnimalsToSpawn = numberOfAnimalsToSpawn;
    }

    public void setRefreshingTime(int refreshingTime) {
        this.refreshingTime = refreshingTime;
    }
}

