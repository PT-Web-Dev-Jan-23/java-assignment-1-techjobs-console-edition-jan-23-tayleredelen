import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column).toLowerCase(); //added .toLowerCase()

            if (aValue.contains(value.toLowerCase())) { //added .toLowerCase()
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */

    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        loadData();
        ArrayList<HashMap<String, String>> results = new ArrayList<>();
        for (HashMap<String, String> job : allJobs) {
            for (Map.Entry<String, String> entry : job.entrySet()) {
                if (entry.getValue().toLowerCase().contains(value.toLowerCase())) {
                    results.add(job);
                    break;
                }
            }
        }
        return results;
    }


//pseudo coded code
//    public static ArrayList<HashMap<String, String>> findByValue(String value) {
//        //method takes string of searched term (results) & returns array of HashMaps (key/value pairs) that contain searched term
//        loadData();
//        // make sure data is loaded
//        ArrayList<HashMap<String, String>> results = new ArrayList<>();
//        // initialize empty ArrayList to store matching jobs
////
////
////        HashSet<String> jobSet = new HashSet<>(); //not working //initialize empty HashSet to keep track of jobs already added (avoiding dupes)
//        for (HashMap<String, String> job : allJobs) {
//            // i over all jobs in the allJobs list
//            for (Map.Entry<String, String> entry : job.entrySet()) {
//                // i over all the keys (column names) in the job HashMap
//                if (entry.getValue().toLowerCase().contains(value.toLowerCase())) {//isn't working :(
//                    // convert value & search term to lowercase to create case insensitivity
//                        results.add(job);
//                        // if conditions met job added
////                    jobSet.add(job.get("ID")); //not working//and it's ID is added to set of jobs that have already been added
//                    break;
//                    //break out of inner loop cuz we don't need to check remaining columns for job
//                    }
//                }
//            }
//        return results;
//        //returns list of matching jobs
//    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace(); // e = error syntax
        }
    }

}
