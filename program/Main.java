package com.company;

import java.io.*;
import java.util.*;

//Program tests nearest neighbor classifier in a specific application
public class Main
{
    /*************************************************************************/

    //number of nearest neighbors
    private static final int NEIGHBORS = 8;
    //Main method
    public static void main(String[] args) throws IOException
    {
        //User for specifying the files
        Scanner scanner=new Scanner(System.in);
        System.out.println("What training file would you like to read? ");
        String inTrainfile=scanner.nextLine();
        System.out.println("What training file would you like to write to? ");
        String outTrainfile=scanner.nextLine();
        System.out.println("What test file would you like to read? ");
        String inTestfile=scanner.nextLine();
        System.out.println("What test file would you like to write to? ");
        String outTestfile=scanner.nextLine();
        System.out.println("What class file would you like to write to? ");
        String classFile=scanner.nextLine();
        //preprocess files
        convertTrainingFile(inTrainfile, outTrainfile);
        convertTestFile(inTestfile, outTestfile);

        //construct nearest neighbor classifier
        NearestNeighbor classifier = new NearestNeighbor();

        //load training data


        classifier.loadTrainingData(outTrainfile);



        //set nearest neighbors
        classifier.setParameters(NEIGHBORS);

        //validate classfier
       classifier.validate();

        //classify test data
        classifier.classifyData(outTestfile, "classifiedfile");

        //postprocess files
        convertClassFile("classifiedfile", classFile,classifier);
    }

    /*************************************************************************/

    //Method converts training file to numerical format
    private static void convertTrainingFile(String inputFile, String outputFile) throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //read number of records, attributes, classes
        int numberRecords = inFile.nextInt();
        int numberAttributes = inFile.nextInt();
        int numberClasses = inFile.nextInt();
        //write number of records, attributes, classes
        outFile.println(numberRecords + " " + numberAttributes + " " + numberClasses);
        //for each record
        for (int i = 0; i < numberRecords; i++)
        {

                    int credit = inFile.nextInt();                      //convert credit score
                    double creditNumber = convertCreditScore(credit);
                    outFile.print(creditNumber + " ");

                    int income = inFile.nextInt();                  //convert income
                    double incomeNumber = convertIncome(income);
                    outFile.print(incomeNumber + " ");

                    int age = inFile.nextInt();                  //convert age
                    double ageNumber = convertAge(age);
                    outFile.print(ageNumber + " ");


                    String gender=inFile.next();                //convert gender
                    double genderNumber=convertGender(gender);
                    outFile.print(genderNumber + " ");

                    String status=inFile.next();                //Convert martial status
                    double statusNumber=convertStatus(status);
                    outFile.print(statusNumber + "   ");


                    String className = inFile.next();                  //convert class name
                    int classNumber = convertClassToNum(className);
                    outFile.println(classNumber);


        }
        inFile.close();
        outFile.close();
    }



    /*************************************************************************/

    //Method converts test file to numerical format
    private static void convertTestFile(String inputFile, String outputFile) throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //read number of records
        int numberRecords = inFile.nextInt();

        //write number of records
        outFile.println(numberRecords);

        //for each record
        for (int i = 0; i <numberRecords; i++)
        {
            int credit = inFile.nextInt();                       //convert credit score
            double creditNumber = convertCreditScore(credit);
            outFile.print(creditNumber + " ");

            int income = inFile.nextInt();                  //convert income
            double incomeNumber = convertIncome(income);
            outFile.print(incomeNumber + " ");

            int age = inFile.nextInt();                  //convert age
            double ageNumber = convertAge(age);
            outFile.print(ageNumber + " ");


            String gender=inFile.next();                //Convert martial status
            double genderNumber=convertGender(gender);
            outFile.print(genderNumber + " ");

            String status=inFile.next();               //Converts status
            double statusNumber=convertStatus(status);
            outFile.print(statusNumber + " \n");
        }

        inFile.close();
        outFile.close();
    }

    /*************************************************************************/

    //Method converts classified file to text format
    private static void convertClassFile(String inputFile, String outputFile,NearestNeighbor n) throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter("output/"+outputFile));

        //read number of records
        int numberRecords = inFile.nextInt();

        //write number of records
        outFile.println(numberRecords);

        //for each record
        for (int i = 0; i < numberRecords; i++)
        {
            int number = inFile.nextInt();                     //convert class number
            String className = convertNumToClass(number);
            outFile.println(className);
        }
        outFile.println();
        outFile.println("validation error: " + n.getErrorRate() + "%");     //Write validation error to file
        outFile.println();
        outFile.println("Nearest Neighbour: "+NEIGHBORS);   //Write nearest Neighbors to file
        inFile.close();
        outFile.close();
    }


    //Method normalizes credit score
    private static double convertCreditScore(double credit)
    {
        return (credit-500)/(900-500);
    }

    //Method normalizes Age
    private static double convertAge(double age)
    {
        return (age-30)/(80-30);
    }

    //Method normalizes Income
    private static double convertIncome(double income)
    {
        return (income-30)/(90-30);
    }


    //Method normalizes class
    private static int convertClassToNum(String className)
    {
        if (className.equals("high"))
            return 3;
        else if (className.equals("medium"))
            return 2;
        else if (className.equals("low"))
            return 1;
        else
            return 4;
    }

    //Method normalizes gender
    private static double convertGender(String gender)
    {
        if(gender.equals("male"))
        {
            return 0.75;
        }
        else{
            return 0.5;
        }
    }

    //Method normalizes martial status
    private static double convertStatus(String status)
    {
        if(status.equals("single"))
        {
            return 0.75;
        }
        else if(status.equals("married")){
            return 0.5;
        }
        else{
            return 0;
        }
    }

    //Method converts class number back to string
    private static String convertNumToClass(int num)
    {
        if (num==3)
            return "high";
        else if (num==2)
            return "medium";
        else if (num==1)
            return "low";
        else
            return "Undetermined";
    }
    /****************************************************************************/
}


