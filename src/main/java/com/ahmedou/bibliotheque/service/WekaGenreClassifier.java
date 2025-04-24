package com.ahmedou.bibliotheque.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WekaGenreClassifier {

    private NaiveBayes classifier;
    private Instances dataset;
    private StringToWordVector filter;
    private final List<String> genreVals = Arrays.asList(
            "Fantastique", "Policier", "Science-fiction", "Romance", "Thriller", "Biographie", "Histoire",
            "Philosophie", "Développement personnel", "Jeunesse", "Poésie", "Essai", "Humour", "Psychologie", "Roman"
    );

    public WekaGenreClassifier() throws Exception {
        initModel("livres.csv");
    }

    private void initModel(String filename) throws Exception {
        // Définir les attributs
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("titre", (List<String>) null));
        attributes.add(new Attribute("description", (List<String>) null));
        attributes.add(new Attribute("genre", new ArrayList<>(genreVals)));

        dataset = new Instances("Livres", attributes, 0);
        dataset.setClassIndex(2);

        File modelFile = new File("weka_model.model");
        File filterFile = new File("weka_filter.model");

        if (modelFile.exists() && filterFile.exists()) {
            classifier = (NaiveBayes) weka.core.SerializationHelper.read("weka_model.model");
            filter = (StringToWordVector) weka.core.SerializationHelper.read("weka_filter.model");
            System.out.println("✅ Modèle et filtre chargés depuis fichiers.");
        } else {
            loadFromCSV(filename);
            filter = new StringToWordVector();
            filter.setInputFormat(dataset);
            Instances filteredData = Filter.useFilter(dataset, filter);
            classifier = new NaiveBayes();
            classifier.buildClassifier(filteredData);
            weka.core.SerializationHelper.write("weka_model.model", classifier);
            weka.core.SerializationHelper.write("weka_filter.model", filter);
            System.out.println("✅ Modèle entraîné et sauvegardé.");
        }
    }

    public void loadFromCSV(String filename) throws Exception {
        ClassPathResource resource = new ClassPathResource(filename);
        Set<String> genresTrouves = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String genre = parts[2].trim();
                    genresTrouves.add(genre);
                    addExample(parts[0].trim(), parts[1].trim(), genre);
                }
            }
        }

        System.out.println("📚 Genres trouvés dans le fichier : " + genresTrouves);
    }

    private void addExample(String titre, String description, String genre) {
        int genreIndex = dataset.attribute(2).indexOfValue(genre);
        if (genreIndex == -1) {
            System.err.println("⚠️ Genre inconnu ignoré : " + genre);
            return;
        }

        double[] vals = new double[3];
        vals[0] = dataset.attribute(0).addStringValue(titre);
        vals[1] = dataset.attribute(1).addStringValue(description);
        vals[2] = genreIndex;
        dataset.add(new DenseInstance(1.0, vals));
    }

    public String predict(String titre, String description) throws Exception {
        Instance instance = new DenseInstance(3);
        instance.setDataset(dataset);
        instance.setValue(0, dataset.attribute(0).addStringValue(titre));
        instance.setValue(1, dataset.attribute(1).addStringValue(description));

        Instances testSet = new Instances(dataset, 0);
        testSet.add(instance);
        Instances filteredTest = Filter.useFilter(testSet, filter);

        double predictionIndex = classifier.classifyInstance(filteredTest.instance(0));
        return dataset.classAttribute().value((int) predictionIndex);
    }

    public synchronized void reloadModelFromCsv(String filename) throws Exception {
        dataset.clear();
        loadFromCSV(filename);
        filter = new StringToWordVector();
        filter.setInputFormat(dataset);
        Instances filteredData = Filter.useFilter(dataset, filter);
        classifier = new NaiveBayes();
        classifier.buildClassifier(filteredData);
        weka.core.SerializationHelper.write("weka_model.model", classifier);
        weka.core.SerializationHelper.write("weka_filter.model", filter);
        System.out.println("♻️ Nouveau modèle rechargé et sauvegardé.");
    }
}
