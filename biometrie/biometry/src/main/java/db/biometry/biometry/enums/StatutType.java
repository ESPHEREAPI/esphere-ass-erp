/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package db.biometry.biometry.enums;

/**
 *
 * @author USER01
 */
public enum StatutType {
    UNKNOWN(-1),
    TYPE1(1);
    //TYPE2(2);

    private final int value;

    StatutType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}