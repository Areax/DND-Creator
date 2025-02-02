package creator;
//Character Creation for DND
//Author: Tyler Clark
//Created in October 2019

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import dnd.*;


class Item
{

    private String name;
    private String description;
    private String rarity;
    private int cost;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String newDescript)
    {
        this.description = newDescript;
    }    
    
    public String getRarity()
    {
        return rarity;
    }
    
    public void setRarity(String rarity)
    {
        this.rarity = rarity;
    }
    
    public int getCost()
    {
        return cost;
    }
    
    public void setCost(int cost)
    {
        this.cost = cost;
    }
   
}

class Character
{
    private int inventorySpots;
    public Item[] backpack;
    public Weapon[] weaponBag;
    public boolean current;
    private String name, race, gender, charClass;
    public int maxHealth, currentHealth;
    public int[] stats;
    private int experience, profBonus, level, str, dex, con, intl, wis, cha, gold;
    private static int[] profBonusChart = {1,5,9,13,17};
    public static int[] proficiencyType = {1,2,2,2,4,4,4,4,4,5,5,5,5,5,6,6,6,6};
    public static String[] proficiencies = {"Athletics", "Acrobatics", "Sleight of Hand", "Stealth", 
    "Arcana", "History","Investigation","Nature",
    "Religion","Animal Handling","Insight","Medicine","Perception",
    "Survival","Deception","Intimidation","Performance","Persuasion"};
    public int[] bonuses;
    public static String[] variables = {"Name", "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", 
    "Charisma", "Class", "Gender","Level", "Experience", "Proficiencies"};
    public static String[] classes = {"Peasant", "Fighter","Wizard", "Rogue","Cleric","Paladin","Warlock","Barbarian"};
    public static int[] healthDice = {3,10,6,8,8,10,6,12}; //Dice used to calculate Health for a specific class.
    public static int[] expThreshhold = {0,300,900,2700,6500,14000,23000,34000,48000,64000,85000,100000,120000,140000,
    165000, 195000, 225000, 265000, 305000, 355000}; //The set experience threshholds for dnd 5e. They do not follow a formula, which was pretty annoying
    private Weapon primaryHand;
    private Weapon offHand;
    
    public Character() //Happens on initialization of every character
    {
        this.inventorySpots = 30;
        backpack = new Item[30];
        weaponBag = new Weapon[10];
        bonuses = new int[proficiencies.length];
        this.gender = "Genderless";
        setLevel(1);
        setClass("Peasant");
        setStr(10);//set all stats to 10
        setDex(10);
        setCon(10);
        setIntl(10);
        setWis(10);
        setCha(10);
        
    }
    
    public boolean isBagRoom(int weight)
    {
        int totalWeight = 0;
        for(int i = 0; i < 10; i++)
        {
            if(weaponBag[i] != null)
            {
                totalWeight = totalWeight + 3;
            }
        }
        for(int i = 0; i < 30; i++)
        {
            if(backpack[i] != null)
            {
                totalWeight++;
            }
        }
        if((totalWeight + weight) <= 30)
        {
            return true;
        }
        return false;
    }
    
    public int findBackpackSpace()
    {
        for(int i = 0; i < backpack.length; i++)
        {
            if(backpack[i] == null)
            {
                return i;
            }
        }
        return -1;
    }
    
    public int findWeaponBagSpace()
    {
        for(int i = 0; i < weaponBag.length; i++)
        {
            if(weaponBag[i] == null)
            {
                return i;
            }
        }
        return -1;
    }
    
    public boolean removeItem(Item item)
    {
        
        for(int i = 0; i < weaponBag.length; i++)
        {
            if(item.getName().equals(weaponBag[i].getName()) && item.getDescription().equals(weaponBag[i].getDescription()))
            {
                weaponBag[i] = null;
                inventorySpots++;
                return true;
            }
            
        }
        return false;
    }
    
    public void addItem(Item item)
    {
        if(isBagRoom(1))
        {
            backpack[findBackpackSpace()] = item;
            inventorySpots--;
        }
        else
        {
            System.out.println("Couldn't add item to bag, there's no room!");
        }
    }
    
    public void addItem(Weapon weapon)
    {
        if(isBagRoom(3))
        {
            weaponBag[10 - inventorySpots] = weapon;
            inventorySpots--;
        }
        else
        {
            System.out.println("Couldn't add item to bag, there's no room!");
        }
    }
    
    public dnd.Weapon getPrimaryHand()
    {
        return primaryHand;
    }
    
    public void setPrimaryHand(dnd.Weapon weapon)
    {
        this.primaryHand = weapon;
    }
    
    public dnd.Weapon getOffHand()
    {
        return offHand;
    }
    
    public void setOffHand(dnd.Weapon weapon)
    {
        this.offHand = weapon;
    }
    public int getGold()
    {
        return gold;
    }
    
    public void setGold(int setGold)
    {
        if(setGold >= 0) //You can't have negative gold!
            this.gold = setGold;
        
    }
    
    public void addGold(int add)
    {
        if((gold + add) >= 0) //You can't subtract gold past 0
            this.gold = gold + add;
    }
    
    public int getProfBonus()
    {
        
        for(int i = 0; i < profBonusChart.length; i++)
        {
            if(profBonusChart[i] > level)
            {
                if(i == 0)
                {
                    this.profBonus = 0;
                    return profBonus;
                }
                else
                {
                    this.profBonus = i + 1; //Bonus starts at 2 and increases by 1 at 5,9,13,17
                    return profBonus;
                }
                
            }
        }
        this.profBonus = 6;
        
        return profBonus;
    }
    
    public int getModfier(String skill)
    {
        getProfBonus();
        refresh(); //Makes sure there wasn't change to stats. Shouldn't be necessary given how rarely stats change, but reassurance is worth computation
        for(int i = 0; i < proficiencies.length; i++)
        {
            if(proficiencies[i].equalsIgnoreCase(skill) || (i == 2 && skill.equalsIgnoreCase("sleight")))
                return ((bonuses[i]*profBonus) + (stats[proficiencyType[i] - 1])/2 - 5);
            if(proficiencies[i].equalsIgnoreCase(skill) || (i == 9 && skill.equalsIgnoreCase("animal")))
                return ((bonuses[i]*profBonus) + (stats[proficiencyType[i] - 1])/2 - 5);
        }
        System.out.println("This is not a known skill, modifier is 0.");
        return -100;
    }
    
    public void refresh()
    {
        stats = new int[6];
        stats[0] = str;
        stats[1] = dex;
        stats[2] = con;
        stats[3] = intl;
        stats[4] = wis;
        stats[5] = cha;
    }
    
    
    public String getCharClass()
    {
        return charClass;
    }
    
    public int getMaxHealth()
    {
        return maxHealth;
    }
    
    public int getCurrentHealth()
    {
        return currentHealth;
    }
    
    public boolean setSkill(String prof)
    {
        for(int i = 0; i < proficiencies.length; i++)
        {
            if(proficiencies[i].equalsIgnoreCase(prof)|| (i == 2 && prof.equalsIgnoreCase("sleight")) || (i == 9 && prof.equalsIgnoreCase("Animal")))
            {
                this.bonuses[i] = 1;
                return true;
            }
        }
        return false;
    }
    
    public void setClass(String newClass)
    {
        boolean foundClass = false;
        for(int i = 0; i < classes.length; i++)
        {
            if(newClass.equalsIgnoreCase(classes[i]))
            {
                foundClass = true;
                charClass = classes[i];
                this.maxHealth = 0;
                changeHealth(level - 1);
                this.currentHealth = maxHealth;
            }
        }
        if(!foundClass)
        {
            System.out.println("No class with that name could be found. Class is not changed.");
        }
        
    }
    public void changeHealth(int change)
    {
        int gain = 0;
        if(charClass == null)
        {
            this.charClass = classes[0];
        }
        for(int i = 0; i < classes.length; i++)
        {
            if(charClass.equalsIgnoreCase(classes[i]))
            {
                if(level == 0 || level == 1)
                {
                    this.maxHealth = Math.max((healthDice[i] + (con/2) - 5),5); //For the sake of my mental health, set minimum level 1 health to 5
                    this.currentHealth = maxHealth;
                }
                else
                {
                    for(int j = 0; j < Math.abs(change); j++) //Absolute value of change because change can be negative
                    {
                        if(change > 0)
                        {
                            gain = CharacterCreate.roll(healthDice[i],con);
                            System.out.println(getName() + " gained " + Math.max(gain,0) + " health!");
                            this.maxHealth = maxHealth + Math.max(gain,0); //You can't lose health from leveling up
                            this.currentHealth = maxHealth;
                        }
                        else
                        {
                            gain = CharacterCreate.roll(healthDice[i],con);
                            System.out.println(getName() + " lost " + Math.max(gain,0) + " health!");
                            this.maxHealth = maxHealth - Math.max(gain,0); //You can't gain health from leveling down
                            this.currentHealth = maxHealth;
                        }
                        
                    }
                }
                break;
            }
        }
        
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public int calculateLevel()
    {
        for(int i = 0; i < 20; i++)
        {
            if(experience < expThreshhold[i])
            {
                if((i + 1)!= level)
                    changeHealth((i + 1) - level);
                return i + 1;
            }
        }
        if(level != 20)
            changeHealth(20 - level); //I've stared at health gains so long that I'm not even sure this is correct. But it works. I must be psyching myself out.
        return 20;
        
    }
    
    public void setLevel(int newLevel)
    {
        int oldLevel = level;
        if(newLevel > 20)
            newLevel = 20;
        if(newLevel < 1)
            newLevel = 1;
        this.experience = expThreshhold[newLevel - 1];
        this.level = newLevel;
        changeHealth(newLevel - oldLevel);
    }
    
    public int getExp()
    {
        return experience;
    }
    
    public void setExp(int newExp)
    {
        this.experience = newExp;
        this.level = calculateLevel();
    }
    
    public void gainExp(int newExp)
    {
        this.experience = experience + newExp;
        this.level = calculateLevel();
    }
    
    public String getGender()
    {
        return gender;
    }
    
    public void setGender(String newGender)
    {
        this.gender = newGender;
    }
    
    public void setCurrent(boolean curr)
    {
        this.current = curr;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String newName)
    {
        this.name = newName;
        
    }
    
    public int getStr()
    {
        return str;
    }
    
    public void setStr(int newVar)
    {
        this.str = newVar;
    }
    
    public int getDex()
    {
        return dex;
    }
    
    public void setDex(int newVar)
    {
        this.dex = newVar;
    }
    
    public int getCon()
    {
        return con;
    }
    
    public void setCon(int newVar)
    {
        this.con = newVar;
    }
    
    public int getIntl()
    {
        return intl;
    }
    
    public void setIntl(int newVar)
    {
        this.intl = newVar;
    }
    
    public int getWis()
    {
        return wis;
    }
    
    public void setWis(int newVar)
    {
        this.wis = newVar;
    }
    
    public int getCha()
    {
        return cha;
    }
    
    public void setCha(int newVar)
    {
        this.cha = newVar;
    }
    
    public void rollEverything()
    {
        
        setStr(statRoller());
        setDex(statRoller());
        setCon(statRoller());
        setIntl(statRoller());
        setWis(statRoller());
        setCha(statRoller());
        refresh();
    }
    
    private int statRoller() //Each stat is made by rolling 4 d6 dice and adding together the largest 3.
    {
        int a, b, c, d, stat;
        a = CharacterCreate.roll(6, 10);
        b = CharacterCreate.roll(6, 10);
        c = CharacterCreate.roll(6, 10);
        d = CharacterCreate.roll(6, 10);
        int min = Math.min(Math.min(a,b),Math.min(c,d)); //We don't know which variable out of a, b, c, d is the biggest but we can find the minimum value
        stat = a + b + c + d - min; //In order to get rid of the lowest value, add all of them up then remove the lowest value
        return stat;
    }

}

public class CharacterCreate
{
 
    public static String workingCommands = "Help, Quit, Get [Variable], Set [Variable] [Value], Set [Class], Variables, Roll Stats, Roll [Number of Sides on Die], Roll [Stat/Skill]," 
    + "\nNew Character, New Character [Name], List, Give Exp [Value], Skills, Info, Switch [Character Name]";
    public static String workingVariables = "Name, Str, Dex, Con, Wis, Intl, Cha, Class, Level, Exp, Proficiency, Gender";
    
    public static boolean isInteger(String str) {
    if (str == null) {
        return false;
    }
    int length = str.length();
    if (length == 0) {
        return false;
    }
    int i = 0;
    if (str.charAt(0) == '-') {
        if (length == 1) {
            return false;
        }
        i = 1;
    }
    for (; i < length; i++) {
        char c = str.charAt(i);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
    }
    
    public static Character findCurrent(ArrayList<Character> roster)
    {
        Character temp;
        for(int i = 0; i < roster.size(); i++)
        {
            temp = roster.get(i);
            if(temp.current)
            {
                return temp;
            }
        }
        return null;
    }
    
    
    
    
    public static int roll(int dice, int modifier)
    {
        Random random = new Random();
        int roll = random.nextInt(dice) + 1;
        if(roll == 1 && dice == 20)
            System.out.println("Critical failure!");
        if(roll == 20 && dice == 20)
            System.out.println("Critical Success!");
        roll = roll + (modifier/2) - 5;
        return roll;
    }
    //written by MAM131
    public static String shorten(String word) //shortens the word so you can use shortened and long version of the stats.
    {
        if(word.equalsIgnoreCase("Strength"))
        {
            word = word.substring(0,3);
            return word;
            
        }
        else if(word.equalsIgnoreCase("Dexterity"))
        {
            word = word.substring(0,3);
            return word;
             
        }
        else if(word.equalsIgnoreCase("Constitution"))
        {
            word = word.substring(0,3);
            return word;
            
        }
        else if(word.equalsIgnoreCase("Intelligence"))
        {
            word = word.substring(0,4);
            return word;
            
        }
        else if(word.equalsIgnoreCase("Wisdom"))
        {
            word = word.substring(0,3);
            return word;
            
        }
        else if(word.equalsIgnoreCase("Charisma"))
        {
            word = word.substring(0,3);
            return word;
            
        }
        return word;
    }

    public static void main(String args[])
    {

        boolean quit = false;
        String[] names;
        String nameLine = "Failed";
        Scanner scanner = new Scanner(System.in);
        Character first = new Character();
        String line = new String();
        Random random = new Random();
        int binary;
        first.calculateLevel();
        try //Try to find the text file in, the 'try' and 'catch' test to see if the file is found
        {
            BufferedReader in = new BufferedReader(new FileReader("names.txt"));
            try
            {
                nameLine = in.readLine(); //If it passes both tests, scan in the first line (All of the first names)
                in.close();
            }
            catch (IOException io)
            {
                names = new String[1]; //If it fails either one, just put the default name in instead so that there's still a name to add
                names[0] = "John";
            }
        }
        catch (FileNotFoundException ex)
        {
            names = new String[1];
            names[0] = "John";
        }
        names = nameLine.split(", "); //This splits the single line into multiple smaller strings (individual names) by splitting it at every comma followed by a space
        binary = random.nextInt() % 2;
        if(binary == 1)
            first.setGender("Male");
        else
            first.setGender("Female");
        first.setCurrent(true);
        first.setName(names[random.nextInt(names.length)]); //random name
        ArrayList<Character> roster = new ArrayList<Character>(); //New ArrayList of characters, ArrayLists are much better than arrays at adding a continuous amount of entries
        roster.add(first); //Add character to roster
        
        WeaponsLibrary library =  new WeaponsLibrary("weapons.txt"); //Calls from weapons.txt for the list of possible weapons
        ArrayList<Weapon> weaponLib = library.weaponLib;
        for(int i = 0; i < weaponLib.size(); i++)
        {
            System.out.print(weaponLib.get(i).getName() + " has damage ");
            for(int j = 0; j < weaponLib.get(i).getDamageType().length; j++)
            {
                System.out.print(weaponLib.get(i).getDamageDice(2*j) + "d" + weaponLib.get(i).getDamageDice(2*j + 1) + " " + weaponLib.get(i).getDamageType(j) + " ");
            }
            System.out.print("\n");
        }
        System.out.println("Welcome to my text based character creator! Type 'HELP' for a list of commands.");
        while(!quit)
        {
            line = scanner.nextLine(); //Have the user input a text line
            if(line.equalsIgnoreCase("quit"))
            {
                break;
            }
            roster = GUI.textInput(line, roster, names, false, null); //Analyze the line using textInput function, update the roster with any changes
        }
        scanner.close();
    }
}