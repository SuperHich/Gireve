package com.dotit.gireve.entity;

/**
 * Created by Hichem Laroussi @SH on 08/04/2015.
 */
public class EVSEStatusEnum {

    public enum Status {

        Unspecified(0),
        OutOfOrder(1),
        Available(2),
        Future(3);


        private final int id;
        Status (int id) { this.id = id; }
        public int getValue() { return id;}

        public static String valueToStatus (int value) {

            for (Status a : Status.values()) {
                if (value == a.getValue())
                    return a.toString();
            }
            return null;
        }

    }
}
