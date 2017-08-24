package com.ffxivcensus.gatherer.config;

/**
 * Application configuration bean encapsulating all of the configuration options available for configuration.
 * By default, the following sensible defaults are set:
 * <dl>
 * <dt>{@link #dbUrl}</dt>
 * <dd>{@link #DEFAULT_DATABASE_HOST}</dd>
 * <dt>{@link #quiet}</dt>
 * <dd>true</dd>
 * <dt>{@link #storeProgression}</dt>
 * <dd>true</dt>
 * <dt>{@link #dbIgnoreSSLWarn}</dt>
 * <dd>true</dd>
 * </dl>
 * 
 * @author matthew.hillier
 */
public class ApplicationConfig {

    public static final String DEFAULT_DATABASE_HOST = "mysql://localhost:3306";
    public static final String DEFAULT_DATABASE_NAME = "dbplayers";
    public static final String DEFAULT_TABLE_NAME = "tblplayers";

    /**
     * Safety limit for thread count - user cannot exceed this limit.
     */
    public final static int MAX_THREADS = 64;

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
    /**
     * String field to store the table name to write records to (if not splitting tables).
     */
    private String tableName = DEFAULT_TABLE_NAME;
    /**
     * Boolean field indicating whether to split character records across multiple tables, with one table for each realm/server.
     */
    private boolean splitTables;
    /**
     * Suffix to be appended to all tables e.g. 15012016
     */
    private String tableSuffix = "";

    ////////////////////////
    // Process Configuration
    ////////////////////////

    /**
     * User-defined limit for thread count.
     */
    private int threadLimit;
    /**
     * Whether to provide console output.
     */
    private boolean quiet = true;
    /**
     * Whether to output all console output.
     */
    private boolean verbose;
    /**
     * Whether to output failed records
     */
    private boolean printFails;

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
    private int endId = -1;
    /**
     * Boolean value indicating whether to store a comma delimited list of minions to database.
     */
    private boolean storeMinions;
    /**
     * Boolean value indicating whether to store a comma delimited list of mounts to database.
     */
    private boolean storeMounts;
    /**
     * Boolean value indicating whether to store bit fields indicating player achievements/progress.
     */
    private boolean storeProgression = true;
    /**
     * Whether to store player activity dates
     */
    private boolean storeActiveDate;
    /**
     * Whether to store player activity bit
     */
    private boolean storePlayerActive;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isSplitTables() {
        return splitTables;
    }

    public void setSplitTables(boolean splitTables) {
        this.splitTables = splitTables;
    }

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
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

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isPrintFails() {
        return printFails;
    }

    public void setPrintFails(boolean printFails) {
        this.printFails = printFails;
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

    public boolean isStoreMinions() {
        return storeMinions;
    }

    public void setStoreMinions(boolean storeMinions) {
        this.storeMinions = storeMinions;
    }

    public boolean isStoreMounts() {
        return storeMounts;
    }

    public void setStoreMounts(boolean storeMounts) {
        this.storeMounts = storeMounts;
    }

    public boolean isStoreProgression() {
        return storeProgression;
    }

    public void setStoreProgression(boolean storeProgression) {
        this.storeProgression = storeProgression;
    }

    public boolean isStoreActiveDate() {
        return storeActiveDate;
    }

    public void setStoreActiveDate(boolean storeActiveDate) {
        this.storeActiveDate = storeActiveDate;
    }

    public boolean isStorePlayerActive() {
        return storePlayerActive;
    }

    public void setStorePlayerActive(boolean storePlayerActive) {
        this.storePlayerActive = storePlayerActive;
    }
}