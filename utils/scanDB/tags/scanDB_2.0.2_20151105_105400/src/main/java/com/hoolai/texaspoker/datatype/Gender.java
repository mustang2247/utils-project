package com.hoolai.texaspoker.datatype;

public enum Gender {
    
    FEMALE(0),
    
    MALE(1),
    
    SHEMALE(2);
    
    private final int value;
    
    private Gender(int value) {
        this.value = value;
    }

    public int value(){
        return value;
    }
    
    public static Gender valueOf(int value){
        switch (value) {
        case 0:
            return FEMALE;
        case 1:
            return MALE;
        case 2:
            return SHEMALE;
        default:
            throw new RuntimeException("not supported gender");
        }
    }
    
}