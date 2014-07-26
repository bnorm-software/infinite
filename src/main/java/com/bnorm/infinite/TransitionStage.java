package com.bnorm.infinite;

/**
 * An enumeration of the different stages of a state transition.
 *
 * @author Brian Norman
 * @since 1.1.0
 */
public enum TransitionStage {

    /**
     * Describes the stage of a state transition before the source state is exited.
     */
    Before,

    /**
     * Describes the stage of a state transition between the source and destination states.
     */
    Between,

    /**
     * Describes the stage of a state transition after the destination state has been entered.
     */
    After,

    // End of enumeration
    ;
}
