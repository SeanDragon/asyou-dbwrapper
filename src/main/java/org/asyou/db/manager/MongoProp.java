package org.asyou.db.manager;

/**
 * Created on 17/10/22 20:33 星期日.
 *
 * @author sd
 */
public class MongoProp extends DbProp {
    private String hostName;
    private Integer port;
    private String dbName;
    private Integer poolSize;
    private Integer maxWaitTime;
    private Integer connectTimeout;
    private Integer blockSize;

    public MongoProp(String id) {
        super(id);
    }

    public String getHostName() {
        return hostName;
    }

    public MongoProp setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public MongoProp setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public MongoProp setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public MongoProp setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
        return this;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public MongoProp setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
        return this;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public MongoProp setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Integer getBlockSize() {
        return blockSize;
    }

    public MongoProp setBlockSize(Integer blockSize) {
        this.blockSize = blockSize;
        return this;
    }
}
