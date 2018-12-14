package be.uclouvain.lingi2252.groupN.parameterization;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelChecker {
    private static final ModelChecker SINGLE_INSTANCE = new ModelChecker();
    private static final String FEATURE_MODEL = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "feature_model.json").toString();
    private List<String> actualFeatures;
    private List<List<String>> minFeatures;
    private Map<String, List<String>> constraints;

    private ModelChecker() {
        actualFeatures = new ArrayList<>();
        minFeatures = new ArrayList<>();
        constraints = new HashMap<>();
    }

    public static ModelChecker getInstance() {
        return SINGLE_INSTANCE;
    }

    private String tolerantFormat(String string) {
        if (string.substring(string.length() - 1).equals("s")) {
            string = string.substring(0, string.length() - 1);
        }
        return string.replaceAll("[ _]", "").toLowerCase();
    }

    public void initialize() {
        File jsonFile = new File(FEATURE_MODEL);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject model = (JSONObject) jsonParser.parse(new FileReader(jsonFile));

            JSONArray minFeatures = (JSONArray) model.get("min_features");
            for (Object features : minFeatures) {
                this.minFeatures.add(new ArrayList<>());
                int i = this.minFeatures.size() - 1;
                JSONArray orFeatures = (JSONArray) features;
                for (Object feature : orFeatures) {
                    String rawFeature = (String) feature;
                    this.minFeatures.get(i).add(tolerantFormat(rawFeature));
                }
            }

            JSONObject constraints = (JSONObject) model.get("implications");
            for (Object constraint : constraints.keySet()) {
                String constraintKey = (String) constraint;
                JSONArray impliedFeatures = (JSONArray) constraints.get(constraintKey);
                this.constraints.put(tolerantFormat(constraintKey), new ArrayList<>());
                for (Object feature : impliedFeatures) {
                    this.constraints.get(tolerantFormat(constraintKey)).add(tolerantFormat((String) feature));
                }
            }

        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("This file [" + FEATURE_MODEL + "] does not exist or is not a valid JSON file!");
        }
    }

    public void addFeature(String feature) {
        this.actualFeatures.add(tolerantFormat(feature));
    }

    public void removeFeature(String feature) {
        this.actualFeatures.remove(tolerantFormat(feature));
    }

    public boolean checkFeatures() {
        for (List<String> clause : this.minFeatures) {
            boolean minIsPresent = false;
            for (String feature : clause) {
                if (actualFeatures.contains(feature)) {
                    minIsPresent = true;
                    break;
                }
            }
            if (!minIsPresent) return false;
        }

        for (Map.Entry<String, List<String>> entry : this.constraints.entrySet()) {
            if (actualFeatures.contains(entry.getKey())) {
                for (String feature : entry.getValue()) {
                    if (!actualFeatures.contains(feature)) return false;
                }
            }
        }

        return true;
    }
}
