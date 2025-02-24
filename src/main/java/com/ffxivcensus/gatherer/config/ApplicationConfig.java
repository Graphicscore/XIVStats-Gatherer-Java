package com.ffxivcensus.gatherer.config;

/**
 * Application configuration bean encapsulating all of the configuration options available for configuration.
 * 
 * @author matthew.hillier
 */
public class ApplicationConfig {

    public static final String DEFAULT_CONFIG_FILE = "config.xml";
    public static final String DEFAULT_DATABASE_HOST = "mysql://localhost:3306";
    public static final String DEFAULT_DATABASE_NAME = "dbplayers";
    public static final String DEFAULT_TABLE_NAME = "tblplayers";
    public static final int DEFAULT_AUTOSTOP_GAP = 50000;

    /**
     * Safety limit for thread count - user cannot exceed this limit.
     */
    public static final int MAX_THREADS = 256;

    /////////////////////////
    // Database Configuration
    /////////////////////////

    /**
     * The JDBC URL of the database to modify
     */
    private String dbUrl = DEFAULT_DATABASE_HOST;
    /**
     * The Name of the Database to use
     */
    private String dbName = DEFAULT_DATABASE_NAME;
    /**
     * The Username of user of the SQL server user to use.
     */
    private String dbUser;
    /**
     * The password for the user, to use.
     */
    private String dbPassword;
    /**
     * Whether to ignore database SSL verification warnings
     */
    private boolean dbIgnoreSSLWarn = true;

    ////////////////////////
    // Process Configuration
    ////////////////////////

    /**
     * User-defined limit for thread count.
     */
    private int threadLimit = MAX_THREADS;

    /**
     * Wheter the databse should be cleaned upon start (trailing empty ids)
     */
    private boolean skipDatabaseCleanup = false;

    //////////////////////////
    // Gathering Configuration
    //////////////////////////

    /**
     * The character ID to start the gatherer at.
     */
    private int startId = -1;
    /**
     * The character ID to end the gatherer at.
     */
    private int endId = Integer.MAX_VALUE;

    /**
     * The lowest character ID to allow auto-stopping to begin at.
     */
    private int autoStopLowerLimitId = 0;

    /**
     * The largest continuous character gap to trigger auto-stopping.
     */
    private int autoStopGap = DEFAULT_AUTOSTOP_GAP;

    /////////////////////////////////
    // Database Configuration Methods
    /////////////////////////////////

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public boolean isDbIgnoreSSLWarn() {
        return dbIgnoreSSLWarn;
    }

    public void setDbIgnoreSSLWarn(boolean dbIgnoreSSLWarn) {
        this.dbIgnoreSSLWarn = dbIgnoreSSLWarn;
    }

    ////////////////////////
    // Process Configuration
    ////////////////////////

    public int getThreadLimit() {
        return threadLimit;
    }

    public void setThreadLimit(int threadLimit) {
        this.threadLimit = threadLimit;
    }

    public boolean shouldSkipDatabaseCleanup() {
        return skipDatabaseCleanup;
    }

    public void setSkipDatabaseCleanup(boolean skipDatabaseCleanup) {
        this.skipDatabaseCleanup = skipDatabaseCleanup;
    }

    //////////////////////////
    // Gathering Configuration
    //////////////////////////

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public int getAutoStopLowerLimitId() {
        return autoStopLowerLimitId;
    }

    public void setAutoStopLowerLimitId(int autoStopLowerLimitId) {
        this.autoStopLowerLimitId = autoStopLowerLimitId;
    }

    public int getAutoStopGap() {
        return autoStopGap;
    }

    public void setAutoStopGap(int autoStopGap) {
        this.autoStopGap = autoStopGap;
    }
}