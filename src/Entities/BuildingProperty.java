package Entities;

import Enums.ContractType;

public class BuildingProperty extends RealEstate{

    protected String independenceType;
    protected ContractType contractType;

    public BuildingProperty(int id, int customerId, String title, String description, double size, String location, double price, String independenceType, ContractType contractType) {
        super(id, customerId, title, description, size, location, price);
        this.independenceType = independenceType;
        this.contractType = contractType;
    }

    public BuildingProperty(int customerId, String title, String description, double size, String location, double price, String independenceType, ContractType contractType) {
        super(customerId, title, description, size, location, price);
        this.independenceType = independenceType;
        this.contractType = contractType;
    }

    public BuildingProperty(int id, String independenceType, ContractType contractType) {
        this.setId(id);
        this.independenceType = independenceType;
        this.contractType = contractType;
    }

    public BuildingProperty()
    {
    }

    public String getIndependenceType() {
        return independenceType;
    }

    public void setIndependenceType(String independenceType) {
        this.independenceType = independenceType;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }
}
