package com.adeadfed.preferences;

public abstract class Preference {
    public abstract String getDefault() throws Exception;
    public abstract String getPersistentKey();
}
