package com.ijudy.races;

/**
 * Current status for the relationship you have to the Race<p>
 *     <CODE>
 *         GOING - I am Going or Attending this Race<p>
 *         NOT_GOING - This is only really when you change your mind.<p>
 *         INTERESTED - You are not committed to this race but want it to be noted in your calendar<p>
 *         VOLUNTEERING - You are  Volunteering for this Race<p>
 *         GOING_VOLUNTEERING - You are both attending and volunteering for this race
 *         NOT_ASSIGNED - Initial State
 *     </CODE>
 */
public enum MyRaceStatus {

    GOING("GOING"),
    NOT_GOING("NOT_GOING"),
    NOT_ASSIGNED("NOT_ASSIGNED"),
    INTERESTED("INTERESTED"),
    MAYBE("MAYBE"),
    VOLUNTEERING("VOLUNTEERING"),
    GOING_VOLUNTEERING("GOING_VOLUNTEERING");

    private String key;

    /**
     * Constructor where the value matches the
     * pk for the entry
     * @param key
     */
    MyRaceStatus(String key){
        this.key = key;
    }

    /**
     * The Key for this Race Status
     * @return Key used for Persistence
     */
    public String getKey(){
        return key;
    }

    /**
     * Given a String convert to the ENUM
     *
     * @param name
     * @return
     */
    public MyRaceStatus toEnum(String name){
        if (GOING.key.equalsIgnoreCase(name)){
            return MyRaceStatus.GOING;
        } else if (INTERESTED.key.equalsIgnoreCase(name) || MAYBE.key.equalsIgnoreCase(name)) {
            return MyRaceStatus.MAYBE;
        } else if (NOT_ASSIGNED.key.equalsIgnoreCase(name)) {
            return MyRaceStatus.NOT_ASSIGNED;
        } else if (VOLUNTEERING.key.equalsIgnoreCase(name)) {
            return MyRaceStatus.VOLUNTEERING;
        } else if (GOING_VOLUNTEERING.key.equalsIgnoreCase(name)) {
            return MyRaceStatus.GOING_VOLUNTEERING;
        } else if (NOT_GOING.key.equalsIgnoreCase(name)) {
            return MyRaceStatus.NOT_GOING;
        } else {
            return MyRaceStatus.NOT_ASSIGNED;
        }
    }
}
