package com.company;

import java.io.*;
import java.util.*;

//Nearest neighbor classifier
public class NearestNeighbor
{
    /*************************************************************************/
    private int classs;
    private double [] attributess;
    //Record class (inner class)
    private class Record
    {
        private double[] attributes;         //attributes of record
        private int className;               //class of record

        //Constructor of Record
        private Record(double[] attributes, int className)
        {
            this.attributes = attributes;    //set attributes
            this.className = className;      //set class
        }
        //getters are being used
        public int getClassName(){return className;}
        public double[] getAttributes(){return attributes;}
    }

    /*************************************************************************/
    ArrayList<Record> trainingRecordsValidation =new ArrayList<>(); //training records used for validation
    private double errorRate;
    private int numberRecords;               //number of training records
    private int numberAttributes;            //number of attributes
    private int numberClasses;               //number of classes
    private int numberNeighbors;             //number of nearest neighbors
    private ArrayList<Record> records;       //list of training records

    /*************************************************************************/

    //Constructor of NearestNeighbor
    public NearestNeighbor()
    {
        //initial data is empty
        numberRecords = 0;
        numberAttributes = 0;
        numberClasses = 0;
        numberNeighbors = 0;
        records = null;
    }

    /*************************************************************************/

    //Method loads data from training file
    public void loadTrainingData(String trainingFile) throws IOException
    {
        Scanner inFile = new Scanner(new File(trainingFile));

        //read number of records, attributes, classes
        numberRecords = inFile.nextInt();
        numberAttributes = inFile.nextInt();
        numberClasses = inFile.nextInt();

        //create empty list of records
        records = new ArrayList<Record>();

        //for each record
        for (int i = 0; i < numberRecords; i++)
        {
            //create attribute array
            double[] attributeArray = new double[numberAttributes];

            //read attribute values
                for (int j = 0; j < numberAttributes; j++)
                    attributeArray[j] = inFile.nextDouble();

                //read class name
                int className = inFile.nextInt();

                //create record
                Record record = new Record(attributeArray, className);

                //add record to list of records
                records.add(record);

        }

        inFile.close();
    }

    /*************************************************************************/

    //Method sets number of nearest neighbors
    public void setParameters(int numberNeighbors)
    {
        this.numberNeighbors = numberNeighbors;
    }

    /*************************************************************************/

    //Method reads records from test file, determines their classes,
    //and writes classes to classified file
    public void classifyData(String testFile, String classifiedFile) throws IOException
    {
        Scanner inFile = new Scanner(new File(testFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));

        //read number of records
        int numberRecords = inFile.nextInt();

        //write number of records
        outFile.println(numberRecords);

        //for each record
        for (int i = 0; i < numberRecords; i++)
        {
            //create attribute array
            double[] attributeArray = new double[numberAttributes];

            //read attribute values
            for (int j = 0; j < numberAttributes; j++)
                attributeArray[j] = inFile.nextDouble();

            //find class of attributes
            int className = classify(attributeArray,records,this.numberRecords);

            //write class name
            outFile.println(className);
        }

        inFile.close();
        outFile.close();
    }

    /*************************************************************************/

    //Method determines the class of a set of attributes
    private int classify(double[] attributes,ArrayList<Record> records,int size)
    {
        double[] distance = new double[numberRecords];
        int[] id = new int[numberRecords];
        //System.out.println(size);
        //find distances between attributes and all records
        for (int i = 0; i < size; i++)
        {
            distance[i] = distance(attributes, records.get(i).attributes);
            id[i] = i;
        }

        //find nearest neighbors
        nearestNeighbor(distance, id);

        //find majority class of nearest neighbors
        int className = majority(id,records);

        //return classrecords
        return className;
    }

    /*************************************************************************/

    //Method finds the nearest neighbors
    private void nearestNeighbor(double[] distance, int[] id)
    {
        //sort distances and choose nearest neighbors
        for (int i = 0; i < numberNeighbors; i++)
            for (int j = i; j < numberRecords; j++)
                if (distance[i] > distance[j])
                {
                    double tempDistance = distance[i];
                    distance[i] = distance[j];
                    distance[j] = tempDistance;

                    int tempId = id[i];
                    id[i] = id[j];
                    id[j] = tempId;
                }
    }

    /*************************************************************************/

    //Method finds the majority class of nearest neighbors
    private int majority(int[] id,ArrayList <Record> records)
    {
        double[] frequency = new double[numberClasses];

        //class frequencies are zero initially
        for (int i = 0; i < numberClasses; i++)
            frequency[i] = 0;

        //each neighbor contributes 1 to its class
        for (int i = 0; i < numberNeighbors; i++)
            frequency[records.get(id[i]).className - 1] += 1;

        //find majority class
        int maxIndex = 0;
        for (int i = 0; i < numberClasses; i++)
            if (frequency[i] > frequency[maxIndex])
                maxIndex = i;

        return maxIndex + 1;
    }

    /*************************************************************************/

    //Method finds Euclidean distance between two points
    private double distance(double[] u, double[] v)
    {
        double distance = 0;

        for (int i = 0; i < u.length; i++)
            distance = distance + (u[i] - v[i])*(u[i] - v[i]);

        distance = Math.sqrt(distance);

        return distance;
    }

    /*************************************************************************/

    //Method validates classifier using leave one out method and displays error rate
    public void validate() throws IOException
    {
        ArrayList<Record> tempRecords;

        //Training records are copied to temp
        tempRecords = records;

        //read number of records
        //initially zero errors
        int numberErrors = 0;

        for(int i=0; i<numberRecords;i++)
        {
            //Nested loop used
            for( int j=0; j<numberRecords;j++)
            {
                //Leave one out if statement
               if(i==j)
               {
                   //Record for validation is being extracted
                   helpervalidation(tempRecords.get(j),i);
               }
               else {
                   //The rest of the records are used for training
                  trainingRecordsValidation.add(records.get(j));
               }

            }

            //Class for validation is recorded
            int actualClass = classs;
            int predictedClass = classify(attributess, trainingRecordsValidation,numberRecords-1); //Algorithm will attempt to find class
            if (predictedClass != actualClass)  //Classes are compared
                numberErrors += 1;
            trainingRecordsValidation.clear(); //training records is cleared and will be user for next iteration
        }
        //for each record

        //find and print error rate
        double errorRate = 100.0*numberErrors/numberRecords-1;
        this.errorRate=errorRate;
        System.out.println("validation error: " + errorRate + "%");

    }

    //Helper method used for validation
    public void helpervalidation(Record record, int index)
    {
        //Validation record attributes and class are recorded
        classs=record.getClassName();

        attributess=record.getAttributes();

    }

    //getter for error rate
    public double getErrorRate()
    {
        return errorRate;
    }

    /************************************************************************/
}

