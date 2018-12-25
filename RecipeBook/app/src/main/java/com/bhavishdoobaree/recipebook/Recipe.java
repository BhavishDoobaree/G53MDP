package com.bhavishdoobaree.recipebook;


import java.util.Comparator;

public class Recipe {

    private int _rcpid;
    private String _rname;
    private String _rdetails;

    public Recipe()
    {

    }

    public Recipe(int rcpid, String rname, String rdetails) {

        this._rcpid = rcpid;
        this._rname = rname;
        this._rdetails = rdetails;

    }

    public Recipe(String rname, String rdetails)
    {
        this._rname = rname;
        this._rdetails = rdetails;
    }

    //used in list view to convert CP to name of recipe on list

    @Override
    public String toString(){
        return this.get_rname();
    }


    //definition of get and set methods for very characteristic of recipe

    public void set_rcpid(int setrcpid)
    {
        this._rcpid = setrcpid;
    }

    public int get_rcpid()
    {
        return this._rcpid;
    }

    public void set_rname(String setrname)
    {
        this._rname = setrname;
    }

    public String get_rname()
    {
        return this._rname;
    }

    public void set_rdetails(String setrdetails)
    {
        this._rdetails = setrdetails;
    }

    public String get_rdetails()
    {
        return this._rdetails;
    }

    //comparator for sorting in alphabetical order on list of recipes

    public static Comparator<Recipe> RecipeNameComparator = (recipe1, recipe2) -> {
        String name1 = recipe1.get_rname();
        String name2 = recipe2.get_rdetails();

        return name1.compareToIgnoreCase(name2);
    };


}
