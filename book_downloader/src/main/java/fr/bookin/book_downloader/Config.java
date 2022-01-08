package fr.bookin.book_downloader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class Config {

    // ===== Attributes =====


    /** The config file path */
    private final File configFile;

    /** The only instance of the config */
    private static Config instance = null;

    // --- The configuration attributes

    /** The book provider */
    private String provider;

    /** The list of the wanted language */
    private ArrayList<String> lang;

    /** The path where the books are going to be exported */
    private String exportPath;


    // ===== Constructors =====


    /**
     * Create a new configuration with the user.dir property as base dir
     */
    private Config() {
        this.configFile = new File(System.getProperty("user.dir") + "/config.json");
        try {
            loadConfigFile();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Get the only instance of the configuration
     *
     * @return The only initialised instance of the config
     */
    public static Config getInstance() {
        if(instance == null) instance = new Config();
        return instance;
    }


    // ===== Class methods =====


    /**
     * Load the config file in the config class.
     * If the config file does not exist, generate a new one
     */
    private void loadConfigFile() throws IOException, ParseException {
        // Test if the file exists, if not generate a new one
        if(!configFile.exists()) {

            System.out.println("Create a new config.json file");
            if(!configFile.createNewFile()) {
                throw new IOException("Cannot create the configuration file");
            }
            loadDefaultConfig();
            save();

        } else {

            load();

        }
    }

    /**
     * Set all attributes to their default value
     */
    private void loadDefaultConfig() {
        provider = "";
        lang = new ArrayList<>();
        exportPath = "./";
    }

    /**
     * Save the current config in the configuration file
     *
     * @throws IOException If the file cannot be written
     */
    public void save() throws IOException {
        // Create the config json
        JSONObject configJson = new JSONObject();

        // Write the value in the json
        configJson.put("provider", provider);
        configJson.put("lang", lang);
        configJson.put("exportPath", exportPath);

        // Write the config into the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
        writer.write(configJson.toJSONString());
        writer.close();
    }

    /**
     * Load the config file in the class
     *
     * @throws IOException If the file cannot be read
     * @throws ParseException If the JSON parsing fails
     */
    public void load() throws IOException, ParseException {
        // Open the json file and parse it
        JSONParser parser = new JSONParser();
        JSONObject configJson = (JSONObject) parser.parse(new FileReader(configFile));

        // Get the configuration from the json
        provider = (String) configJson.getOrDefault("provider", "");

        lang = new ArrayList<>();
        JSONArray langArray = (JSONArray) configJson.getOrDefault("lang", new JSONArray());
        lang.addAll(langArray);

        exportPath = (String) configJson.getOrDefault("exportPath", "./");
    }

    /**
     * Display the configuration
     */
    public void printConfig() {
        String builder = "Config file : " + configFile.toString() +
                "\n  provider = " + provider +
                "\n  lang = " + lang.toString() +
                "\n  exportPath = " + exportPath + " (" + new File(exportPath).getAbsolutePath() + ")";

        System.out.println(builder);
    }

}
