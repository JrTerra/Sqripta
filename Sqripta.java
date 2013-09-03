import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.io.File;
import java.util.ArrayList;

public class Sqripta
{
   public static void main(String[] args) throws IOException
   {

      File inputFile = new File("/home/land/Desktop/input.txt");        //open input file
      if (!inputFile.exists()) {
         System.out.println("Input file not found here. Maybe path in source code incorrect?");
         System.exit(0);
      }
      Scanner input = new Scanner(inputFile);                             
      String[] lines = new String[567];                                   //populate string array from input file
      for(int i=0;i<=565;i++) {
         lines[i]=input.nextLine();
      }

      Scanner keyboard = new Scanner(System.in);
      Random gen = new Random();
//determine number of statements to write
      int numberStatements = 0;
      do {
         System.out.print("Number of statements to write(integer): ");
         numberStatements = keyboard.nextInt();
      } while (numberStatements<1 || numberStatements>3000000);
      int statementCount=0;

      keyboard.nextLine();
//determine type of statements to write
      String dbDropper1="";
      String dbDropper2="";
      do {
         System.out.print("Exclude dangerous statements(y/n): ");
         dbDropper1 = keyboard.nextLine();
         dbDropper2=dbDropper1.toLowerCase();
      } while (dbDropper2.charAt(0)!='y' && dbDropper2.charAt(0)!='n');

//determine include attacks
      boolean attacksOn=false;
      int numberAttacks=0;
      int attackCount=0;
      int attackInterval=0;
      String attackInput1="";
      String attackInput2="";
      do {
         System.out.print("Include attacks(y/n): ");
         attackInput1=keyboard.nextLine();
         attackInput2=attackInput1.toLowerCase();
      } while (attackInput2.charAt(0)!='y' && attackInput2.charAt(0)!='n');

      if (attackInput2.charAt(0)=='y') {
         attacksOn=true;
         do {
            System.out.print("Number of attacks to intersperse(integer): ");
            numberAttacks=keyboard.nextInt();
         } while (numberAttacks<1 || numberAttacks>numberStatements);
         attackInterval=numberStatements/numberAttacks;
         keyboard.nextLine();
      }
      else {
         attacksOn=false;
      }

      System.out.print("Enter name of DB: ");
      String dbName=keyboard.nextLine();
      boolean attackDesperation=false;
      int attackProb=0;


      PrintWriter writer = new PrintWriter(numberStatements+"_"+"attacks_"+attackInput2+".sql");

      writer.println("USE ["+dbName+"]");
      writer.println("GO");


      for(int j=0;j<numberStatements;j++)
      {
         attackDesperation=false;
         if ((attackCount>=numberAttacks) && attacksOn) {
            attacksOn=false;
         }
         if (attacksOn) {
            if (((numberStatements-statementCount)/(numberAttacks-attackCount))<attackInterval) {
               attackDesperation=true;
            }
         }
         int statementType=0;
         if (dbDropper2.charAt(0)=='y') {
            statementType = gen.nextInt(25);
         }
         else {
            statementType = gen.nextInt(33);
         }
         int num = gen.nextInt(566);                                //pick random line from input text
         String line = lines[num];                                  //write random line to local variable

//avoiding drop DB statements
         if(statementType==32) {
            int reRoll=gen.nextInt(1001);
            if(reRoll!=0) {
               statementType=gen.nextInt(32);
            }
         }
         switch(statementType)
         {
            case 0:
            {
//need table1,column1,column2,two values,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);


//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }


//datatyping
               placer=99;
               char dataType = line.charAt(placer);


//adding "NOT" clause
               int notId=gen.nextInt(2);
               String not="";
               if (notId==0) {
                  not=" NOT";
               }

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);


//getting random values
               String value1="";
               String value2="";
               switch(dataType)
               {
                  case 'n':
                  {
                     final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
                     int alphaLength=ALPHABET.length();
                     value1=""+ALPHABET.charAt(gen.nextInt(alphaLength));
                     value2=""+ALPHABET.charAt(gen.nextInt(alphaLength));
                     break;
                  }
                  case 's':
                  {
                     value1=""+gen.nextInt(32768);
                     value2=""+gen.nextInt(32768);
                     break;
                  }
                  case 'i':
                  {
                     value1=""+gen.nextInt(1000);
                     value2=""+gen.nextInt(1000);
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        value1=""+((double)gen.nextInt(10000))/100.0;
                        value2=""+((double)gen.nextInt(10000))/100.0;
                     }
                     else {
                        int year1=gen.nextInt(64) + 1950;
                        int month1=gen.nextInt(12) + 1;
                        int day1;
                        if (month1==1||month1==3||month1==5||month1==7||month1==8||month1==10||month1==12) {
                           day1=gen.nextInt(31)+1;
                        }
                        else if (month1==2 && year1%4==0) {
                           day1=gen.nextInt(29)+1;
                        }
                        else if (month1==2 && year1%4!=0) {
                           day1=gen.nextInt(28)+1;
                        }
                        else {
                           day1=gen.nextInt(30)+1;
                        }
                        int year2=gen.nextInt(64) + 1950;
                        int month2=gen.nextInt(12) + 1;
                        int day2;
                        if (month2==1||month2==3||month2==5||month2==7||month2==8||month2==10||month2==12) {
                           day2 = gen.nextInt(31) + 1;
                        }
                        else if (month2==2 && year2%4==0) {
                           day2=gen.nextInt(29)+1;
                        }
                        else if (month2==2 && year2%4!=0) {
                           day2=gen.nextInt(28)+1;
                        }
                        else {
                           day2=gen.nextInt(30)+1;
                        }
                        value1=year1+"-"+month1+"-"+day1;
                        value2=year2+"-"+month2+"-"+day2;
                     }
                     break;
                  }
                  case 'm':
                  {
                     value1=""+((double)gen.nextInt(10000))/100.0;
                     value2=""+((double)gen.nextInt(10000))/100.0;
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        int hour1=gen.nextInt(24);
                        int minute1=gen.nextInt(60);
                        int hour2=gen.nextInt(24);
                        int minute2=gen.nextInt(60);
                        value1=hour1+":"+minute1;
                        value2=hour2+":"+minute2;
                     }
                     else {
                        value1=""+gen.nextInt(256);
                        value2=""+gen.nextInt(256);
                     }
                     break;
                  }
               }

//schema
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"] FROM ["+schema1+"].["+table1+"] WHERE ["+column2+"]"+attack+not+" BETWEEN "+value1+" AND "+value2+";");
               writer.println("GO");

               break;
            }
            case 1:
            {
//need table,column1,column2,twovalues,schema1

//table1               
               int placer=34;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//column1
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }


//adding "NOT" clause
               int notId=gen.nextInt(2);
               String not="";
               if (notId==0) {
                  not=" NOT";
               }

//datatyping
               placer=99;
               char dataType = line.charAt(placer);

//generate new line to get second column

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//getting random values
               String value1="";
               String value2="";
               switch(dataType)
               {
                  case 'n':
                  {
//pattern
                     final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
                     int alphaLength=ALPHABET.length();
                     value1=""+ALPHABET.charAt(gen.nextInt(alphaLength));
                     value2=""+ALPHABET.charAt(gen.nextInt(alphaLength));
                     break;
                  }
                  case 's':
                  {
                     value1=""+gen.nextInt(32768);
                     value2=""+gen.nextInt(32768);
                     break;
                  }
                  case 'i':
                  {
                     value1=""+gen.nextInt(1000);
                     value2=""+gen.nextInt(1000);
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        value1=""+((double)gen.nextInt(10000))/100.0;
                        value2=""+((double)gen.nextInt(10000))/100.0;
                     }
                     else {
                        int year1=gen.nextInt(64) + 1950;
                        int month1=gen.nextInt(12) + 1;
                        int day1;
                        if (month1==1||month1==3||month1==5||month1==7||month1==8||month1==10||month1==12) {
                           day1 = gen.nextInt(31) + 1;
                        }
                        else if (month1==2 && year1%4==0) {
                           day1=gen.nextInt(29)+1;
                        }
                        else if (month1==2 && year1%4!=0) {
                           day1=gen.nextInt(28)+1;
                        }
                        else {
                           day1=gen.nextInt(30)+1;
                        }
                        int year2=gen.nextInt(64) + 1950;
                        int month2=gen.nextInt(12) + 1;
                        int day2;
                        if (month2==1||month2==3||month2==5||month2==7||month2==8||month2==10||month2==12) {
                           day2 = gen.nextInt(31) + 1;
                        }
                        else if (month2==2 && year2%4==0) {
                           day2=gen.nextInt(29)+1;
                        }
                        else if (month2==2 && year2%4!=0) {
                           day2=gen.nextInt(28)+1;
                        }
                        else {
                           day2=gen.nextInt(30)+1;
                        }
                        value1=year1+"-"+month1+"-"+day1;
                        value2=year2+"-"+month2+"-"+day2;
                     }
                     break;
                  }
                  case 'm':
                  {
                     value1=""+((double)gen.nextInt(10000))/100.0;
                     value2=""+((double)gen.nextInt(10000))/100.0;
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        int hour1=gen.nextInt(24);
                        int minute1=gen.nextInt(60);
                        int hour2=gen.nextInt(24);
                        int minute2=gen.nextInt(60);
                        value1=hour1+":"+minute1;
                        value2=hour2+":"+minute2;
                     }
                     else {
                        value1=""+gen.nextInt(256);
                        value2=""+gen.nextInt(256);
                     }
                     break;
                  }
                  
               }

//schema
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"] FROM ["+schema1+"].["+table1+"] WHERE ["+column2+"]"+attack+not+" IN ("+value1+","+value2+");");
               writer.println("GO");

               break;
            }
            case 2:
            {
//need table1,column1,column2,pattern,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//adding "NOT" clause
               int notId=gen.nextInt(2);
               String not="";
               if (notId==0) {
                  not=" NOT";
               }

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }


//generate new line to get new column

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2

               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//pattern
               final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
               int alphaLength=ALPHABET.length();
               String pattern="\'%"+(ALPHABET.charAt(gen.nextInt(alphaLength)))+"\'";

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"] FROM ["+schema1+"].["+table1+"] WHERE "+column2+"]"+attack+not+" LIKE "+pattern+";");
               writer.println("GO");

               break;
            }

            case 3:
            {
//need table1,column1,column2,schema1,pick ASC/DESC

//table1               
               int placer=34;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//column1
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);


//generate new line to get second column

               int num2=gen.nextInt(566);
               String line2=lines[num2];


//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//generate new line to get third column

               int num3=gen.nextInt(566);
               String line3=lines[num3];

//column3
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line3.charAt(placer)==' ' || line3.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line3.charAt(placer));
                  placer++;
               }
               String column3 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//pick ASC/DESC
               int ascDesc=gen.nextInt(2);
               String ordering="";
               if (ascDesc==0) {
                  ordering="ASC";
               }
               else {
                  ordering="DESC";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"] FROM ["+schema1+"].["+table1+attack+"] ORDER BY ["+column3+"],["+column2+"] "+ordering+";");
               writer.println("GO");

               break;
            }
            case 4:
            {
//need column1, column2,format,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }


//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"], ["+column2+attack+"], FORMAT(Now(),\'YYYY-MM-DD\') FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 5:
            {
//need table1,column1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"] NOW() FROM ["+schema1+"].["+table1+attack+"];");
               writer.println("GO");

               break;
            }
            case 6:
            {
//need column1,table1,column2,#ofdecimals,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//numdecimals
               int numDecimals=gen.nextInt(3);

//write
               writer.println("SELECT ["+column1+attack+"], ROUND(["+column2+"],"+numDecimals+") FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 7:
            {
//need column1,table1,column2,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }


//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT ["+column1+"], LEN(["+column2+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 8:
            {
//need column1,lengthvalues,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval-1);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//start
               int starter=gen.nextInt(100)+1;

//length
               int lengthFinder=gen.nextInt(100)+1;

//write
               writer.println("SELECT MID(["+column1+"],"+starter+","+lengthFinder+") FROM ["+schema1+"].["+table1+attack+"];");
               writer.println("GO");

               break;
            }
            case 9:
            {
//need column1,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT LOWER(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 10:
            {
//need column1,table1,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT UPPER(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 11:
            {
//need column1,table1,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT SUM(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 12:
            {
//need column1,table1,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT MIN(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 13:
            {
//need column1,table1,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT MAX(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 14:
            {
//need column1,table1,column2,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT TOP 1 ["+column1+attack+"] FROM ["+schema1+"].["+table1+"] ORDER BY ["+column2+"] DESC;");
               writer.println("GO");

               break;
            }
            case 15:
            {
//need column1,table1,column2,schema1


//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                           //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT TOP 1 ["+column1+attack+"] FROM ["+schema1+"].["+table1+"] ORDER BY ["+column2+"] ASC;");
               writer.println("GO");

               break;
            }
            case 16:
            {
//need column1,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT COUNT(DISTINCT ["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 17:
            {
//need column1,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT AVG(["+column1+attack+"]) FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 18:
            {
//need column1,table1,table2,schema1,schema2

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//table2               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line2.charAt(placer)==' '||line2.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String table2 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//schema2
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line2.charAt(placer)==' '||line2.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String schema2=getString(charList);

//write
               writer.println("SELECT ["+column1+attack+"] INTO ["+schema2+"].["+table2+"] FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 19:
            {
//need column1,table1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("SELECT DISTINCT ["+column1+attack+"] FROM ["+schema1+"].["+table1+"];");
               writer.println("GO");

               break;
            }
            case 20:
            {
//need table1,column1,column2,column3,somepattern,somevalue,somecolumn=somevalue,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//datatyping
               placer=99;
               char dataType = line.charAt(placer);

//value1
               String value1="";
               if(dataType=='n') {
                  final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
                  int alphaLength=ALPHABET.length();
                  value1=""+ALPHABET.charAt(gen.nextInt(alphaLength));
               }
               else if(dataType=='s') {
                  value1=""+gen.nextInt(32768);
               }
               else if(dataType=='i') {
                  value1=""+gen.nextInt(1000);
               }
               else if(dataType=='d') {
                  if(line.charAt(placer+1)=='e') {
                     value1=""+((double)gen.nextInt(10000))/100.0;
                  }
                  else {
                     int year1=gen.nextInt(64) + 1950;
                     int month1=gen.nextInt(12) + 1;
                     int day1;
                     if (month1==1||month1==3||month1==5||month1==7||month1==8||month1==10||month1==12) {
                        day1 = gen.nextInt(31) + 1;
                     }
                     else if (month1==2 && year1%4==0) {
                        day1=gen.nextInt(29)+1;
                     }
                     else if (month1==2 && year1%4!=0) {
                        day1=gen.nextInt(28)+1;
                     }
                     else {
                        day1=gen.nextInt(30)+1;
                     }
                     value1=year1+"-"+month1+"-"+day1;
                  }
               }
               else if(dataType=='m') {
                  value1=""+((double)gen.nextInt(10000))/100.0;
               }
               else if(dataType=='t') {
                  if(line.charAt(placer+2)=='m') {
                     int hour1=gen.nextInt(24);
                     int minute1=gen.nextInt(60);
                     value1=hour1+":"+minute1;
                  }
                  else {
                     value1=""+gen.nextInt(256);
                  }
               }


//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//datatyping
               placer=99;
               char dataType2 = line2.charAt(placer);

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//value2
               String value2="";
               if(dataType2=='n') {
                  final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
                  int alphaLength=ALPHABET.length();
                  value2=""+ALPHABET.charAt(gen.nextInt(alphaLength));
               }
               else if(dataType2=='s') {
                  value2=""+gen.nextInt(32768);
               }
               else if(dataType2=='i') {
                  value2=""+gen.nextInt(1000);
               }
               else if(dataType2=='d') {
                  if(line.charAt(placer+1)=='e') {
                     value2=""+((double)gen.nextInt(10000))/100.0;
                  }
                  else {
                     int year2=gen.nextInt(64) + 1950;
                     int month2=gen.nextInt(12) + 1;
                     int day2;
                     if (month2==1||month2==3||month2==5||month2==7||month2==8||month2==10||month2==12) {
                        day2 = gen.nextInt(31) + 1;
                     }
                     else if (month2==2 && year2%4==0) {
                        day2=gen.nextInt(29)+1;
                     }
                     else if (month2==2 && year2%4!=0) {
                        day2=gen.nextInt(28)+1;
                     }
                     else {
                        day2=gen.nextInt(30)+1;
                     }
                     value1=year2+"-"+month2+"-"+day2;
                  }
               }
               else if(dataType2=='m') {
                  value2=""+((double)gen.nextInt(10000))/100.0;
               }
               else if(dataType2=='t') {
                  if(line.charAt(placer+2)=='m') {
                     int hour2=gen.nextInt(24);
                     int minute2=gen.nextInt(60);
                     value2=hour2+":"+minute2;
                  }
                  else {
                     value2=""+gen.nextInt(256);
                  }
               }

//generate new line to get new column/table

               int num3=gen.nextInt(566);
               String line3=lines[num3];

//column3
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                           //loop to populate charList
                  if(line3.charAt(placer)==' ' || line3.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line3.charAt(placer));
                  placer++;
               }
               String column3 = getString(charList);

//pattern
               final String ALPHABET="0123456689abcdefghijklmnopqrstuvwxyz";
               int alphaLength=ALPHABET.length();
               String pattern="\'%"+(ALPHABET.charAt(gen.nextInt(alphaLength)))+"\'";

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("UPDATE ["+schema1+"].["+table1+"] SET ["+column1+"]=\'"+value1+"\', ["+column2+"]=\'"+value2+"\' WHERE ["+column3+attack+"] LIKE "+pattern+";");
               writer.println("GO");

               break;
            }
            case 21:
            {
//need column1,table1,column2,value1,value2,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//column2
               placer = 68;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line2.charAt(placer)==' ' || line2.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String column2 = getString(charList);

//values to insert
               String value1="something1";
               String value2="something2";

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("INSERT INTO ["+schema1+"].["+table1+"] (["+column1+"],["+column2+attack+"]) VALUES (\'"+value1+"\', \'"+value2+"\');");
               writer.println("GO");

               break;
            }
            case 22:
            {
//need column1,table1,table2,schema1,schema2

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//generate new line to get new column/table

               int num2=gen.nextInt(566);
               String line2=lines[num2];

//table2               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line2.charAt(placer)==' '||line2.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String table2 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//schema2
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line2.charAt(placer)==' '||line2.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line2.charAt(placer));
                  placer++;
               }
               String schema2=getString(charList);

//write
               writer.println("INSERT INTO ["+schema1+"].["+table1+"] SELECT ["+column1+attack+"] FROM ["+schema2+"].["+table2+"];");
               writer.println("GO");

               break;
            }

            case 23:
            {
//need table1,column1,datatype,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//datatyping
               placer=99;
               char dataTypeInput = line.charAt(placer);

//values
               String dataType1="";
               switch(dataTypeInput)
               {
                  case 'n':
                  {
                     dataType1="nvarchar";
                     break;
                  }
                  case 's':
                  {
                     dataType1="smallint";
                     break;
                  }
                  case 'i':
                  {
                     dataType1="integer";
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        dataType1="decimal";
                     }
                     else {
                        dataType1="date";
                     }
                     break;
                  }
                  case 'm':
                  {
                     dataType1="money";
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        dataType1="time";
                        break;
                     }
                     else {
                        dataType1="tinyint";
                        break;
                     }
                  }
               }

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("ALTER TABLE ["+schema1+"].["+table1+"] ALTER COLUMN ["+column1+attack+"] "+dataType1+";");
               writer.println("GO");

               break;
            }
            case 24:
            {
//need indexname,table1,column1,schema1

//index
               String index1="PIndex";

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("CREATE INDEX ["+index1+"] ON ["+schema1+"].["+table1+"](["+column1+attack+"]);");
               writer.println("GO");

               break;
            }
            case 25:
            {
//need table1,somecolumn=somevalue,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//some value
//datatyping
               placer=99;
               char dataTypeInput = line.charAt(placer);

//values
               String value1="";
               switch(dataTypeInput)
               {
                  case 'n':
                  {
                     value1="number";
                     break;
                  }
                  case 's':
                  {
                     value1=""+gen.nextInt(32768);
                     break;
                  }
                  case 'i':
                  {
                     value1=""+gen.nextInt(1000);
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        value1=""+((double)gen.nextInt(10000))/100.0;
                     }
                     else {
                        int year=gen.nextInt(64) + 1950;
                        int month=gen.nextInt(12) + 1;
                        int day;
                        String month1="";
                        String year1="";
                        String day1="";
                        if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
                           day=gen.nextInt(31) + 1;
                        }
                        else if (month==2 && year%4==0) {
                           day=gen.nextInt(29)+1;
                        }
                        else if (month==2 && year%4!=0) {
                           day=gen.nextInt(28)+1;
                        }
                        else {
                           day=gen.nextInt(30)+1;
                        }
                        if (month<10) {
                           month1="0"+month;
                        }
                        if (day<10) {
                           day1="0"+day;
                        }
                        value1=year1+"-"+month1+"-"+day1;
                     }
                     break;
                  }
                  case 'm':
                  {
                     value1=""+((double)gen.nextInt(10000))/100.0;
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        int hour1=gen.nextInt(24);
                        int minute1=gen.nextInt(60);
                        value1=hour1+":"+minute1;
                        break;
                     }
                     else {
                        value1=""+gen.nextInt(256);
                        break;
                     }
                  }
               }

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("DELETE FROM ["+schema1+"].["+table1+"] WHERE ["+column1+"]="+value1+attack+";");
               writer.println("GO");
               break;
            }

            case 26:
            {
//need table1,index1,schema1

//table1               
               int placer=34;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//index1
               String index1="PIndex";

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("DROP INDEX ["+schema1+"].["+table1+"].["+index1+attack+"];");
               writer.println("GO");

               break;
            }
            case 27:
            {
//need table1,schema1

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//table1               
               int placer=34;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("DROP TABLE ["+schema1+"].["+table1+attack+"];");
               writer.println("GO");

               break;
            }
            case 28:
            {
//need table1,schema1

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//table1               
               int placer=34;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("TRUNCATE TABLE ["+schema1+"].["+table1+attack+"];");
               writer.println("GO");

               break;
            }
            case 29:
            {
//need table1,column1,dataType1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//datatyping
               placer=99;
               char dataTypeInput = line.charAt(placer);

//values
               String dataType1="";
               switch(dataTypeInput)
               {
                  case 'n':
                  {
                     dataType1="nvarchar";
                     break;
                  }
                  case 's':
                  {
                     dataType1="smallint";
                     break;
                  }
                  case 'i':
                  {
                     dataType1="integer";
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        dataType1="decimal";
                     }
                     else {
                        dataType1="date";
                     }
                     break;
                  }
                  case 'm':
                  {
                     dataType1 = "money";
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        dataType1="time";
                        break;
                     }
                     else {
                        dataType1="tinyint";
                        break;
                     }
                  }
               }

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("ALTER TABLE ["+schema1+"].["+table1+attack+"] ADD ["+column1+"] "+dataType1+";");
               writer.println("GO");

               break;
            }
            case 30:
            {
//need table1,column1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("ALTER TABLE ["+schema1+"].["+table1+attack+"] DROP COLUMN ["+column1+"];");
               writer.println("GO");

               break;
            }

            case 31:
            {
//need table1,column1,dataType1,schema1

//column1
               int placer = 68;
               ArrayList<Character> charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                         //loop to populate charList
                  if(line.charAt(placer)==' ' || line.charAt(placer)==',') {    //from line
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String column1 = getString(charList);

//table1               
               placer=34;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {                                       //loop to populate char array
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {    //from table area of string
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String table1 = getString(charList);

//datatyping
               placer=99;
               char dataTypeInput = line.charAt(placer);

//values
               String dataType1="";
               switch(dataTypeInput)
               {
                  case 'n':
                  {
                     dataType1="nvarchar";
                     break;
                  }
                  case 's':
                  {
                     dataType1="smallint";
                     break;
                  }
                  case 'i':
                  {
                     dataType1="integer";
                     break;
                  }
                  case 'd':
                  {
                     if(line.charAt(placer+1)=='e') {
                        dataType1="decimal";
                     }
                     else {
                        dataType1="date";
                     }
                     break;
                  }
                  case 'm':
                  {
                     dataType1 = "money";
                     break;
                  }
                  case 't':
                  {
                     if(line.charAt(placer+2)=='m') {
                        dataType1="time";
                        break;
                     }
                     else {
                        dataType1="tinyint";
                        break;
                     }
                  }
               }

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//schema1
               placer=19;
               charList = new ArrayList<Character>();
               for(int i=0;i<=40;i++) {
                  if(line.charAt(placer)==' '||line.charAt(placer)==',') {
                     break;
                  }
                  charList.add(line.charAt(placer));
                  placer++;
               }
               String schema1=getString(charList);

//write
               writer.println("ALTER TABLE ["+schema1+"].["+table1+attack+"] MODIFY ["+column1+"] "+dataType1+";");
               writer.println("GO");

               break;
            }

            case 32:
            {
//need database (Adventureworks2012)

//attack intersperse
               String attack="";
               if (attacksOn) {
                  attackProb=gen.nextInt(attackInterval);
                  if (attackProb==0 || attackDesperation) {
                     attackCount++;
                     int attackType=gen.nextInt(3);
                     switch (attackType) {
                        case 0: {
                           attack=" or \'1\'=\'1\' --";
                           break;
                        }
                        case 1: {
                           attack=" or \'a\'=\'a\' --";
                           break;
                        }
                        case 2: {
                           attack=" waitfor delay \'00:00:10\' --";
                           break;
                        }
                     }
                  }
               }
               else {
               attack="";
               }

//write
               writer.println("DROP DATABASE ["+dbName+"]"+attack+";");
               writer.println("GO");

               break;
            }
            
         }
         statementCount++;

      }
      writer.close();
      //outputFile.close();
      System.out.println("Finished.");
      if(attackCount>1) {
         System.out.println(attackCount+" attacks were included out of "+numberAttacks+", out of "+numberStatements+".");
      }
      else if(attackCount==1) {
         System.out.println(attackCount+" attack was included out of "+numberAttacks+", out of "+numberStatements+".");
      }
      else {
         System.out.println(numberStatements+" statements were written, without attacks.");
      }


   }

   public static String getString(ArrayList<Character> charList)
   {    
      StringBuilder builder = new StringBuilder(charList.size());
      for(Character ch: charList)
      {
         builder.append(ch);
      }
      return builder.toString();
   }





}
