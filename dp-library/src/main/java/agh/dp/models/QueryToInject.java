package agh.dp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QueryToInject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer operationLevel;
    private String operationName;
    private String tableName;

    public QueryToInject() {
    }

    public QueryToInject(Long id, Integer operationLevel, String operationName, String tableName){
        this.id = id;
        this.operationLevel = operationLevel;
        this.operationName = operationName;
        this.tableName = tableName;
    }

    public QueryToInject(Integer operationLevel, String operationName, String tableName){
        this.operationLevel = operationLevel;
        this.operationName = operationName;
        this.tableName = tableName;
    }

    public Long getId() {
        return id;
    }

    public Integer getOperationLevel() {
        return operationLevel;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperationLevel(Integer operationLevel) {
        this.operationLevel = operationLevel;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
