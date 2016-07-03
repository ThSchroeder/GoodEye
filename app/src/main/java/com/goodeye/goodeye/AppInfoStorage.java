package com.goodeye.goodeye;


import java.util.List;

public abstract class AppInfoStorage {

    // this unlocks an id
    // NOTE: id is guaranteed to be unique
    public abstract void UnlockID(String id);

    // this returns true if id is unlocked
    // NOTE: id is guaranteed to be unique
    public abstract boolean IsUnlockedID(String id);

    // this returns a string list which contains all unlocked ids
    public abstract List<String> GetAllUnlockedIDs();

    // this sets a value
    // NOTE: the values name is guaranteed to be a unique string
    public abstract void SetValue(String name, String value);

    // this returns a value
    // NOTE: the values name is guaranteed to be a unique string
    public abstract String GetValue(String name);

}
